package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class Elipse implements IslandShape {

    public Geometry elipse;

    public Elipse(Structs.Vertex center, double height, double width, double rotation) {
        this.elipse = generateShape(center,height,width,rotation,64);
    }

    @Override
    public boolean isVertexInside(Structs.Vertex vertexToCheck) {
        // Create a geometry factory
        GeometryFactory geoFactory = new GeometryFactory();

        // Convert the vertex to check into a Point that JTS can understand since the shape is in JTS format
        Point coordToCheck = geoFactory.createPoint(new Coordinate(vertexToCheck.getX(),vertexToCheck.getY()));

        // Check if the point is within the Geometry using a built in JTS method
        return (this.elipse.contains(coordToCheck));
    }
}
