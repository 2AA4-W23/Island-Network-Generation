package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.*;

import java.util.List;

public interface IslandShape {

    public Polygon shape = null;

    default Polygon generateShape(List<Structs.Vertex> vertexList) {
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

    default boolean isVertexInside(Structs.Vertex vertexToCheck) {

        GeometryFactory geoFactory = new GeometryFactory();

        Point coordToCheck = geoFactory.createPoint(new Coordinate(vertexToCheck.getX(),vertexToCheck.getY()));

        return (this.shape.contains(coordToCheck));

    }

}
