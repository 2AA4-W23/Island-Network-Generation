package ca.team50.water;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.IslandShape;

import java.util.ArrayList;

public class RiverGenerator {

    public RiverGenerator (PolyMesh<Polygons> mesh, int maxRivers, double thresholdAltitude){

        //give all vertices altitude
        ArrayList<Structs.Vertex> currPolyVertices = new ArrayList<>();
        ArrayList<Structs.Vertex> totalPolyVertices = new ArrayList<>();

        for (Polygons currPolygon : mesh) {
            currPolygon.unifyElevation();
        }

        ArrayList<Structs.Vertex> beginningCandidates = new ArrayList<>();

        // Find vertices that are above the threshold to be considered for river generation
       for(Polygons currentPolygon : mesh) {
            for (Structs.Vertex currVertex : currentPolygon.getVerticesList()){
                if (getElevation(currVertex) >= thresholdAltitude)
                    beginningCandidates.add(currVertex);
            }
       }

        //from possible candidates choose appropriate number of vertices for the starting point of rivers
        ArrayList<Structs.Vertex> riverStart = new ArrayList<>();
        for (int i = 0; i < maxRivers; i++) {
            int index = (int)(Math.random() * beginningCandidates.size());
            riverStart.add(beginningCandidates.get(index));
        }

        Structs.Vertex start = riverStart.get(0);
        System.out.println("start " + getElevation(start));

        //find segments containing the start vertex
        ArrayList<Polygons> neighbors = new ArrayList<>();

        for(Polygons currPoly : mesh) {
            for (Structs.Vertex v: currPoly.getVerticesList()){
                if (v.getX() == start.getX() && v.getY() == start.getY()){
                    neighbors.add(currPoly);
                }
            }
        }

        ArrayList<Structs.Vertex> neighborV = new ArrayList<>();
        ArrayList<Structs.Segment> neighborS = new ArrayList<>();

        //find all segments in neighbors containing the start Vertex
        for(Polygons currPoly : neighbors) {
            for (Structs.Segment s: currPoly.getSegmentsList()){
               if (currPoly.getVerticesList().get(s.getV1Idx()).getX() == start.getX() && currPoly.getVerticesList().get(s.getV1Idx()).getY() == start.getY()){
                   neighborV.add(currPoly.getVerticesList().get(s.getV2Idx()));
                   neighborS.add(s);
               }
                if (currPoly.getVerticesList().get(s.getV2Idx()).getX() == start.getX() && currPoly.getVerticesList().get(s.getV2Idx()).getY() == start.getY()){
                    neighborV.add(currPoly.getVerticesList().get(s.getV1Idx()));
                    neighborS.add(s);
                }

            }
        }






    }



    private double getElevation(Structs.Vertex v) {

        String val = null;
        for (Structs.Property p : v.getPropertiesList()) {
            if (p.getKey().equals("altitude")) {
                val = p.getValue();
            }
        }
        if (val == null) {
            return 0;
        }

        double alphaVal = Double.parseDouble(val);

        return alphaVal;
    }


}
