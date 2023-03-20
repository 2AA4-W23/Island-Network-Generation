package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.awt.*;

public class Lagoon {

    private static final double oceanRadius = 350;
    private static final double lagoonRadius = 200;

    private static final int[] oceanColor = {21,34,138};
    private static final int[] lagoonColor = {99,144,230};
    private static final int[] landColor = {172,189,75};
    private static final int[] beachColor = {255,238,0};

    public static void generateIsland(PolyMesh<Polygons> mesh) {

        // Find center of canvas, the center will act as the center of the lagoon island
        Vertex center = CanvasUtils.getCenter(mesh);
        System.out.println("Center: " + center.getX() + ":" + center.getY());

        // Loop through all polygons
        // Get their centroids and apply appropriate colour based on where they are located relative to center and specified radius
        for (Polygons currentPolygon : mesh) {

            // Clean properties (i.e. set defaults)
            currentPolygon.cleanProperties();

            Vertex centroid = currentPolygon.getCentroid();

            // Get distance to center
            double distance = getDistanceToCenter(center,centroid);
            System.out.println("Distance: " + distance);

            // Check where the polygon is located and colour it accordingly
            if (distance <= lagoonRadius) {
                currentPolygon.unifyColor(lagoonColor);
            } else if (distance > lagoonRadius && distance <=oceanRadius) {
                currentPolygon.unifyColor(landColor);
            } else if (distance > oceanRadius) {
                currentPolygon.unifyColor(oceanColor);
            }

        }

        // For beach colouring, check if the polygon is a neighbour to some water and check if it exists as a land piece
        for (int index = 0; index < mesh.size(); index++) {

           for (int index1 = 0; index1 < mesh.size(); index1++) {

               if (index != index1 && mesh.isNeighbor(index,index1)) {

                   Polygons polygon1 = mesh.get(index);
                   Polygons polygon2 = mesh.get(index1);

                   Vertex centroid1 = polygon1.getCentroid();
                   Vertex centroid2 = polygon2.getCentroid();

                   double distance1 = getDistanceToCenter(center,centroid1);
                   double distance2 = getDistanceToCenter(center,centroid2);

                   // Check if polygon 1 is classified as water and polygon 2 is classified as land
                   if ((distance1 <= lagoonRadius || distance1 > oceanRadius) && (distance2 > lagoonRadius && distance2 <= oceanRadius)) {
                       // Then polygon 2 should be of beach type
                       polygon2.unifyColor(beachColor);

                       // Check other way around
                   } else if ((distance2 <= lagoonRadius || distance2 > oceanRadius) && (distance1 > lagoonRadius && distance1 <= oceanRadius)) {
                       polygon1.unifyColor(beachColor);
                   }


               }

           }
        }


    }

    private static double getDistanceToCenter(Vertex center, Vertex pointToCheck) {
        double distance = Math.sqrt(Math.pow((pointToCheck.getX()-center.getX()),2) + Math.pow((pointToCheck.getY()-center.getY()),2));
        return distance;
    }

}
