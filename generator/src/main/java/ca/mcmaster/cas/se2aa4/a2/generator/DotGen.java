package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import java.awt.Color;

public class DotGen {

    private final int width = 500;
    private final int height = 500;
    private final int square_size = 20;

    public Mesh generate() {
        Set<Vertex> vertices = new HashSet<>();
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
        Set<Vertex> verticesWithColors = new HashSet<>();
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

        Set <Segment> segments = new HashSet<>();
        for (Vertex v1: verticesWithColors){
            for (Vertex v2: verticesWithColors){

                //only draw segment if vertices are adjacent in either the same row or column
                if ((v1.getX() == v2.getX() &&  v1.getY() == (v2.getY()+square_size))||(v1.getX() == v2.getX() &&  v1.getY() == (v2.getY()-square_size))|| ((v1.getY() == v2.getY()) &&  v1.getX() == (v2.getX()+square_size))||(v1.getY() == v2.getY() &&  v1.getX() == (v2.getX()-square_size))) {

                    Color v1Color = extractColor(v1.getPropertiesList());
                    Color v2Color = extractColor(v2.getPropertiesList());

                    //calculate average color and set as segment color
                    int averageR = (v1Color.getRed() + v2Color.getRed())/2;
                    int averageG = (v1Color.getGreen() + v2Color.getGreen())/2;
                    int averageB = (v1Color.getBlue() + v2Color.getBlue())/2;
                
                    String segmentColor = averageR + "," + averageG + "," + averageB;
                    String startVertex = v1.getX() + "," + v1.getY();
                    String endVertex = v2.getX() + "," + v2.getY();

                    //create and add properties(color, start & end vertices) to each line segment
                    Property color = Property.newBuilder().setKey("rgb_color").setValue(segmentColor).build();
                    Property startPos = Property.newBuilder().setKey("start_vertex").setValue(startVertex ).build();
                    Property endPos = Property.newBuilder().setKey("end_vertex").setValue(endVertex).build();

                    Segment newS = Segment.newBuilder().addProperties(color).build();
                    newS = newS.toBuilder().addProperties(startPos).build();
                    newS = newS.toBuilder().addProperties(endPos).build();

                    segments.add(newS);
                }
            }
        }

        //generate mesh with colored vertices and line segments
        Mesh mesh = Mesh.newBuilder().addAllVertices(verticesWithColors).build();
        Mesh meshWithSegments = mesh.toBuilder().addAllSegments(segments).build();

        return meshWithSegments; 
    } 
    
    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }

}

