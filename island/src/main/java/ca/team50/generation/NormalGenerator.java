package ca.team50.generation;

import ca.lenoverd.city.CityGenerator;
import ca.lenoverd.city.NameGenerator;
import ca.lenoverd.city.NetworkGenerator;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.Arctic.ArcticUtils;
import ca.team50.Tiles.BiomeType;
import ca.team50.Tiles.Deserts.DesertsUtils;
import ca.team50.Tiles.LakeTile;
import ca.team50.Tiles.OceanTile;
import ca.team50.Tiles.TileType;
import ca.team50.Tiles.Tropical.TropicalUtils;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.elevation.ElevationType;
import ca.team50.elevation.Mountains;
import ca.team50.elevation.Plains;
import ca.team50.elevation.Volcano;
import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.GenerationException;
import ca.team50.shapes.*;
import ca.team50.soilAbsorption.*;
import ca.team50.specification.CLInterfaceIsland;
import ca.team50.water.AquiferGenerator;
import ca.team50.water.LakeGenerator;
import ca.team50.water.RiverCentroidsGenerator;

import java.util.ArrayList;
import java.util.List;


public class NormalGenerator implements IslandGenerable {

    private CLInterfaceIsland specification;

    public NormalGenerator(CLInterfaceIsland islandSpecification) {
        this.specification = islandSpecification;
    }

    // Main island generation
    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        System.out.println("Generating Island...");
        System.out.println("Please note this may take sometime depending on the size of the canvas and the number of polygons!");

        try {

            // Given generator.jar gives polygons random properties, we want to default all of them specifically for island generation
            for (Polygons currentPolygon : mesh) {
                currentPolygon.cleanProperties();
            }

            // Determine noise evaluation position
            long noiseEvaluationPosition = 1;

            // Define position for maximum points (i.e. so that shapes do not generate a size larger than the canvas)
            Structs.Vertex max = CanvasUtils.getMaxPoint(mesh);
            Structs.Vertex middle = CanvasUtils.getCenter(mesh);

            // Specify extra tiles
            TileType lakeTile = new LakeTile();
            TileType oceanTile = new OceanTile();

            // Get island shape
            IslandShape islandShape = getShape(mesh,specification,max,middle,noiseEvaluationPosition);

            // GENERATOR VARIABLES
            // MAX RADIUS
            // We use canvas center for middle of island as all islands will be based from the center of the canvas
            double maxRadiusCanvasCenter = getMaxIslandDistance(mesh,islandShape,middle);

            double maxRadius = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition,0,maxRadiusCanvasCenter);
            // THRESHOLD ALTITUDE
            double altitude = 1.0 - GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.1,0,1);
            //RIVER THRESHOLD
            double rivAltitude = 1.0 - GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.15,0.1,0.3);

            // Special case where elevation is plains, noise evaluation on elevation where rivers should be considered shall be much more broad
            if (specification.getElevationType() == ElevationType.PLAINS) {
                rivAltitude = 0 + GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.15,0.2,1);
            }


            // ------ GENERATORS ------ //
            // Assign altitude
            assignAltitude(mesh,specification,islandShape,noiseEvaluationPosition,max,middle);

            // Get soil profile
            SoilProfile profile = getSoilProfile(specification);
            double maxDistanceOnIsland = CanvasUtils.maxDistFromIslandCent(mesh,islandShape);

            // Aquifer generation
            AquiferGenerator aquiferGenerator = new AquiferGenerator();
            AquiferGenerator aquifer = aquiferGenerator.generateAquifers(mesh,islandShape);

            // Lake generation
            LakeGenerator lakeGenerator = new LakeGenerator(mesh,islandShape,specification.getNumLakes(),maxRadius,altitude,specification.getSeed());

            // Generate Rivers
            RiverCentroidsGenerator rivers = new RiverCentroidsGenerator(mesh, specification.getNumRivers(), rivAltitude);

            // Generate Cities
            CityGenerator cityGenerator = new CityGenerator(mesh, specification.getNumCities(), islandShape, specification.getSeed());

            // Network Generator
            NetworkGenerator netGen = new NetworkGenerator(mesh,islandShape);
            netGen.createStarNetwork(cityGenerator);

            // Name generator
            NameGenerator nameGen = new NameGenerator(specification.getNameDatasetFilePath(),4);

            // ------ ASSIGN COLOURS TO POLYGONS ------ //

            // Assign colours to polygons
            for (Polygons curPoly : mesh) {

                Structs.Vertex centroid = curPoly.getCentroid();

                // Check if polygon exists within island
                if (islandShape.isVertexInside(centroid)) {

                    // Compute humidity
                    profile.computeRemainingWater(curPoly,lakeGenerator,aquiferGenerator, maxDistanceOnIsland);

                    // Check altitude and assign tile colour accordingly
                    double polygonAltitude = extractProperties(centroid.getPropertiesList(), "altitude");
                    double polygonHumidity = extractProperties(centroid.getPropertiesList(), "humidity");

                    if (specification.getBiomeType().equals(BiomeType.Tropical)) {
                        curPoly.unifyColor(TropicalUtils.getTileFormProperty(polygonAltitude, polygonHumidity).getTileColour());
                    } else if (specification.getBiomeType().equals(BiomeType.Arctic)) {
                        curPoly.unifyColor(ArcticUtils.getTileFormProperty(polygonAltitude, polygonHumidity).getTileColour());
                    } else if (specification.getBiomeType().equals(BiomeType.Deserts)) {
                        curPoly.unifyColor(DesertsUtils.getTileFormProperty(polygonAltitude, polygonHumidity).getTileColour());
                    }

                } else {
                    curPoly.unifyColor(oceanTile.getTileColour());
                }

                // Check if the polygon existed with a lake and assign colour accordingly
                if (lakeGenerator.isPolygonApartOfLake(curPoly)) {
                    curPoly.unifyColor(lakeTile.getTileColour());
                }

                // Check for city
                if (cityGenerator.isVertexACity(centroid)) {

                    curPoly.addProperty("IsCity","True");
                    curPoly.addProperty("CityName",nameGen.generateName(9));
                    curPoly.unifyColor(cityGenerator.getCityType(centroid).getTileColour());

                }

            }

            // Finally, apply star network to mesh for visualizer to render
            netGen.applyStarNetworkToMesh();

        } catch (GenerationException e) {
            ExceptionHandler.handleException(e);
        } catch (Exception e) {
            ExceptionHandler.handleException(new GenerationException("Generation failed! Please check arguments and try again"));
        }




    }

    // Method to generate shape
    private IslandShape getShape(PolyMesh<Polygons> mesh, CLInterfaceIsland specification, Structs.Vertex max, Structs.Vertex middle, long noiseEvaluationPosition) {

        IslandShape islandShape = null;

        // Get island shape
        if (specification.getShapeType().equals(IslandShapeType.CIRCLE)) {

            // CIRCLE
            double maxRadiusCanvasCenter = Math.sqrt(Math.pow(max.getX()-middle.getX(),2) + Math.pow(max.getY()-middle.getY(),2));
            double circleRadiusNoise = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.1,maxRadiusCanvasCenter*0.5,maxRadiusCanvasCenter*0.9);

            islandShape = new Circle(CanvasUtils.getCenter(mesh),circleRadiusNoise);

        } else if (specification.getShapeType().equals(IslandShapeType.ELIPSE)) {

            // ELIPSE
            double maxRadiusCanvasCenter = Math.sqrt(Math.pow(max.getX()-middle.getX(),2) + Math.pow(max.getY()-middle.getY(),2));
            double width = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.2,maxRadiusCanvasCenter*0.5,maxRadiusCanvasCenter*0.9);
            double height = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.3,maxRadiusCanvasCenter*0.5,maxRadiusCanvasCenter*0.9);
            double rotation = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.4,-6.28,6.28);

            islandShape = new Elipse(CanvasUtils.getCenter(mesh),height,width,rotation);

        } else if (specification.getShapeType().equals(IslandShapeType.RECTANGLE)) {

            // RECTANGLE
            double maxRadiusX = max.getX() - middle.getX();
            double maxRadiusY = max.getY() - middle.getY();
            double staringPosX = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.5,maxRadiusX*0.5,maxRadiusX);
            double staringPosY = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.6,maxRadiusX*0.5,maxRadiusX);
            double increaseX = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.7,maxRadiusX+1,(maxRadiusX*2)*0.9);
            double increaseY = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.8,maxRadiusY+1,(maxRadiusY*2)*0.95);
            Structs.Vertex vertex1 = Structs.Vertex.newBuilder().setX(staringPosX).setY(staringPosY).build();
            Structs.Vertex vertex2 = Structs.Vertex.newBuilder().setX(staringPosX+increaseX).setY(staringPosY).build();
            Structs.Vertex vertex3 = Structs.Vertex.newBuilder().setX(staringPosX+increaseX).setY(staringPosY+increaseY).build();
            Structs.Vertex vertex4 = Structs.Vertex.newBuilder().setX(staringPosX).setY(staringPosY+increaseY).build();

            islandShape = new Rectangle(vertex1,vertex2,vertex3,vertex4);

        } else if (specification.getShapeType().equals(IslandShapeType.IRREGULAR)) {

            //IRREGULAR
            double noiseThreshold = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.9,0,1);

            islandShape = new Irregular(specification.getSeed(), noiseThreshold,CanvasUtils.getCenter(mesh),CanvasUtils.getMaxPoint(mesh));

        }

        return islandShape;

    }

    // Method to assign altitude
    private void assignAltitude(PolyMesh<Polygons> mesh, CLInterfaceIsland specification, IslandShape islandShape, long noiseEvaluationPosition, Structs.Vertex max, Structs.Vertex middle) {

        // Get list of polygons inside island shape
        ArrayList<Polygons> islandPoly = new ArrayList<>();

        for (Polygons currentPolygon : mesh) {

            Structs.Vertex centroid = currentPolygon.getCentroid();

            if (islandShape.isVertexInside(centroid))
                islandPoly.add(currentPolygon);

        }

        // Assign elevation to all polygons
        if (specification.getElevationType().equals(ElevationType.PLAINS)) {

            // ALTITUDE
            double baseAltitude = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.2,0,1);
            //FLUCTUATION
            double fluctuation = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.3,0,0.2);

            Plains.plainsAltitude(islandPoly,baseAltitude,fluctuation);

        } else if (specification.getElevationType().equals(ElevationType.MOUNTAINS)) {

            // MOUNTAINS
            double maxRadiusCanvasCenter = getMaxIslandDistance(mesh,islandShape,middle);
            int numOfMountains = (int) GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.4,0,20);
            double topAltitude = 1.0;
            double botAltitude = 0.0;
            double slopeRadius = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.5,maxRadiusCanvasCenter*0.5,maxRadiusCanvasCenter);

            Mountains.mountainAltitude(islandPoly,numOfMountains,topAltitude,botAltitude,slopeRadius);

        } else if (specification.getElevationType().equals(ElevationType.VOLCANO)) {

            // VOLCANO'S
            double topAltitude = 1.0;
            double botAltitude = 0.0;
            double maxRadiusCanvasCenter = getMaxIslandDistance(mesh,islandShape,middle);
            double area = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.6,0,maxRadiusCanvasCenter);
            double width_vol = CanvasUtils.getMaxPoint(mesh).getX() - GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.7,0,maxRadiusCanvasCenter);
            double height_vol = CanvasUtils.getMaxPoint(mesh).getY() - GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.8,0,maxRadiusCanvasCenter);

            Volcano.volcanoAltitude(islandPoly, CanvasUtils.getCenter(mesh),topAltitude, botAltitude, height_vol, width_vol, area);

        }
    }

    private SoilProfile getSoilProfile(CLInterfaceIsland specification) {

        // Get Soil Type
        String soilType = specification.getSoilType();

        SoilProfile profile = null;
        double clayContent;
        double sandContent;
        double loamContent;
        double absorptionRate = 1;

        switch(soilType.toLowerCase()){
                case "clay":
                    clayContent = Math.random() * 0.5 + 0.5;
                    sandContent = Math.random() * 0.3;
                    loamContent = Math.random() * 0.3;
                    absorptionRate = new Clay(clayContent,sandContent,loamContent,absorptionRate).calculateAbsorptionRate(clayContent,sandContent,loamContent);
                    profile = new Clay(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "sand":
                    clayContent = Math.random() * 0.3;
                    sandContent = Math.random() * 0.5 + 0.5;
                    loamContent = Math.random() * 0.3;
                    absorptionRate = new Sand(clayContent,sandContent,loamContent,absorptionRate).calculateAbsorptionRate(clayContent,sandContent,loamContent);
                    profile = new Sand(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "loam":
                    clayContent = Math.random() * 0.3;
                    sandContent = Math.random() * 0.3;
                    loamContent = Math.random() * 0.5 + 0.5;
                    absorptionRate = new Loam(clayContent,sandContent,loamContent,absorptionRate).calculateAbsorptionRate(clayContent,sandContent,loamContent);
                    profile = new Loam(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "special":
                    clayContent = Math.random() * 0.5 + 0.5;
                    sandContent = Math.random() * 0.5 + 0.5;
                    loamContent = Math.random() * 0.5 + 0.5;
                    absorptionRate = new Special(clayContent,sandContent,loamContent,absorptionRate).calculateAbsorptionRate(clayContent,sandContent,loamContent);
                    profile = new Special(clayContent,sandContent,loamContent,absorptionRate);
                    break;
            }

        return profile;

    }

    // Method to get radius of island
    private double getMaxIslandDistance(PolyMesh<Polygons> mesh, IslandShape islandShape, Structs.Vertex centerOfIsland) {

        double longestDistance = 0.0;

        // Loop through all polygons in mesh
        for (Polygons curPoly : mesh) {

            Structs.Vertex centroid = curPoly.getCentroid();

            // Check if the polygons' centroid exists within the island
            if (islandShape.isVertexInside(centroid)) {

                // Calculate the distance from the center
                double distanceFromCenter = Math.sqrt(Math.pow(centroid.getX()-centerOfIsland.getX(),2) + Math.pow(centroid.getY()-centerOfIsland.getY(),2));

                // If it's longer than what's currently stored, set new longest distance
                if (distanceFromCenter > longestDistance) {
                    longestDistance = distanceFromCenter;
                }

            }

        }

        return longestDistance;

    }

    // Method to extract properties from vertices
    private static double extractProperties(java.util.List<Structs.Property> properties, String property){

        String val = "0";
        for(Structs.Property p: properties) {
            if (p.getKey().equals(property)) {
                val = p.getValue();
            }
        }

        return  Double.parseDouble(val);
    }

}
