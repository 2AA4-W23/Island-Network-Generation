package ca.team50.generation;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.util.Arrays;

public class VoronoiGen {

    public static void genVoronoi(int canvasSizeX, int canvasSizeY, Point[] points) {

        // Create a new voronoi diagram builder
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        // Create the geometry factory use to create the output of .getDiagram()
        GeometryFactory geoFactory = new GeometryFactory();

        // Set the coordinates to develop polygons around
        voronoi.setSites(Arrays.asList(points));

        // Set space in which the diagram will be computed on (i.e. the canvas size)
        voronoi.setClipEnvelope(new Envelope(new Coordinate(0,0),new Coordinate(canvasSizeX,canvasSizeY)));

        // Compute the diagram, return as a collection of polygons
        Geometry polygonsCollection = voronoi.getDiagram(geoFactory);

    }

}
