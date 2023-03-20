package ca.team50.adt;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.team50.generation.RandomGen;

import java.io.Serializable;
import java.util.*;

public class Polygons implements Serializable {

    private List<Vertex> verticesList;
    private List<Segment> segmentsList;
    private Vertex centroid;


    /**
     * Returns a Polygon object that describes an abstract shape based on the amount of vertices in a list.
     * Calculates centroid automatically
     * @param vertexList the list of all vertices in the shape, in-order
     * @exception IllegalStateException if list passed is empty
     * @return the abstract shape as a Polygon object
     */



    public Polygons(List<Vertex> vertexList) throws IllegalStateException {

        if (vertexList.isEmpty()) {
            throw new IllegalStateException("Vertex list cannot be empty");
        }


        //Utilize convex hull to reorder the vertices such that segment consecutiveness invariance remains
        vertexList = convexHull(vertexList);


        this.verticesList = new ArrayList<>();
        this.verticesList.addAll(vertexList);

        this.segmentsList = new ArrayList<>();

        for (int index = 0; index < vertexList.size(); index++) {

            Vertex currentVertex = this.verticesList.get(index);

            // Check if the end of the list was reached
            if (index+1 >= verticesList.size()) {

                // If so, connect the final segment from the last to the first Vertex
                Vertex firstVertex = this.verticesList.get(0);

                Segment segment = Segment.newBuilder().setV1Idx(verticesList.indexOf(currentVertex)).setV2Idx(verticesList.indexOf(firstVertex)).build();

                segmentsList.add(segment);
                break;

            }

            Vertex nextVertex = this.verticesList.get(index+1);

            Segment segment = Segment.newBuilder().setV1Idx(verticesList.indexOf(currentVertex)).setV2Idx(verticesList.indexOf(nextVertex)).build();
            segmentsList.add(segment);

        }

        this.centroid = calculateCentroid();
    }

    // Method to colour all segments and vertices the same
    public void unifyColor(int[] RGBColour) {

        // Create property
        String colorCode = RGBColour[0] + "," + RGBColour[1] + "," + RGBColour[2];
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();

        ArrayList<Vertex> newList = new ArrayList<>();

        // Give all vertices this colour
        for (Vertex currentVertex : this.getVerticesList()) {
                newList.add(currentVertex.toBuilder().addProperties(color).build());
        }

        this.verticesList = newList;

    }

    // methods for finding convex hull of polygons
    private static Double orientation(Vertex p, Vertex q, Vertex r)
    {
        Double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0.0;  // collinear
        return (val > 0)? 1.0: 2.0; // clock or counterclock wise
    }

    private static List<Vertex> convexHull(List<Vertex> points) {
        int n = points.size();
        // There must be at least 3 points
        if (n < 3) return points;

        // Initialize Result
        ArrayList<Vertex> hull = new ArrayList<Vertex>();

        // Find the leftmost point
        int leftmost = 0;
        for (int i = 1; i < n; i++)
            if (points.get(i).getX() < points.get(0).getX()) {
                leftmost = i;
            }

        int p = leftmost, q;

        do{
            // Add current point to result
            hull.add(points.get(p));

            // Search for a point 'q' such that orientation(p, q, x) is counterclockwise for all points 'x'
            q = (p + 1) % n;

            for (int i = 0; i < n; i++) {
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2) q = i;
            }

            // Now q is the most counterclockwise
            p = q;
        }
        while (p != leftmost);

        return hull;
    }



    /**
     * Returns a Polygon object that describes an abstract shape based on the amount of vertices in a list.
     * Does not perform centroid calculation automatically, user must specify the location of the centroid
     * @param vertexList the list of all vertices in the shape, in-order
     * @param centroidOverride provide a centroid position as a Vertex object
     * @exception IllegalStateException if list passed is empty
     * @return the abstract shape as a Polygon object
     */
    public Polygons(List<Vertex> vertexList, Vertex centroidOverride) throws IllegalStateException {

        if (vertexList.isEmpty()) {
            throw new IllegalStateException("Vertex list cannot be empty");
        }

        //Utilize convex hull to reorder the vertices such that segment consecutiveness invariance remains
        vertexList = convexHull(vertexList);

        this.verticesList = new ArrayList<>();
        this.verticesList.addAll(vertexList);

        this.segmentsList = new ArrayList<>();

        for (int index = 0; index < vertexList.size(); index++) {

            Vertex currentVertex = this.verticesList.get(index);

            // Check if the end of the list was reached
            if (index+1 >= verticesList.size()) {

                // If so, connect the final segment from the last to the first Vertex
                Vertex firstVertex = this.verticesList.get(0);

                Segment segment = Segment.newBuilder().setV1Idx(verticesList.indexOf(currentVertex)).setV2Idx(verticesList.indexOf(firstVertex)).build();

                segmentsList.add(segment);
                break;

            }

            Vertex nextVertex = this.verticesList.get(index+1);

            Segment segment = Segment.newBuilder().setV1Idx(verticesList.indexOf(currentVertex)).setV2Idx(verticesList.indexOf(nextVertex)).build();
            segmentsList.add(segment);

        }

        // Don't perform centroid calculation, instead copy what is given as the parameter
        this.centroid = centroidOverride;

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

        // Create centoid with random alpha, thickness and color
        Vertex centroid = RandomGen.thicknessGen(RandomGen.colorGen(RandomGen.alphaGen(Vertex.newBuilder().setX(xAverage).setY(yAverage).build())));

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
