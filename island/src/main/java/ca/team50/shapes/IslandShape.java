package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.util.GeometricShapeFactory;

import java.util.List;

public interface IslandShape {

    // Creates an abstract shape
    default Geometry generateShape(List<Structs.Vertex> vertexList) {

        GeometryFactory geoFactory = new GeometryFactory();

        // First extract data from vertex list
        CoordinateList coordinateList = new CoordinateList();

        for (Structs.Vertex currentVertex : vertexList) {
            coordinateList.add(new Coordinate(currentVertex.getX(),currentVertex.getY()));
        }

        // Close the loop (shape loop) at the end by adding the starting point again
        coordinateList.add(new Coordinate(vertexList.get(0).getX(),vertexList.get(0).getY()));

        // Create geometry
        return geoFactory.createPolygon(coordinateList.toCoordinateArray());
    }

    // Creates a circle instead of an abstract shape
     default Geometry generateShape(Structs.Vertex center, double radius, int precision) {

        // Create shape factory
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();

        // Set number of points around the circle
        shapeFactory.setNumPoints(precision);

        // Set the location of the centre
        shapeFactory.setCentre(new Coordinate(center.getX(),center.getY()));

        // Set the size of the circle
        shapeFactory.setSize(radius*2);

        // Create the circle
        return shapeFactory.createCircle();
    }

    // Create an elipse instead of an abstract shape
    default Geometry generateShape(Structs.Vertex center, double height, double width, int precision) {

        // Create shape factory
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();

        // Set number of points around the circle
        shapeFactory.setNumPoints(precision);

        shapeFactory.setHeight(height);
        shapeFactory.setWidth(width);

        // Set the location of the centre
        shapeFactory.setCentre(new Coordinate(center.getX(),center.getY()));

        // Create the elipse
        return shapeFactory.createEllipse();
    }

    // Method to detect if any given point (vertex) is inside the shape may vary
    boolean isVertexInside(Structs.Vertex vertexToCheck);

}
