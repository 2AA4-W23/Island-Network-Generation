package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.Arctic.ArcticUtils;
import ca.team50.Tiles.BiomeType;
import ca.team50.Tiles.Deserts.DesertsUtils;
import ca.team50.Tiles.TileType;
import ca.team50.Tiles.Tropical.TropicalUtils;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.elevation.ElevationType;
import ca.team50.elevation.Mountains;
import ca.team50.elevation.Plains;
import ca.team50.elevation.Volcano;
import ca.team50.shapes.*;
import ca.team50.specification.CLInterface;
import ca.team50.specification.CLInterfaceIsland;
import ca.team50.water.LakeGenerator;

import java.io.File;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NormalGenerator implements IslandGenerable {

    private CLInterfaceIsland specification;

    public NormalGenerator(CLInterfaceIsland islandSpecification) {
        this.specification = islandSpecification;
    }


    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        // PLACEHOLDER VALUES

        // CIRCLE
        double radius = 100;

        // ELIPSE
        double width = 500;
        double height = 800;
        double rotation = 0.3;

        // RECTANGLE
        Structs.Vertex vertex1 = Structs.Vertex.newBuilder().setX(300).setY(300).build();
        Structs.Vertex vertex2 = Structs.Vertex.newBuilder().setX(500).setY(300).build();
        Structs.Vertex vertex3 = Structs.Vertex.newBuilder().setX(500).setY(500).build();
        Structs.Vertex vertex4 = Structs.Vertex.newBuilder().setX(300).setY(500).build();

        //IRREGULAR
        double noiseThreshold = 0.2;

        // LAKE GENERATION
        // MAX RADIUS
        int maxRadius = 80;

        // THRESHOLD ALTITUDE
        double altitude = 0.5;

        // ELEVATION
        // ALTITUDE
        double baseAltitude = 0.0;
        //FLUCTUATION
        double fluctuation = 0.3;

        // MOUNTAINS
        int numOfMountains = 2;
        double topAltitude = 1.0;
        double botAltitude = 0.0;
        double numOf = 6.0;
        double slopeRadius = 200;

        // VOLCANO'S
        double area = 25.0;
        double width_vol = 200;
        double height_vol = 300;

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

        ArrayList<Polygons> islandPoly = new ArrayList<>();

        for (Polygons currentPolygon : mesh) {

            Structs.Vertex centroid = currentPolygon.getCentroid();

            if (islandShape.isVertexInside(centroid))
                islandPoly.add(currentPolygon);

        }

        // Assign elevation
        if (specification.getElevationType().equals(ElevationType.PLAINS)) {

            Plains.plainsAltitude(islandPoly,baseAltitude,fluctuation);

        } else if (specification.getElevationType().equals(ElevationType.MOUNTAINS)) {

            Mountains.mountainAltitude(islandPoly,numOfMountains,topAltitude,botAltitude,slopeRadius);

        } else if (specification.getElevationType().equals(ElevationType.VOLCANO)) {

            Volcano.volcanoAltitude(islandPoly, CanvasUtils.getCenter(mesh), topAltitude, botAltitude, height_vol, width_vol, area);
            
        }

        // Lake generation
        LakeGenerator lakeGenerator = new LakeGenerator(mesh,islandShape,specification.getNumLakes(),maxRadius,altitude,specification.getSeed());


    }
}
