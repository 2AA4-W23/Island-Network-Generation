package ca.team50.generation;

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

        long noiseEvaluationPosition = 1234;
        Structs.Vertex max = CanvasUtils.getMaxPoint(mesh);
        Structs.Vertex middle = CanvasUtils.getCenter(mesh);

        // CIRCLE
        double maxRadiusCanvasCenter = Math.sqrt(Math.pow(max.getX()-middle.getX(),2) + Math.pow(max.getY()-middle.getY(),2));
        double circleRadiusNoise = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.1,0,maxRadiusCanvasCenter);
        double radius = circleRadiusNoise;

        // ELIPSE
        double maxRadiusX = max.getX() - middle.getX();
        double maxRadiusY = max.getY() - middle.getY();
        double width = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.2,0,maxRadiusX);
        double height = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.3,0,maxRadiusY);;
        double rotation = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.4,-6.28,6.28);

        // RECTANGLE
        double staringPosX = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.5,0,maxRadiusX);
        double staringPosY = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.6,0,maxRadiusY);
        double increaseX = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.7,1,maxRadiusX);
        double increaseY = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.8,1,maxRadiusY);
        Structs.Vertex vertex1 = Structs.Vertex.newBuilder().setX(staringPosX).setY(staringPosY).build();
        Structs.Vertex vertex2 = Structs.Vertex.newBuilder().setX(staringPosX+increaseX).setY(staringPosY).build();
        Structs.Vertex vertex3 = Structs.Vertex.newBuilder().setX(staringPosX+increaseX).setY(staringPosY+increaseY).build();
        Structs.Vertex vertex4 = Structs.Vertex.newBuilder().setX(staringPosX).setY(staringPosY+increaseY).build();

        //IRREGULAR
        double noiseThreshold = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*0.9,-1,1);

        // LAKE GENERATION
        // MAX RADIUS
        double maxRadius = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition,0,maxRadiusCanvasCenter);
        // THRESHOLD ALTITUDE
        double altitude = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.1,0,1);

        //RIVER THRESHOLD
        double rivAltitude = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.15,0,1);

        // ELEVATION
        // ALTITUDE
        double baseAltitude = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.2,0,1);
        //FLUCTUATION
        double fluctuation = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.3,0,40);

        // MOUNTAINS
        int numOfMountains = (int) GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.4,0,20);
        double topAltitude = 1.0;
        double botAltitude = 0.0;
        double slopeRadius = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.5,0,maxRadiusCanvasCenter);

        // VOLCANO'S
        double area = GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.6,0,maxRadiusCanvasCenter);
        double width_vol = CanvasUtils.getMaxPoint(mesh).getX() - GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.7,0,maxRadiusCanvasCenter);
        double height_vol = CanvasUtils.getMaxPoint(mesh).getY() - GenerationUtils.worleyNoise1DScaled(specification.getSeed(), noiseEvaluationPosition*1.8,0,maxRadiusCanvasCenter);

        // Setup
        for (Polygons currentPolygon : mesh) {
            currentPolygon.cleanProperties();
        }

        // Get island shape
        IslandShape islandShape = getShape(mesh,radius,height,width,vertex1,vertex2,vertex3,vertex4,noiseThreshold);

        // Get list of polygons
        ArrayList<Polygons> islandPoly = new ArrayList<>();

        for (Polygons currentPolygon : mesh) {

            Structs.Vertex centroid = currentPolygon.getCentroid();

            if (islandShape.isVertexInside(centroid))
                islandPoly.add(currentPolygon);

        }

        // Assign altitude
        assignAltitude(mesh,islandPoly,baseAltitude,fluctuation,numOfMountains,topAltitude,botAltitude,slopeRadius,height_vol,width_vol,area);

        // Get soil profile
        SoilProfile profile = getSoilProfile(specification);

        // Aquifer generation
        AquiferGenerator aquiferGenerator = new AquiferGenerator();
        AquiferGenerator aquifer = aquiferGenerator.generateAquifers(mesh,islandShape);

        // Lake generation
        LakeGenerator lakeGenerator = new LakeGenerator(mesh,islandShape,specification.getNumLakes(),maxRadius,altitude,specification.getSeed());

        TileType lakeTile = new LakeTile();
        TileType oceanTile = new OceanTile();

        // Generate Rivers
        RiverCentroidsGenerator rivers = new RiverCentroidsGenerator(mesh, specification.getNumRivers(), rivAltitude);

        // Assign colours to polygons
        for (Polygons curPoly : mesh) {

            Structs.Vertex centroid = curPoly.getCentroid();

            // Check if polygon exists within island
            if (islandShape.isVertexInside(centroid)) {

                // Compute humidity
                profile.computeRemainingWater(curPoly,lakeGenerator,aquiferGenerator);

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

        }


    }

    // Method to generate shape
    private IslandShape getShape(PolyMesh<Polygons> mesh, double cirRadius, double elHeight, double elWidth, Structs.Vertex vertex1, Structs.Vertex vertex2, Structs.Vertex vertex3, Structs.Vertex vertex4, double irrNoiseThreshold) {

        IslandShape islandShape = null;

        // Get island shape
        if (specification.getShapeType().equals(IslandShapeType.CIRCLE)) {

            islandShape = new Circle(CanvasUtils.getCenter(mesh),cirRadius);

        } else if (specification.getShapeType().equals(IslandShapeType.ELIPSE)) {

            islandShape = new Elipse(CanvasUtils.getCenter(mesh),elHeight,elWidth,0.3);

        } else if (specification.getShapeType().equals(IslandShapeType.RECTANGLE)) {

            islandShape = new Rectangle(vertex1,vertex2,vertex3,vertex4);

        } else if (specification.getShapeType().equals(IslandShapeType.IRREGULAR)) {

            islandShape = new Irregular(specification.getSeed(),irrNoiseThreshold,CanvasUtils.getCenter(mesh),CanvasUtils.getMaxPoint(mesh));

        }

        return islandShape;

    }

    private void assignAltitude(PolyMesh<Polygons> mesh, List<Polygons> islandPoly, double plainsBaseAltitude, double plainsFluctuation, int numOfMountains, double mountTopAltitude, double mountBotAltitude, double mountSlope, double height_vol, double width_vol, double area) {
        // Assign elevation to all polygons
        if (specification.getElevationType().equals(ElevationType.PLAINS)) {

            Plains.plainsAltitude(islandPoly,plainsBaseAltitude,plainsFluctuation);

        } else if (specification.getElevationType().equals(ElevationType.MOUNTAINS)) {

            Mountains.mountainAltitude(islandPoly,numOfMountains,mountTopAltitude,mountBotAltitude,mountSlope);

        } else if (specification.getElevationType().equals(ElevationType.VOLCANO)) {

            Volcano.volcanoAltitude(islandPoly, CanvasUtils.getCenter(mesh), mountTopAltitude, mountBotAltitude, height_vol, width_vol, area);

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
                    profile = new Clay(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "sand":
                    clayContent = Math.random() * 0.3;
                    sandContent = Math.random() * 0.5 + 0.5;
                    loamContent = Math.random() * 0.3;
                    profile = new Sand(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "loam":
                    clayContent = Math.random() * 0.3;
                    sandContent = Math.random() * 0.3;
                    loamContent = Math.random() * 0.5 + 0.5;
                    profile = new Loam(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "special":
                    clayContent = Math.random() * 0.5 + 0.5;
                    sandContent = Math.random() * 0.5 + 0.5;
                    loamContent = Math.random() * 0.5 + 0.5;
                    profile = new Special(clayContent,sandContent,loamContent,absorptionRate);
                    break;
            }

        return profile;

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
