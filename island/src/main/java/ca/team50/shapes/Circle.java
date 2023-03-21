package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class Circle implements IslandShape {

    private double radius;
    private Structs.Vertex center;

    public Circle(Structs.Vertex centerOfMesh, double radius) {
        this.radius = radius;
        this.center = centerOfMesh;
    }

    public boolean isVertexInside(Structs.Vertex vertexToCheck) {
        double distance = getDistanceToCenter(this.center,vertexToCheck);
        return (distance<=radius);
    }

    private static double getDistanceToCenter(Structs.Vertex center, Structs.Vertex pointToCheck) {
        double distance = Math.sqrt(Math.pow((pointToCheck.getX()-center.getX()),2) + Math.pow((pointToCheck.getY()-center.getY()),2));
        return distance;
    }

}
