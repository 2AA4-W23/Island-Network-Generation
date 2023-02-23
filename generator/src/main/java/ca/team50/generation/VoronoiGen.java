package ca.team50.generation;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class VoronoiGen {

    /**
     * Generates a voronoi diagram given Point objects
     * @param canvasSizeX the x boundary for the canvas size
     * @param canvasSizeY the y boundary for the canvas size
     * @param points an arraylist of points to compute polygons for
     * @implNote It is recommended that both canvasSizeX and canvasSizeY are equal to or greater than the maximum position (coordinate) of a given Point object
     * @exception IllegalArgumentException if canvasSizeX or canvasSizeY are less than or equal to 0.
     * @return an Arraylist of Polygon (JTS) objects
     */
    public static ArrayList<Polygon> genVoronoi(int canvasSizeX, int canvasSizeY, ArrayList<Point> points) {

        // Error checking
        if (canvasSizeX <= 0) {
            throw new IllegalArgumentException("canvasSizeX is less than or equal to 0: " + canvasSizeX + ", make sure that it is greater than 0");
        } else if (canvasSizeY <= 0) {
            throw new IllegalArgumentException("canvasSizeY is less than or equal to 0: " + canvasSizeY + ", make sure that it is greater than 0");
        }

        // Create a new voronoi diagram builder
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        // Create the geometry factory use to create the output of .getDiagram()
        GeometryFactory geoFactory = new GeometryFactory();

        // Set the coordinates to develop polygons around
        voronoi.setSites(points);

        // Set space in which the diagram will be computed on (i.e. the canvas size)
        voronoi.setClipEnvelope(new Envelope(new Coordinate(0,0),new Coordinate(canvasSizeX,canvasSizeY)));

        // Compute the diagram, returns as a collection of polygons
        Geometry polygonsCollection = voronoi.getDiagram(geoFactory);

        // Since diagram returns a collection of geometry, need to parse through it all and get all Polygons it made
        // Create a new array list to return
        ArrayList<Polygon> returnList = new ArrayList<>();

        // Add all polygons
        for (int index = 0; index < polygonsCollection.getNumGeometries(); index++) {
            returnList.add((Polygon) polygonsCollection.getGeometryN(index));
        }

        return returnList;

    }

}
