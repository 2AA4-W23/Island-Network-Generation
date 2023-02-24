package ca.team50.generation;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.util.ArrayList;
import java.util.List;

public class VoronoiGen {

    // Variable declarations
    private int canvasSizeX;
    private int canvasSizeY;
    private Geometry geoCollection;
    private ArrayList<Coordinate> coordinates;

    /**
     * Generates a voronoi diagram given Point objects (JTS)
     * @param canvasSizeX the x boundary for the canvas size
     * @param canvasSizeY the y boundary for the canvas size
     * @param coordinates an arraylist of coordinates to compute polygons for
     * @implNote It is recommended that both canvasSizeX and canvasSizeY are equal to or greater than the maximum position (coordinate) of a given Point object
     * @exception IllegalArgumentException if canvasSizeX or canvasSizeY are less than or equal to 0.
     * @return a VoronoiGen object containing a Geometry object (JTS) with a collection of Polygons (JTS)
     */
    public VoronoiGen(int canvasSizeX, int canvasSizeY, ArrayList<Coordinate> coordinates) {

        // Error checking
        if (canvasSizeX <= 0) {
            throw new IllegalArgumentException("canvasSizeX is less than or equal to 0: " + canvasSizeX + ", make sure that it is greater than 0");
        } else if (canvasSizeY <= 0) {
            throw new IllegalArgumentException("canvasSizeY is less than or equal to 0: " + canvasSizeY + ", make sure that it is greater than 0");
        }

        // Set variables
        this.canvasSizeX = canvasSizeX;
        this.canvasSizeY = canvasSizeY;
        this.coordinates = coordinates;

        // Compute diagram
        generateDiagram();

    }

    /**
     * Relaxes the current voronoi diagram
     * @param iterations perform relaxation X times
     * @exception IllegalArgumentException if iterations is less than 0
     */
    public void relax(int iterations) {

        // Error checking
        if (iterations < 0) {
            throw new IllegalArgumentException("Iterations cannot be less than 0 (" + iterations + ")");
        }

        // Relax X amount of times
        for (int currentIteration = 1; currentIteration <= iterations; currentIteration++) {

            List<Polygon> polygonList = new ArrayList<>();

            // Reset points list
            this.coordinates.clear();

            // Get all Polygons from Geometry collection
            for (int index = 0; index < this.geoCollection.getNumGeometries(); index++) {
                polygonList.add((Polygon) geoCollection.getGeometryN(index));
            }

            // Loop through each Polygon and get the centroid
            for (Polygon currentPolygon : polygonList) {

                // Then add the centroid to the points Arraylist
                this.coordinates.add(currentPolygon.getCentroid().getCoordinate());

            }

            // Recompute diagram
            generateDiagram();
        }

    }

    // Method to compute the voronoi diagram
    private void generateDiagram() {
        // Create a new voronoi diagram builder
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        // Create the geometry factory use to create the output of .getDiagram()
        GeometryFactory geoFactory = new GeometryFactory();

        // Set the coordinates to develop polygons around
        voronoi.setSites(this.coordinates);

        // Set space in which the diagram will be computed on (i.e. the canvas size)
        voronoi.setClipEnvelope(new Envelope(new Coordinate(0,0),new Coordinate(this.canvasSizeX,this.canvasSizeY)));

        // Compute the diagram, returns as a collection of polygons
        Geometry polygonsCollection = voronoi.getDiagram(geoFactory);

        // UNUSED
        // Since diagram returns a collection of geometry, need to parse through it all and get all Polygons it made
        // Create a new array list to return
        //ArrayList<Polygon> returnList = new ArrayList<>();

        // Add all polygons
        //for (int index = 0; index < polygonsCollection.getNumGeometries(); index++) {
        //returnList.add((Polygon) polygonsCollection.getGeometryN(index));
        //}

        this.geoCollection = polygonsCollection;
    }

    /**
     * Gets the current Voronoi diagram as a geometry collection of polygons (JTS)
     */
    public Geometry getGeoCollection() {
        return this.geoCollection;
    }

}
