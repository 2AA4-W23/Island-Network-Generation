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

public class Polygons extends Polygon {

    private List<Vertex> verticiesList;
    private List<Segment> segmentsList;

    // Vertices and Segments
    public Polygons(List<Vertex> vertices) {
        this.verticiesList = vertices;

        this.segmentsList = new ArrayList<>();

        Iterator<Vertex> curentVertexIterator = this.verticiesList.iterator();
        Iterator<Vertex> nextVertexIterator = this.verticiesList.iterator();
        nextVertexIterator.next();

        while (nextVertexIterator.hasNext()) {

            Vertex curentVertex = curentVertexIterator.next();
            Vertex nextVertex = nextVertexIterator.next();

            Segment v1Segment = Segment.newBuilder().setV1Idx(verticiesList.indexOf(curentVertex)).build();
            Segment v2Segment = v1Segment.toBuilder().setV2Idx(verticiesList.indexOf(nextVertex)).build();
            segmentsList.add(v2Segment);

        }


    }

    public List<Vertex> getVerticiesList() {
        return verticiesList;
    }

    public List<Segment> getSegmentsList() {
        return segmentsList;
    }

    public Point getCentroid() {
        Point returnPoint = new Point();
        return returnPoint;
    }

}
