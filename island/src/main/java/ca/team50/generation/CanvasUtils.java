package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.exceptions.FileReadException;
import ca.team50.shapes.IslandShape;

import java.awt.*;

public class CanvasUtils {

    /**
     * Get the center of the canvas in relation to polygons on canvas (i.e. not an exact center but more aligned to the polygons)
     * @param mesh a PolyMesh object
     * @return a Point object (awt) containing the x and y coordinates of the relative center
     */
    public static Vertex getCenter(PolyMesh<Polygons> mesh) {

        double maxX = 0;
        double maxY = 0;

        // Loop through all Polygons
        for (Polygons currentPolygon : mesh) {

            // Loop through each vertex in each Polygon
            for (Vertex currentVertex : currentPolygon.getVerticesList()) {

                // Check if the x or y position is greater than what is already stored
                double vertexX = currentVertex.getX();
                double vertexY = currentVertex.getY();

                if (vertexX > maxX) {
                    maxX = vertexX;
                }
                if (vertexY > maxY) {
                    maxY = vertexY;
                }
            }
        }

        // Divide x and y by 2 to get midpoint
        Vertex vertex = Vertex.newBuilder().setX((maxX/2)).setY(maxY/2).build();
        return vertex;

    }

    /**
     * Get the maximum point on the canvas in relation to polygons on canvas (i.e. not an exact center but more aligned to the polygons)
     * @param mesh a PolyMesh object
     * @return a Point object (awt) containing the x and y coordinates of the relative maximum
     */
    public static Vertex getMaxPoint(PolyMesh<Polygons> mesh) {
        Vertex mid = getCenter(mesh);
        return Vertex.newBuilder().setX(mid.getX()*2).setY(mid.getY()*2).build();
    }

    /**
     * Get the maximum distance between two polygon centroids on a given island
     * @param mesh a PolyMesh object
     * @param shape the given island shape
     * @return a double value containing the maximum distance any two polygons can be on the island from each other
     */
    public static double maxDistFromIslandCent(PolyMesh<Polygons> mesh, IslandShape shape) {

        double maxDistance = 0;

        for (Polygons curPoly : mesh) {

            for (Polygons testPoly : mesh) {

                if (testPoly != curPoly) {

                    Vertex curPolyCentroid = curPoly.getCentroid();
                    Vertex testPolyCentroid = testPoly.getCentroid();

                    if (shape.isVertexInside(curPolyCentroid) && shape.isVertexInside(testPolyCentroid)) {
                        double distance = Math.sqrt(Math.pow((curPolyCentroid.getX() - testPolyCentroid.getX()), 2) + Math.pow((curPolyCentroid.getY() - testPolyCentroid.getY()), 2));

                        if (distance > maxDistance) {

                            maxDistance = distance;

                        }

                    }

                }

            }

        }

        return maxDistance;

    }

}
