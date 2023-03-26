package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.Arctic.ArcticUtils;
import ca.team50.Tiles.BiomeType;
import ca.team50.Tiles.Deserts.DesertsUtils;
import ca.team50.Tiles.TileType;
import ca.team50.Tiles.Tropical.TropicalUtils;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
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


        // Get tiles to use
        ArrayList<TileType> tilesList = new ArrayList<>();

        // Temp shape
        IslandShape islandShape = new Circle(CanvasUtils.getCenter(mesh),radius);

        if (specification.getBiomeType().equals(BiomeType.Tropical)) {

            tilesList.addAll(TropicalUtils.getAllTiles());

        } else if (specification.getBiomeType().equals(BiomeType.Arctic)) {

            tilesList.addAll(ArcticUtils.getAllTiles());

        } else if (specification.getBiomeType().equals(BiomeType.Deserts)) {

            tilesList.addAll(DesertsUtils.getAllTiles());

        }

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


        // Lake generation
        LakeGenerator lakeGenerator = new LakeGenerator(mesh,islandShape,specification.getNumLakes(),maxRadius,altitude,specification.getSeed());





    }
}
