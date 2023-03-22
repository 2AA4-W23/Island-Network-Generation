package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class Elipse implements IslandShape {

    public Geometry elipse;

    /**
     * Generate an elipse island shape
     * @param center center of the elipse on the canvas
     * @param height height of the elipse
     * @param width width of the elipse
     * @param rotation rotate the elipse around it's center
     * @return an elipse object
     */
    public Elipse(Structs.Vertex center, double height, double width, double rotation) {
        this.elipse = generateShape(center,height,width,rotation,64);
    }

    /**
     * Test if any given point exists within the elipse shape
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
        return (this.elipse.contains(coordToCheck));
    }
}
