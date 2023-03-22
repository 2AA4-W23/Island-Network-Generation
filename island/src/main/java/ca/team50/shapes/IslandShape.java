package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.util.GeometricShapeFactory;

import java.util.List;

public interface IslandShape {

    // Creates an abstract shape
    default Geometry generateShape(List<Structs.Vertex> vertexList) {
        // Create the geometry factory use to create the output of .getDiagram()
        GeometryFactory geoFactory = new GeometryFactory();

        // First extract data from Polygons instance
        CoordinateList coordinateList = new CoordinateList();

        for (Structs.Vertex currentVertex : vertexList) {
            coordinateList.add(new Coordinate(currentVertex.getX(),currentVertex.getY()));
        }

        // Close the loop at the end by adding the starting point again
        coordinateList.add(new Coordinate(vertexList.get(0).getX(),vertexList.get(0).getY()));

        return geoFactory.createPolygon(coordinateList.toCoordinateArray());
    }

    // Creates a circle instead of an abstract shape
     default Geometry generateShape(Structs.Vertex center, double radius, int precision) {
        // Create the geometry factory use to create the output of .getDiagram()
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();

        shapeFactory.setNumPoints(precision);

        shapeFactory.setCentre(new Coordinate(center.getX(),center.getY()));

        shapeFactory.setSize(radius*2);

        return shapeFactory.createCircle();
    }

    boolean isVertexInside(Structs.Vertex vertexToCheck);

}
