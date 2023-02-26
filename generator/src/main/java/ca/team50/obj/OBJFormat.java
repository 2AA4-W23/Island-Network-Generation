package ca.team50.obj;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class OBJFormat {

    public static void writeToObj(PolyMesh<Polygons> mesh, String name) {

        try {
            FileWriter writer = new FileWriter(name+".obj");

            // Get each polygon, write each of it's vertices to file

            int index = 1;

            int currentTotalPrintedVerticies = 0;
            int currentPrinted = 0;

            for (Polygons currentPolygon : mesh) {

                writer.write("# Polygon: " + index + "\n");
                index++;

                List<Vertex> vertexList = currentPolygon.getVerticesList();
                ArrayList<Segment> segmentsList = (ArrayList<Segment>) currentPolygon.getSegmentsList();

                currentPrinted = 0;

                // Write all vertices for a given Polygon to file
                for (Vertex currentVertex : vertexList) {

                    writer.write("v" + " " + currentVertex.getX() + " " + currentVertex.getY() + " " + "0\n");
                    currentPrinted+=1;
                    currentTotalPrintedVerticies+=1;

                }

                // Construct string to reference vertices in obj format
                // i.e. take the current total printed vertices value, start the first reference from that value minus however many vertices were written to the file
                // Then increment by one upwards until we hit the current max vertex printed
                String faceCommand = "f ";
                for (int curIndex = currentTotalPrintedVerticies-currentPrinted+1; curIndex <= currentTotalPrintedVerticies; curIndex++) {
                    faceCommand+=(curIndex + " ");
                }

                writer.write(faceCommand+"\n");

            }

            writer.close();

        } catch (Exception e) {

        }

    }

}
