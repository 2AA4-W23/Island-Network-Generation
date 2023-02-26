package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoronoiGen {

    // Variable declarations
    private int canvasSizeX;
    private int canvasSizeY;
    private Geometry geoCollection;
    private Geometry triCollection;
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
        crop();
        computeTriangulation();


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

            // Get all polygons
            List<Polygon> polygonList = getPolygons();

            // Reset points list
            this.coordinates.clear();

            // Loop through each Polygon and get the centroid
            for (Polygon currentPolygon : polygonList) {

                // Then add the centroid to the points Arraylist
                this.coordinates.add(currentPolygon.getCentroid().getCoordinate());

            }

            // Recompute diagram
            generateDiagram();
            crop();
            computeTriangulation();

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

        this.geoCollection = polygonsCollection;
    }

    /**
     * Gets the current Voronoi diagram as a geometry collection of polygons (JTS)
     */
    public Geometry getGeoCollection() {
        return this.geoCollection;
    }

    /**
     * Gets the current Voronoi diagram triangulated geometry collection of polygons (JTS)
     */
    public Geometry getTriCollection() {
        return this.triCollection;
    }

    private void crop(){

        // Get all Polygons from geo collection
        List<Polygon> polygonList = getPolygons();

        // Loop through each Polygon and modify coordinates
        for (Polygon currentPolygon : polygonList) {

            for (Coordinate c: currentPolygon.getCoordinates()){
                if (c.getX() > this.canvasSizeX) {
                c.setX(this.canvasSizeX);
            }
                if (c.getY() > this.canvasSizeY) {
                    c.setY(this.canvasSizeY);
                }
            }

        }

    }

    // Method to get neighbourhood relationships of irregular mesh
    private void computeTriangulation() {

        List<Polygon> polygonList = getPolygons();

        List<Coordinate> centroidList = new ArrayList<>();

        // Loop through every Polygon, get their centroid and add it to list as a coordinate
        for (Polygon currentPolygon : polygonList) {

            Point centroid = currentPolygon.getCentroid();

            centroidList.add(new Coordinate(centroid.getX(),centroid.getY()));

        }

        // Create a new triangulation builder
        DelaunayTriangulationBuilder triBuilder = new DelaunayTriangulationBuilder();

        // Create the geometry factory use to create the output of .getEdges()
        GeometryFactory geoFactory = new GeometryFactory();

        // Set the coordinates in which to compute triangulations from
        triBuilder.setSites(centroidList);

        // Get computed Triangles as polygons
        Geometry triCollectionGen = triBuilder.getTriangles(geoFactory);

        this.triCollection = triCollectionGen;

    }

    // Method to extract polygons from geo collection
    private List<Polygon> getPolygons() {

        List<Polygon> polygonList = new ArrayList<>();

        // Get all Polygons from Geometry collection
        for (int index = 0; index < this.geoCollection.getNumGeometries(); index++) {
            polygonList.add((Polygon) geoCollection.getGeometryN(index));
        }

        return polygonList;

    }

}
