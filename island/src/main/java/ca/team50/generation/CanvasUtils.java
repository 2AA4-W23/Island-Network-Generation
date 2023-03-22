package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.exceptions.FileReadException;

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

}
