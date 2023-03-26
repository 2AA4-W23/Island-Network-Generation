package ca.team50.elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.team50.elevation.Plains;
import ca.team50.generation.TestIsland;

import java.util.ArrayList;
import java.util.List;

public class Plains {

    //later, overall base altitude of plains can be determined through command line, and to what degree altitudes fluctuates can also be user determined
    public static void plainsAltitude(List<Polygons> mesh, double altitude, double fluctuation) {


        for (Polygons currPoly: mesh){
             currPoly.changeElevation(String.valueOf((altitude - fluctuation) + (Math.random() * (((altitude + fluctuation) - (altitude - fluctuation)) + 0.1))));
        }


    }




}