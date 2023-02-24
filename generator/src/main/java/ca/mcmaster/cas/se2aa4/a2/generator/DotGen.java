package ca.mcmaster.cas.se2aa4.a2.generator;

import java.util.*;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;


public class DotGen {

    private final static int width = 500;
    private final static int height = 500;
    private final static int square_size = 20;


    public static PolyMesh<Polygons> polygonGenerate() {
        List<Vertex> vertices = new ArrayList<>();

        // Create all the vertices
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {

                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build()); // X:0 Y:0
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y).build()); // X: 20 Y:0
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y+square_size).build()); // X:20 Y:20
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y+square_size).build()); // X: 0 Y:20
            }
        }

        //Distribute thickness randomly.
        ArrayList<Vertex> verticesWithThickness= new ArrayList<Vertex>();
        Random bag = new Random();
        for(Vertex v: vertices){
            Float vertexWidth = bag.nextFloat(5);
            String width = String.valueOf(vertexWidth);
            Property thickness = Property.newBuilder().setKey("thickness").setValue(width).build();
            v = v.toBuilder().addProperties(thickness).build();
            verticesWithThickness.add(v);
        }

        // Distribute colors randomly. Vertices are immutable, need to enrich them
        ArrayList<Vertex> verticesWithColors = new ArrayList<Vertex>();
        Random bag1 = new Random();
        for(Vertex v: verticesWithThickness) {
            int red = bag1.nextInt(255);
            int green = bag1.nextInt(255);
            int blue = bag1.nextInt(255);
            String colorCode = red + "," + green + "," + blue;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Vertex colored = Vertex.newBuilder(v).addProperties(color).build();
            verticesWithColors.add(colored);
        }


        List<Vertex> vertexSetOf4 = new ArrayList<>();
        List<Polygons> polygonsList = new ArrayList<>();

        for (Vertex currentVertex : verticesWithColors) {

            if (vertexSetOf4.size() < 4) {
                vertexSetOf4.add(currentVertex);
            } else {

                polygonsList.add(new Polygons(vertexSetOf4));
                vertexSetOf4.clear();
                vertexSetOf4.add(currentVertex);

            }

        }

        PolyMesh<Polygons> polygonMesh = new PolyMesh<Polygons>();

        for (Polygons currentPolygon : polygonsList) {
            polygonMesh.add(currentPolygon);
        }

        return polygonMesh;
    
    } 

}

