package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

// This class describes an abstract circle
public class Circle implements IslandShape {

    private Geometry circle;

    public Circle(Structs.Vertex centerOfMesh, double radius) {
        // Create a circle
        this.circle = generateShape(centerOfMesh,radius,64);
    }

    public boolean isVertexInside(Structs.Vertex vertexToCheck) {
        // Create a geometry factory
        GeometryFactory geoFactory = new GeometryFactory();

        // Convert the vertex to check into a Point that JTS can understand since the shape is in JTS format
        Point coordToCheck = geoFactory.createPoint(new Coordinate(vertexToCheck.getX(),vertexToCheck.getY()));

        // Check if the point is within the Geometry using a built in JTS method
        return (this.circle.contains(coordToCheck));
    }

}
