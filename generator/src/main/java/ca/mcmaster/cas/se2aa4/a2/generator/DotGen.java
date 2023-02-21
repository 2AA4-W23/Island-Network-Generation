package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.*;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.awt.Color;

public class DotGen {

    private final static int width = 500;
    private final static int height = 500;
    private final static int square_size = 20;

    public static void polygonGenerate() {
        List<Vertex> vertices = new ArrayList<>();
        // Create all the vertices
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y).build());
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y+square_size).build());
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y+square_size).build());
            }
        }

            // Distribute colors randomly. Vertices are immutable, need to enrich them
        ArrayList<Vertex> verticesWithColors = new ArrayList<Vertex>();
        Random bag = new Random();
        for(Vertex v: vertices){
            int red = bag.nextInt(255);
            int green = bag.nextInt(255);
            int blue = bag.nextInt(255);
            String colorCode = red + "," + green + "," + blue;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Vertex colored = Vertex.newBuilder(v).addProperties(color).build();
            verticesWithColors.add(colored);
        }

        ArrayList<Vertex> polygonVertices = new ArrayList<Vertex>();
        ArrayList<Polygons> polygons = new ArrayList<Polygons>();

        for (Vertex v1: verticesWithColors){
            for (Vertex v2: verticesWithColors){
                for (Vertex v3: verticesWithColors){
                    for (Vertex v4: verticesWithColors){
                        if ((v1.getY() == v2.getY() && v1.getX() == v2.getX() + square_size) && (v1.getX() == v3.getX() && v3.getY() == v1.getY() + square_size) && (v2.getX() == v4.getX() && v4.getY() == v2.getY() + square_size) && (v3.getY() == v4.getY() && v4.getX() == v3.getX() + square_size)){
                            polygonVertices.add(v1);
                            polygonVertices.add(v2);
                            polygonVertices.add(v3);
                            polygonVertices.add(v4);

                            polygons.add(new Polygons(polygonVertices)); 

                            polygonVertices.clear();
                        } 
                    }
                }
            }
        }

        polygonsArray.addAll(polygons);
        
    } 

}

