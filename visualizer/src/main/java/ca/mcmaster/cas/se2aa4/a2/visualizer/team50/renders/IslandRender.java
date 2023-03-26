package ca.mcmaster.cas.se2aa4.a2.visualizer.team50.renders;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.visualizer.team50.properties.Property;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class IslandRender implements Renderable {
    @Override
    public void render(PolyMesh<? extends Polygons> polygons, Graphics2D canvas) {

        System.out.println("Island mode enabled");

        ArrayList<Structs.Vertex> vertices = new ArrayList<Structs.Vertex>();

        // Execute methods to draw to screen
        drawPolygons(polygons,canvas);
        drawCentroids(polygons,canvas);
        drawSegments(polygons,canvas,vertices);
        drawVertices(vertices,canvas);
    }


    // Method to draw polygons to canvas
    private void drawPolygons(PolyMesh<? extends Polygons> polygons, Graphics2D canvas) {
        //iterate through and draw each polygon with appropriate color
        for (int index = 0; index < polygons.size(); index++){

            ArrayList<Integer> x_vertices = new ArrayList<Integer>();
            ArrayList<Integer> y_vertices = new ArrayList<Integer>();
            int n = 0;
            Color color;
            int R = 0;
            int G = 0;
            int B = 0;
            float alpha = 0;

            for (Structs.Vertex v: polygons.get(index).getVerticesList()) {
                x_vertices.add((int)v.getX());
                y_vertices.add((int)v.getY());
                n++;

                alpha = Property.extractAlpha(v.getPropertiesList());

                color = Property.extractColor(v.getPropertiesList());
                R = color.getRed();
                G = color.getGreen();
                B = color.getBlue();
            }

            int[] x = x_vertices.stream().mapToInt(Integer::intValue).toArray();
            int[] y = y_vertices.stream().mapToInt(Integer::intValue).toArray();

            canvas.setColor(new Color(R, G, B));
            // Set alpha using Porter-Duff blend mode
            canvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            canvas.fillPolygon(x, y, n);
        }
    }

    // Method to draw centroids to canvas
    private void drawCentroids(PolyMesh<? extends Polygons> polygons, Graphics2D canvas) {
        //iterate through and draw each centroid with appropriate color and thickness
        for (int index = 0; index < polygons.size(); index++){

            Structs.Vertex centroid = polygons.get(index).getCentroid();
            Color centroidColor = Property.extractColor(centroid.getPropertiesList());
            Float thickness = Property.extractThickness(centroid.getPropertiesList());

            double centre_x = centroid.getX() - (thickness / 2.0d);
            double centre_y = centroid.getY() - (thickness / 2.0d);
            canvas.setColor(centroidColor);
            Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, thickness, thickness);
            canvas.fill(point);
        }
    }

    // Method to draw segments to canvas
    // Draw segments also takes in a vertices arraylist which is modified to contain all vertices after execution
    // (Reduces duplication in drawVertices as this method gets vertices already)
    private void drawSegments(PolyMesh<? extends Polygons> polygons, Graphics2D canvas, ArrayList<Structs.Vertex> vertices) {

        float segmentAlpha = 0f;

        //iterate through and draw each line segment with appropriate color and thickness
        for (int index = 0; index < polygons.segmentSize(); index++) {
            Structs.Vertex[] segment = polygons.getSegment(index);
            Structs.Vertex start_Point = segment[0];
            Structs.Vertex end_Point = segment[1];

            vertices.add(start_Point);
            vertices.add(end_Point);
            // Set alpha using Porter-Duff blend mode
            canvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, segmentAlpha));

            //set segment color
            Color v1Color = Property.extractColor(start_Point.getPropertiesList());
            Color v2Color = Property.extractColor(end_Point.getPropertiesList());

            //calculate average color and set as segment color
            int R = (v1Color.getRed() + v2Color.getRed())/2;
            int G = (v1Color.getGreen() + v2Color.getGreen())/2;
            int B = (v1Color.getBlue() + v2Color.getBlue())/2;
            canvas.setColor(new Color(R, G, B));

            //calculate average thickness and set as segment thickness
            Float segmentThickness = 0.5f;
            Stroke segmentStroke = new BasicStroke(segmentThickness);
            canvas.setStroke(segmentStroke);

            //draw segments with averaged color and thickness
            Point2D startPoint = new Point2D.Double(start_Point.getX(), start_Point.getY());
            Point2D endPoint = new Point2D.Double(end_Point.getX(), end_Point.getY());

            Line2D line = new Line2D.Double(startPoint,endPoint);

            canvas.draw(line);
            canvas.fill(line);
        }
    }

    // Method to draw vertices (avoids duplicates)
    private void drawVertices(ArrayList<Structs.Vertex> vertices, Graphics2D canvas) {
        //Draw vertices with appropriate color and thickness

        ArrayList<Structs.Vertex> duplicates = new ArrayList<Structs.Vertex>(4);

        for (int j = 0; j < vertices.size(); j++){
            float thickness = 0.5f;
            Color color = (new Color(0, 0, 0));
            int R = 0;
            int G = 0;
            int B = 0;
            int n = 0;


            for (int i = 0; i < vertices.size(); i++) {
                if (((vertices.get(j).getX() == vertices.get(i).getX()) ) && ((vertices.get(j).getY() == vertices.get(i).getY())) ){
                    duplicates.add(vertices.get(i));
                }

                if (i == vertices.size()-1){
                    for (Structs.Vertex v: duplicates) {
                        thickness += Property.extractThickness(v.getPropertiesList());
                        color = Property.extractColor(v.getPropertiesList());

                        R = color.getRed();
                        G = color.getGreen();
                        B = color.getBlue();
                        n++;
                    }

                    canvas.setColor(new Color(0, 0, 0));

                    Ellipse2D point = new Ellipse2D.Double(vertices.get(j).getX() - (thickness / 2.0d), vertices.get(j).getY()- (thickness / 2.0d), thickness, thickness);
                    canvas.fill(point);

                    duplicates.clear();
                }

            }
        }
    }

}
