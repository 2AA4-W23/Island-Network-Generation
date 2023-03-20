package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.awt.*;

public class Lagoon {

    private static final double oceanRadius = 100;
    private static final double lagoonRadius = 40;

    private static final int[] oceanColor = {21,34,138};
    private static final int[] lagoonColor = {99,144,230};
    private static final int[] landColor = {172,189,75};
    private static final int[] beachColor = {236,240,132};

    public static void generateIsland(PolyMesh<Polygons> mesh) {

        // Find center of canvas, the center will act as the center of the lagoon island
        Vertex center = CanvasUtils.getCenter(mesh);

        // Loop through all polygons
        // Get their centroids and apply appropriate colour based on where they are located relative to center and specified radius
        for (Polygons currentPolygon : mesh) {

            Vertex centroid = currentPolygon.getCentroid();

            // Get distance to center
            double distance = getDistanceToCenter(center,centroid);

            // Check where the polygon is located and colour it accordingly
            if (distance <= lagoonRadius) {
                currentPolygon.unifyColor(lagoonColor);
            } else if (distance > lagoonRadius || distance <=oceanRadius) {
                currentPolygon.unifyColor(landColor);
            } else if (distance > oceanRadius) {
                currentPolygon.unifyColor(oceanColor);
            }

        }


    }

    private static double getDistanceToCenter(Vertex center, Vertex pointToCheck) {
        double distance = Math.sqrt(Math.pow((pointToCheck.getX()-center.getX()),2) + Math.pow((pointToCheck.getY()-center.getY()),2));
        return distance;
    }

}
