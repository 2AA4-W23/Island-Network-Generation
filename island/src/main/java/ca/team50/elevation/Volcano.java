package ca.team50.elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Circle;
import ca.team50.shapes.IslandShape;
import org.apache.xmlgraphics.util.dijkstra.Vertex;

import java.util.ArrayList;

import static ca.team50.generation.TestIsland.extractProperties;

public class Volcano {

    public static void volcanoAltitude(ArrayList<Polygons> islandPoly, Structs.Vertex center, double topAltitude, double botAltitude, double islandwidth, double islandlength, double area ) {

        //Determine max radius of volcano
        double maxRadius = Math.min(islandwidth, islandlength);


        // Radius of peak of volcano is not greater than one third of the total allowable radius for entire volcano
        double botRadius = maxRadius - area;
        double slopeRadius = botRadius - area;

        double steepness = 2 + (Math.random() * 6);
        double slopeIntervalRadius = slopeRadius / (steepness-1);
        double slopeIntervalAltitude= (topAltitude-botAltitude)/(steepness-1);

        IslandShape topCircle = new Circle(center, area);
        IslandShape botCircle = new Circle(center, botRadius);
        IslandShape slopeCircle;

        for (Polygons currPoly: islandPoly) {
            Structs.Vertex centroid = currPoly.getCentroid();
            if (botCircle.isVertexInside(centroid)) {
                currPoly.changeElevation(String.valueOf(botAltitude));
            }

        }

        for (int i = 0; i < steepness; i++){
            double r = botRadius - (slopeIntervalRadius * i);
            slopeCircle = new Circle(center, r);
            //System.out.println(r);

            for (Polygons currPoly: islandPoly) {
                Structs.Vertex centroid = currPoly.getCentroid();
                double a = botAltitude + (slopeIntervalAltitude * i);
                a = a*100;
                a = (double) Math.round(a);
                a = a/100;

                if (a > topAltitude)
                    a = topAltitude;

                double average = extractProperties(centroid.getPropertiesList(), "altitude");
                a = (a+average)/2;

                //System.out.println(a);

                if (slopeCircle.isVertexInside(centroid)) {
                    currPoly.changeElevation(String.valueOf(a));
                }

                if (topCircle.isVertexInside(currPoly.getCentroid())) {
                    currPoly.changeElevation(String.valueOf(topAltitude));
                }
            }

        }



    }
}
