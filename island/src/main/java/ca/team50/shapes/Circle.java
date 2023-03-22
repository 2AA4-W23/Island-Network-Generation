package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class Circle implements IslandShape {

    private Geometry circle;

    public Circle(Structs.Vertex centerOfMesh, double radius) {
        this.circle = generateShape(centerOfMesh,radius,64);
    }

    public boolean isVertexInside(Structs.Vertex vertexToCheck) {
        GeometryFactory geoFactory = new GeometryFactory();

        Point coordToCheck = geoFactory.createPoint(new Coordinate(vertexToCheck.getX(),vertexToCheck.getY()));

        return (this.circle.contains(coordToCheck));
    }

}
