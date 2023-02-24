package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.team50.adt.PolyMesh;

import java.util.ArrayList;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.List;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import java.lang.Float;

public class GraphicRenderer{

    private static final int THICKNESS = 3;
    private boolean debugMode = false;


    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }


    public void render(PolyMesh polygons , Graphics2D canvas) {
        
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);

        if (polygons.debugMode) {
            System.out.println("Debug mode enabled");

            for (int index = 0; index < polygons.size(); index++){
                
                Vertex centroid = polygons.get(index).getCentroid();
                List<Vertex> vertices = polygons.get(index).getVerticesList();

                //color Vertices Black
                for (Vertex v: vertices){
                    double centre_x1 = v.getX() - (THICKNESS/2.0d);
                    double centre_y1 = v.getY() - (THICKNESS/2.0d);
                    Color old = canvas.getColor();
                    canvas.setColor(Color.BLACK);
                    Ellipse2D point = new Ellipse2D.Double(centre_x1, centre_y1, THICKNESS, THICKNESS);
                    canvas.fill(point);
                    canvas.setColor(old);
                }

                //color Centroids RED
                double centre_x = centroid.getX() - (THICKNESS / 2.0d);
                double centre_y = centroid.getY() - (THICKNESS / 2.0d);
                Color old = canvas.getColor();
                canvas.setColor(Color.RED);
                Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
                canvas.fill(point);
                canvas.setColor(old);


                for (int index1 = 0; index < polygons.size(); index++){

                    Vertex centroid1 = polygons.get(index1).getCentroid();

                    //color neighborhood relations grey
                    if (polygons.isNeighbor(index, index1)){
                        Point2D startPoint = new Point2D.Double(centroid.getX(), centroid.getY());
                        Point2D endPoint = new Point2D.Double(centroid1.getX(), centroid1.getY());
            
                        Line2D line = new Line2D.Double(startPoint,endPoint);
                        canvas.setColor(Color.GRAY);
                        canvas.draw(line);
                        canvas.fill(line);
                    }  
                }
            }
            // Color all polygons BLACK
            for (int index = 0; index < polygons.segmentSize(); index++) {
            
                Vertex[] segment = polygons.getSegment(index);
                Vertex start_Point = segment[0];
                Vertex end_Point = segment[1];

                Point2D startPoint = new Point2D.Double(start_Point.getX(), start_Point.getY());
                Point2D endPoint = new Point2D.Double(end_Point.getX(), end_Point.getY());
            
                Line2D line = new Line2D.Double(startPoint,endPoint);
                canvas.setColor(Color.BLACK);
                canvas.draw(line);
                canvas.fill(line);
            }

        }


        else {
            System.out.println("Normal mode enabled");
            ArrayList<Vertex> vertices = new ArrayList<Vertex>();


/*            //iterate through and draw each centroid with appropriate color and thickness
            for (int index = 0; index < polygons.size(); index++){

                Vertex centroid = polygons.get(index).getCentroid();
                Color centroidColor = extractColor(centroid.getPropertiesList());
                Float thickness = extractThickness(centroid.getPropertiesList());

                double centre_x = centroid.getX() - (thickness / 2.0d);
                double centre_y = centroid.getY() - (thickness / 2.0d);
                Color old = canvas.getColor();
                canvas.setColor(centroidColor);
                Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, thickness, thickness);
                canvas.fill(point);
                canvas.setColor(old);
            }*/

           //iterate through and draw each line segment with appropriate color and thickness
            for (int index = 0; index < polygons.segmentSize(); index++) {
                Vertex[] segment = polygons.getSegment(index);
                Vertex start_Point = segment[0];
                Vertex end_Point = segment[1];

                vertices.add(start_Point);
                vertices.add(end_Point);

                /*//set Vertices thickness
                Float thicknessV1 = extractThickness(start_Point.getPropertiesList());
                Float thicknessV2 = extractThickness(end_Point.getPropertiesList());

                //set segment color
                Color v1Color = extractColor(start_Point.getPropertiesList());
                Color v2Color = extractColor(end_Point.getPropertiesList());

                //calculate average color and set as segment color
                int R = (v1Color.getRed() + v2Color.getRed())/2;
                int G = (v1Color.getGreen() + v2Color.getGreen())/2;
                int B = (v1Color.getBlue() + v2Color.getBlue())/2;
                canvas.setColor(new Color(R, G, B));

                //calculate average thickness and set as segment thickness
                Float segmentThickness = (thicknessV1 + thicknessV2)/2;
                Stroke segmentStroke = new BasicStroke(segmentThickness);
                canvas.setStroke(segmentStroke);

                //draw segments with averaged color and thickness
                Point2D startPoint = new Point2D.Double(start_Point.getX(), start_Point.getY());
                Point2D endPoint = new Point2D.Double(end_Point.getX(), end_Point.getY());

                Line2D line = new Line2D.Double(startPoint,endPoint);

                canvas.draw(line);
                canvas.fill(line);*/
            }


            //Draw vertices with appropriate color and thickness
            ArrayList<Vertex> vertices2 = new ArrayList<Vertex>();

            for (int i = 1; i < vertices.size(); i++){
                vertices2.add(vertices.get(i));
            }

            ArrayList<Vertex> duplicates = new ArrayList<Vertex>();

            for (Vertex v1: vertices){
                duplicates.add(v1);

                for (Vertex v2: vertices2) {
                    if ((v1.getX() == v2.getX()) && (v1.getY() == v2.getY()) ){
                        duplicates.add(v2);

                        if (duplicates.size() == 4){
                            Float thicknessV1 = extractThickness(duplicates.get(0).getPropertiesList());
                            Float thicknessV2 = extractThickness(duplicates.get(1).getPropertiesList());
                            Float thicknessV3 = extractThickness(duplicates.get(2).getPropertiesList());
                            Float thicknessV4 = extractThickness(duplicates.get(3).getPropertiesList());

                            Color colorV1 = extractColor(duplicates.get(0).getPropertiesList());
                            Color colorV2 = extractColor(duplicates.get(1).getPropertiesList());
                            Color colorV3 = extractColor(duplicates.get(2).getPropertiesList());
                            Color colorV4 = extractColor(duplicates.get(3).getPropertiesList());

                            int R = (colorV1.getRed() + colorV2.getRed() + colorV3.getRed() + colorV4.getRed())/4;
                            int G = (colorV1.getGreen() + colorV2.getGreen() + colorV3.getGreen() + colorV4.getGreen())/4;
                            int B = (colorV1.getBlue() + colorV2.getBlue() + colorV3.getBlue() + colorV4.getBlue())/4;
                            canvas.setColor(new Color(R, G, B));

                            Float vertexThickness = (thicknessV1 + thicknessV2 + thicknessV3 + thicknessV4)/4;

                            Ellipse2D point = new Ellipse2D.Double(v2.getX(), v2.getY(), vertexThickness, vertexThickness);
                            canvas.fill(point);

                            duplicates.clear();
                        }

                    }

                }
            }






        }
        
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }

    private Float extractThickness(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("thickness")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return 3.0f;

        Float thicknessVal= Float.parseFloat(val);

        return thicknessVal;
    }

}
