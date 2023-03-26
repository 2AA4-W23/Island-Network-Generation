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
import ca.team50.specification.CLInterfaceIsland;
import ca.team50.water.LakeGenerator;
import java.util.ArrayList;


public class NormalGenerator implements IslandGenerable {

    private CLInterfaceIsland specification;

    public NormalGenerator(CLInterfaceIsland islandSpecification) {
        this.specification = islandSpecification;
    }


    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        // PLACEHOLDER VALUES

        // CIRCLE
        double radius = 800;

        // ELIPSE
        double width = 1200;
        double height = 1000;
        double rotation = 0.3;

        // RECTANGLE
        Structs.Vertex vertex1 = Structs.Vertex.newBuilder().setX(300).setY(300).build();
        Structs.Vertex vertex2 = Structs.Vertex.newBuilder().setX(500).setY(300).build();
        Structs.Vertex vertex3 = Structs.Vertex.newBuilder().setX(500).setY(500).build();
        Structs.Vertex vertex4 = Structs.Vertex.newBuilder().setX(300).setY(500).build();

        //IRREGULAR
        double noiseThreshold = -0.2;

        // LAKE GENERATION
        // MAX RADIUS
        int maxRadius = 80;

        // THRESHOLD ALTITUDE
        double altitude = 0.8;

        // ELEVATION
        // ALTITUDE
        double baseAltitude = 0.0;
        //FLUCTUATION
        double fluctuation = 5.0;

        // MOUNTAINS
        int numOfMountains = 10;
        double topAltitude = 1.0;
        double botAltitude = 0.0;
        double slopeRadius = 500;

        // VOLCANO'S
        double area = 150.0;
        double width_vol = CanvasUtils.getMaxPoint(mesh).getX();
        double height_vol = CanvasUtils.getMaxPoint(mesh).getY();

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

                if (specification.getBiomeType().equals(BiomeType.Tropical)) {
                    curPoly.unifyColor(TropicalUtils.getTileFormProperty(polygonAltitude).getTileColour());
                } else if (specification.getBiomeType().equals(BiomeType.Arctic)) {
                    curPoly.unifyColor(ArcticUtils.getTileFormProperty(polygonAltitude).getTileColour());
                } else if (specification.getBiomeType().equals(BiomeType.Deserts)) {
                    curPoly.unifyColor(DesertsUtils.getTileFormProperty(polygonAltitude).getTileColour());
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
