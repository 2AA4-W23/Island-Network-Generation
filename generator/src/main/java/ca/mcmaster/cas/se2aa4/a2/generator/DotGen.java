package ca.mcmaster.cas.se2aa4.a2.generator;

import java.util.*;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.RandomGen;
import ca.team50.generation.VoronoiGen;
import ca.team50.translation.JtsTranslation;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;


public class DotGen {

    private final static int width = 500;
    private final static int height = 500;
    private final static int square_size = 20;


    public static PolyMesh<Polygons> polygonGenerate() {
        List<Vertex> vertices = new ArrayList<>();

        // Create all the vertices
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {

                // Create the vertex with position, then apply random property values to it (random thickness and RGB color)
                vertices.add(RandomGen.colorGen(RandomGen.thicknessGen(Vertex.newBuilder().setX((double) x).setY((double) y).build()))); // X:0 Y:0
                vertices.add(RandomGen.colorGen(RandomGen.thicknessGen(Vertex.newBuilder().setX((double) x+square_size).setY((double) y).build()))); // X: 20 Y:0
                vertices.add(RandomGen.colorGen(RandomGen.thicknessGen(Vertex.newBuilder().setX((double) x+square_size).setY((double) y+square_size).build()))); // X:20 Y:20
                vertices.add(RandomGen.colorGen(RandomGen.thicknessGen(Vertex.newBuilder().setX((double) x).setY((double) y+square_size).build()))); // X: 0 Y:20
            }
        }


        List<Vertex> vertexSetOf4 = new ArrayList<>();
        List<Polygons> polygonsList = new ArrayList<>();

        for (Vertex currentVertex : vertices) {

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

        // TESTING ------

        // To generate a Voronoi diagram, instantiate it as such
/*      VoronoiGen newGenTest = new VoronoiGen(500,500, RandomGen.genCoords(400,400,60));
        First two arguments indicate the size of the overall canvas, third argument is a list of coordinates to draw Polygons around
        .genCoords - First argument is maxX in which a coordinate can exist, second is maxY (same idea as maxX) and third is the number of coordinates to generate

        // Once you have an instance of a diagram you can relax it X number of iterations (the higher this number, the less pointy the mesh will look)
        // Note that once you relax, you must call getGeoCollection() again for the updated mesh if you previously used the method!
        newGenTest.relax(30);

        // Finally, the outputted diagram is in the form of a Geometry JTS object
        // This object contains a group of JTS Polygons. You can translate this object into a PolyMesh via the following command
        PolyMesh<Polygons> testMesh = JtsTranslation.convertToPolyMesh(newGenTest.getGeoCollection());
*/
        return polygonMesh;
    
    } 

}

