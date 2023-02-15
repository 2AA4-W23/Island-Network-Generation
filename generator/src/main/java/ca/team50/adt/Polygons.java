package ca.team50.adt;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.awt.Polygon;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class Polygons extends Polygon {

    private List<Vertex> verticesList;
    private List<Segment> segmentsList;
    private Vertex centroid;


    /**
     * Returns a Polygon object that describes an abstract shape based on the amount of vertices in a list
     * @param vertexList the list of all vertices in the shape, in-order
     * @exception IllegalStateException if list passed is empty
     * @return the abstract shape as a Polygon object
     */
    public Polygons(List<Vertex> vertexList) throws IllegalStateException {

        if (vertexList.isEmpty()) {
            throw new IllegalStateException("Vertex list cannot be empty");
        }

        this.verticesList = vertexList;

        this.segmentsList = new ArrayList<>();

        Iterator<Vertex> curentVertexIterator = this.verticesList.iterator();
        Iterator<Vertex> nextVertexIterator = this.verticesList.iterator();
        nextVertexIterator.next();

        while (nextVertexIterator.hasNext()) {

            Vertex curentVertex = curentVertexIterator.next();
            Vertex nextVertex = nextVertexIterator.next();

            Segment v1Segment = Segment.newBuilder().setV1Idx(verticesList.indexOf(curentVertex)).build();
            Segment v2Segment = v1Segment.toBuilder().setV2Idx(verticesList.indexOf(nextVertex)).build();
            segmentsList.add(v2Segment);

        }

        this.centroid = calculateCentroid();
    }

    private Vertex calculateCentroid() {

        List<Vertex> vertexList = this.getVerticesList();

        double yAverage = 0;
        double xAverage = 0;
        int listSize = vertexList.size();

        // Find average x and y position
        for (Vertex currentVertex : vertexList) {
            xAverage+=currentVertex.getX();
            yAverage+=currentVertex.getY();
        }

        yAverage = yAverage/listSize;
        xAverage = xAverage/listSize;

        Vertex centroid = Vertex.newBuilder().setX(xAverage).setY(yAverage).build();

        return centroid;
    }

    public Vertex getCentroid() {
        return this.centroid;
    }

    public List<Vertex> getVerticesList() {
        return this.verticesList;
    }

    public List<Segment> getSegmentsList() {
        return this.segmentsList;
    }


    @Override
    public boolean equals(Object o) {
        try {
            Polygons polygonToCompare = (Polygons) o;

            if (this.verticesList.size() != polygonToCompare.getVerticesList().size()) {
                return false;
            }

            Iterator<Vertex> thisPolygonList = this.verticesList.iterator();
            Iterator<Vertex> comparePolygonList = polygonToCompare.getVerticesList().iterator();

            while(comparePolygonList.hasNext() && thisPolygonList.hasNext()) {

                Vertex thisCurrentVertex = thisPolygonList.next();
                Vertex compareCurrentVertex = comparePolygonList.next();

                if (thisCurrentVertex.getX() != compareCurrentVertex.getX() || thisCurrentVertex.getY() != compareCurrentVertex.getY()) {
                    return false;
                }

            }

            return true;

        } catch (ClassCastException e) {
            return false;
        }
    }
}
