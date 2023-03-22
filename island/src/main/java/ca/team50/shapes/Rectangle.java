package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

public class Rectangle implements IslandShape {

    private Geometry rectangle;


    // Note, list of vertices only needs 3 vertex! The first one will be repeated automatically to form the boundary of the shape!
    /**
     * Generate a square island shape
     * @param vertex1 the starting point of the square (this vertex is automatically repeated)
     * @param vertex2 the next point of the square (from vertex1)
     * @param vertex3 the next point of the square (from vertex2)
     * @return a Square object
     */
    public Rectangle(Structs.Vertex vertex1, Structs.Vertex vertex2, Structs.Vertex vertex3) {

        // Convert to list
        ArrayList<Structs.Vertex> list = new ArrayList<>();
        list.add(vertex1);
        list.add(vertex2);
        list.add(vertex3);

        this.rectangle = generateShape(list);
    }

    /**
     * Test if any given point exists within the square shape
     * @param vertexToCheck the position (as a vertex) to check
     * @return a boolean value, true if the point exists within the shape, false otherwise
     */
    @Override
    public boolean isVertexInside(Structs.Vertex vertexToCheck) {
        // Create a geometry factory
        GeometryFactory geoFactory = new GeometryFactory();

        // Convert the vertex to check into a Point that JTS can understand since the shape is in JTS format
        Point coordToCheck = geoFactory.createPoint(new Coordinate(vertexToCheck.getX(),vertexToCheck.getY()));

        // Check if the point is within the Geometry using a built in JTS method
        return (this.rectangle.contains(coordToCheck));
    }
}
