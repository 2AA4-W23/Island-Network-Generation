package ca.team50.generators;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.RandomGen;

import java.util.ArrayList;
import java.util.List;

public class Grid implements Generable {


    @Override
    public PolyMesh<Polygons> generate(int width, int height, int numOfPolygons) {
        // Calculate the square size of each polygon


        // square_size^2 * numOfPolygons = width*height
        // square_size = sqr(((width*height)/numOfPolygons))
        // Flooring the double with always round said value down thus square_size^2 * numOfPolygons will never exceed width*height
        int square_size = ((Double) Math.floor((Math.sqrt((width*height)/numOfPolygons)))).intValue();

        List<Structs.Vertex> vertices = new ArrayList<>();
        // Create all the vertices
        // Iterations will limit the number of vertex sets (4 per) to the total number of polygons specified
        int iterations = 0;
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height && iterations < numOfPolygons; y += square_size) {

                // Create the vertex with position, then apply random property values to it (random alpha, thickness and RGB color)
                vertices.add(RandomGen.alphaGen((RandomGen.colorGen(RandomGen.thicknessGen(Structs.Vertex.newBuilder().setX((double) x).setY((double) y).build()))))); // X:0 Y:0
                vertices.add(RandomGen.alphaGen((RandomGen.colorGen(RandomGen.thicknessGen(Structs.Vertex.newBuilder().setX((double) x+square_size).setY((double) y).build()))))); // X: 20 Y:0
                vertices.add(RandomGen.alphaGen((RandomGen.colorGen(RandomGen.thicknessGen(Structs.Vertex.newBuilder().setX((double) x+square_size).setY((double) y+square_size).build()))))); // X:20 Y:20
                vertices.add(RandomGen.alphaGen(RandomGen.colorGen(RandomGen.thicknessGen(Structs.Vertex.newBuilder().setX((double) x).setY((double) y+square_size).build())))); // X: 0 Y:20
                iterations++;

            }
        }


        List<Structs.Vertex> vertexSetOf4 = new ArrayList<>();
        List<Polygons> polygonsList = new ArrayList<>();

        // Since each polygon in the mesh is of groups of 4...
        // Iterate through all vertices and group them into 4's
        for (Structs.Vertex currentVertex : vertices) {

            if (vertexSetOf4.size() < 4) {
                vertexSetOf4.add(currentVertex);
            } else {

                // Create a polygon with the four vertices and add it to the polygon list
                polygonsList.add(new Polygons(vertexSetOf4));
                vertexSetOf4.clear();
                vertexSetOf4.add(currentVertex);

            }

            // Since loop will end once the last vertex is added, thus creating one more group of 4...
            // Create a new polygon with the last four vertices and add to the list before exiting the for loop
            if (vertices.get(vertices.size()-1) == currentVertex) {
                polygonsList.add(new Polygons(vertexSetOf4));
                vertexSetOf4.clear();
            }

        }

        // Create new PolyMesh and add all polygons to it
        PolyMesh<Polygons> polygonMesh = new PolyMesh<Polygons>();
        polygonMesh.addAll(polygonsList);

        return polygonMesh;
    }
}
