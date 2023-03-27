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


public class NormalGenerator implements IslandGenerable {

    private CLInterfaceIsland specification;

    public NormalGenerator(CLInterfaceIsland islandSpecification) {
        this.specification = islandSpecification;
    }

    // Main island generation
    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

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

        // Temp shape
        IslandShape islandShape = new Circle(CanvasUtils.getCenter(mesh),radius);

        // Get island shape
        if (specification.getShapeType().equals(IslandShapeType.CIRCLE)) {

            islandShape = new Circle(CanvasUtils.getCenter(mesh),radius);
            
        } else if (specification.getShapeType().equals(IslandShapeType.ELIPSE)) {

            islandShape = new Elipse(CanvasUtils.getCenter(mesh),height,width,0.3);
            
        } else if (specification.getShapeType().equals(IslandShapeType.RECTANGLE)) {

            islandShape = new Rectangle(vertex1,vertex2,vertex3,vertex4);
            
        } else if (specification.getShapeType().equals(IslandShapeType.IRREGULAR)) {

            islandShape = new Irregular(specification.getSeed(),noiseThreshold,CanvasUtils.getCenter(mesh),CanvasUtils.getMaxPoint(mesh));

        }

        // Get list of polygons
        ArrayList<Polygons> islandPoly = new ArrayList<>();

        for (Polygons currentPolygon : mesh) {

            Structs.Vertex centroid = currentPolygon.getCentroid();

            if (islandShape.isVertexInside(centroid))
                islandPoly.add(currentPolygon);

        }

        System.out.println(islandPoly.size());

        // Assign elevation to all polygons
        if (specification.getElevationType().equals(ElevationType.PLAINS)) {

            Plains.plainsAltitude(islandPoly,baseAltitude,fluctuation);

        } else if (specification.getElevationType().equals(ElevationType.MOUNTAINS)) {

            Mountains.mountainAltitude(islandPoly,numOfMountains,topAltitude,botAltitude,slopeRadius);

        } else if (specification.getElevationType().equals(ElevationType.VOLCANO)) {

            Volcano.volcanoAltitude(islandPoly, CanvasUtils.getCenter(mesh), topAltitude, botAltitude, height_vol, width_vol, area);
            
        }

        // Get Soil Type
        String soilType = specification.getSoilType();
        if (soilType != null){
            double clayContent;
            double sandContent;
            double loamContent;
            double absorptionRate = 0;

            switch(soilType.toLowerCase()){
                case "clay":
                    clayContent = Math.random() * 0.5 + 0.5;
                    sandContent = Math.random() * 0.3;
                    loamContent = Math.random() * 0.3;
                    SoilProfile clayProfile = new Clay(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "sand":
                    clayContent = Math.random() * 0.3;
                    sandContent = Math.random() * 0.5 + 0.5;
                    loamContent = Math.random() * 0.3;
                    SoilProfile sandProfile = new Sand(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "loam":
                    clayContent = Math.random() * 0.3;
                    sandContent = Math.random() * 0.3;
                    loamContent = Math.random() * 0.5 + 0.5;
                    SoilProfile loamProfile = new Loam(clayContent,sandContent,loamContent,absorptionRate);
                    break;
                case "special":
                    clayContent = Math.random() * 0.5 + 0.5;
                    sandContent = Math.random() * 0.5 + 0.5;
                    loamContent = Math.random() * 0.5 + 0.5;
                    SoilProfile specialProfile = new Special(clayContent,sandContent,loamContent,absorptionRate);
                    break;
            }
        }


        // Aquifer generation
        AquiferGenerator aquiferGenerator = new AquiferGenerator();
        AquiferGenerator aquifer = aquiferGenerator.generateAquifers(mesh,islandShape);

        // Lake generation
        LakeGenerator lakeGenerator = new LakeGenerator(mesh,islandShape,specification.getNumLakes(),maxRadius,altitude,specification.getSeed());
        TileType lakeTile = new LakeTile();
        TileType oceanTile = new OceanTile();

        // Assign colours to polygons
        for (Polygons curPoly : mesh) {

            Structs.Vertex centroid = curPoly.getCentroid();

            // Check if polygon exists within island
            if (islandShape.isVertexInside(centroid)) {

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

        // Generate Rivers
        RiverCentroidsGenerator rivers = new RiverCentroidsGenerator(mesh, specification.getNumRivers(), rivAltitude);


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
