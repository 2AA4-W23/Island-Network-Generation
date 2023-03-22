package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

// This class describes an abstract circle
public class Circle implements IslandShape {

    private Geometry circle;

    /**
     * Generate a circle island shape
     * @param centerOfMesh center of the circle on the canvas
     * @param radius the radius of the circle
     * @return a Circle object
     */
    public Circle(Structs.Vertex centerOfMesh, double radius) {
        // Create a circle
        this.circle = generateShape(centerOfMesh,radius,64);
    }

    /**
     * Test if any given point exists within the circle shape
     * @param vertexToCheck the position (as a vertex) to check
     * @return a boolean value, true if the point exists within the shape, false otherwise
     */
    public boolean isVertexInside(Structs.Vertex vertexToCheck) {
        // Create a geometry factory
        GeometryFactory geoFactory = new GeometryFactory();

        // Convert the vertex to check into a Point that JTS can understand since the shape is in JTS format
        Point coordToCheck = geoFactory.createPoint(new Coordinate(vertexToCheck.getX(),vertexToCheck.getY()));

        // Check if the point is within the Geometry using a built in JTS method
        return (this.circle.contains(coordToCheck));
    }

}
