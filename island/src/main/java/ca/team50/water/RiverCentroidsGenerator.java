package ca.team50.water;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.util.ArrayList;

public class RiverCentroidsGenerator {

    public RiverCentroidsGenerator(ArrayList<Polygons> mesh, int maxRivers, double thresholdAltitude){

        ArrayList<Polygons> river = new ArrayList<>();

        // get beginning of river, the polygon
        ArrayList<Structs.Vertex> beginningCandidates = new ArrayList<>();

        int index = 0;
        ArrayList<Integer> beginIndices = new ArrayList<>();
        for(int  i = 0; i < mesh.size(); i++) {
            if (getElevation(mesh.get(i).getCentroid()) > thresholdAltitude) {
                beginningCandidates.add(mesh.get(i).getCentroid());
                river.add(mesh.get(i));
                index = i;
            }
        }


        ArrayList<Polygons> neighbors = new ArrayList<>();

            int k = 0;
            int l = 0;

            while (l < 2) {
                //find neighboring polygons
                for (int j = 0; j < mesh.size(); j++) {
                    if (mesh.isNeighbor(index, j)) {
                        neighbors.add(mesh.get(j));
                        k = j;
                    }
                }

                //Find neighbor with lowest elevation
                Polygons LowElevationNeighbor = mesh.get(0);
                double lowElevation = 1.0;

               for (Polygons currPoly : neighbors) {
                    if (getElevation(currPoly.getCentroid()) < lowElevation) {
                        if (l < 2 && getElevation(currPoly.getCentroid()) != 0) {
                            lowElevation = getElevation(currPoly.getCentroid());
                            LowElevationNeighbor = currPoly;
                        }
                    }
                }

                //add neighbor with lowest elevation to an arraylist for rendering
                river.add(LowElevationNeighbor);

               if (getElevation(mesh.get(index).getCentroid()) == 0)
                   l++;

                //recursion
                index = k;



            }


        for (Polygons currPoly: river){
            currPoly.setAsRiver();
            System.out.println(currPoly.getCentroid().getPropertiesList());}



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
