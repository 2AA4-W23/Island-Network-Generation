package ca.mcmaster.cas.se2aa4.a2.visualizer.team50.renders;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class DebugRender implements Renderable {

    private static final int THICKNESS = 3;
    private static final Color centroidColor = Color.RED;
    private static final Color neighborColor = Color.GRAY;
    private static final Color segmentColor = Color.BLACK;

    @Override
    public void render(PolyMesh<? extends Polygons>polygons, Graphics2D canvas) {
        System.out.println("Debug mode enabled");

        ArrayList<Structs.Vertex> vertices = new ArrayList<Structs.Vertex>();

        // Draw data to screen
        drawCentroids(polygons,canvas);
        drawNeighbors(polygons,canvas);
        drawSegments(polygons,canvas,vertices);
        drawVertices(canvas,vertices);

    }

    // Method to draw centroids of Polygons to screen
    private void drawCentroids(PolyMesh<? extends Polygons> polygons, Graphics2D canvas) {
        for (int index = 0; index < polygons.size(); index++){
            Structs.Vertex centroid = polygons.get(index).getCentroid();
            double centre_x = centroid.getX() - (THICKNESS / 2.0d);
            double centre_y = centroid.getY() - (THICKNESS / 2.0d);
            Color old = canvas.getColor();
            canvas.setColor(centroidColor);
            Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
            canvas.fill(point);
            canvas.setColor(old);
        }
    }

    // Method to draw neighbour relations
    private void drawNeighbors(PolyMesh<? extends Polygons> polygons, Graphics2D canvas) {
        //Get neighboring relations and color Grey
        for (int index = 0; index < polygons.size(); index++) {
            for (int index1 = 0; index1 < polygons.size(); index1++) {
                if (polygons.isNeighbor(index, index1)) {
                    Point2D startPoint = new Point2D.Double(polygons.get(index).getCentroid().getX(), polygons.get(index).getCentroid().getY());
                    Point2D endPoint = new Point2D.Double(polygons.get(index1).getCentroid().getX(), polygons.get(index1).getCentroid().getY());

                    Line2D line = new Line2D.Double(startPoint, endPoint);
                    canvas.setColor(neighborColor);
                    canvas.draw(line);
                    canvas.fill(line);
                }
            }
        }
    }

    // Method to draw segments to canvas
    // Modifies an array list for drawVertices (further explained in NormalRender)
    private void drawSegments(PolyMesh<? extends Polygons> polygons, Graphics2D canvas, ArrayList<Structs.Vertex> vertices) {
        // Color all segments BLACK
        for (int index = 0; index < polygons.segmentSize(); index++) {
            Structs.Vertex[] segment = polygons.getSegment(index);
            Structs.Vertex start_Point = segment[0];
            Structs.Vertex end_Point = segment[1];

            vertices.add(start_Point);
            vertices.add(end_Point);

            Point2D startPoint = new Point2D.Double(start_Point.getX(), start_Point.getY());
            Point2D endPoint = new Point2D.Double(end_Point.getX(), end_Point.getY());

            Line2D line = new Line2D.Double(startPoint,endPoint);
            canvas.setColor(segmentColor);
            canvas.draw(line);
            canvas.fill(line);
        }
    }

    // Method to draw black vertices to canvas (no duplicates)
    private void drawVertices(Graphics2D canvas, ArrayList<Structs.Vertex> vertices) {
        //Color all vertices Black
        ArrayList<Structs.Vertex> duplicates = new ArrayList<Structs.Vertex>(4);

        for (int j = 0; j < vertices.size(); j++) {
            for (int i = 0; i < vertices.size(); i++) {

                if (((vertices.get(j).getX() == vertices.get(i).getX())) && ((vertices.get(j).getY() == vertices.get(i).getY()))) {
                    duplicates.add(vertices.get(i));
                }

                if (i == vertices.size() - 1) {
                    Ellipse2D point = new Ellipse2D.Double(vertices.get(j).getX() - (THICKNESS / 2.0d), vertices.get(j).getY() - (THICKNESS / 2.0d), THICKNESS, THICKNESS);
                    canvas.fill(point);

                    duplicates.clear();
                }
            }
        }
    }

}
