package ca.team50.water;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.IslandShape;

import java.util.ArrayList;

public class RiverGenerator {

    public RiverGenerator (PolyMesh<Polygons> mesh, IslandShape islandShape, int maxRivers, double thresholdAltitude){

        //give all vertices altitude
        ArrayList<Structs.Vertex> currPolyVertices = new ArrayList<>();
        ArrayList<Structs.Vertex> totalPolyVertices = new ArrayList<>();

        for (Polygons currPolygon : mesh) {
            currPolygon.unifyElevation();
        }

        //determine beginnings of rivers (above threshold altitude)
        ArrayList<Structs.Vertex> beginningCandidates = new ArrayList<>();

        // Find vertices that are above the threshold to be considered for river generation
        for(Polygons currentPolygon : mesh) {
            for (Structs.Vertex currVertex : currentPolygon.getVerticesList()){
                if (islandShape.isVertexInside(currentPolygon.getCentroid()) && getElevation(currVertex) >= thresholdAltitude)
                    beginningCandidates.add(currVertex);
            }
        }
        ArrayList<Structs.Vertex> riverStart = new ArrayList<>();
        for (int i = 0; i < maxRivers; i++) {
            int index = (int)(Math.random() * beginningCandidates.size());
            riverStart.add(beginningCandidates.get(index));
        }


        Structs.Vertex v = riverStart.get(0);
        ArrayList<Structs.Vertex> riverSegment = new ArrayList<>();

        Structs.Vertex low = v;

        while (getElevation(low) != 0.0){

            //find all vertices linked to vertex
        for(Polygons currentPolygon : mesh) {
            for (Structs.Segment currSegment : currentPolygon.getSegmentsList()){
                //
                if (currentPolygon.getVerticesList().get(currSegment.getV1Idx()) == v){
                    riverSegment.add(currentPolygon.getVerticesList().get(currSegment.getV2Idx()));
                }
                if(currentPolygon.getVerticesList().get(currSegment.getV2Idx()) == v){
                    riverSegment.add(currentPolygon.getVerticesList().get(currSegment.getV1Idx()));
                }
            }
        }

        //find lowest altitude vertex linked by single segment
        for (int j = 0; j < riverSegment.size(); j++){
            for (int k = 1; k < riverSegment.size(); k++){
                if (getElevation(riverSegment.get(j)) < getElevation(riverSegment.get(k))) {
                    low = riverSegment.get(j);
                }
            }
        }

        //find said low value vertices in mesh and color blue.
            int i = 0;
        for(Polygons currentPolygon : mesh) {
            for (Structs.Vertex v1: currentPolygon.getVerticesList()){
                if (v1 == low)
                    break;
            }

            i++;
            }

        mesh.get(i).changeRiverColor(low);

        v = low;

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
