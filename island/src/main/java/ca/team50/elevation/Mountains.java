package ca.team50.elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Circle;
import ca.team50.shapes.IslandShape;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

import static ca.team50.elevation.Volcano.volcanoAltitude;

public class Mountains {

    public static void mountainAltitude(List<Polygons> islandPoly, double numOf, double topAltitude, double botAltitude, double slopeRadius ){

       for (int i = 0; i < numOf; i++) {

            int index = (int)(Math.random() * islandPoly.size());
            System.out.println(index);

            Structs.Vertex peak = islandPoly.get(index).getCentroid();

            volcanoAltitude(islandPoly, peak, topAltitude, botAltitude, slopeRadius, slopeRadius, 30 );

       }
    }

}

