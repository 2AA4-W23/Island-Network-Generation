package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class GraphicRenderer {

    private static final int THICKNESS = 3;
    public void render(Mesh aMesh, Graphics2D canvas) {
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);
        for (Vertex v: aMesh.getVerticesList()) {
            double centre_x = v.getX() - (THICKNESS/2.0d);
            double centre_y = v.getY() - (THICKNESS/2.0d);
            Color old = canvas.getColor();
            canvas.setColor(extractColor(v.getPropertiesList()));
            Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
            canvas.fill(point);
            canvas.setColor(old);
        }

        //iterate through and draw each line segment
        for (Segment s : aMesh.getSegmentsList()) {
            canvas.setColor(extractColor(s.getPropertiesList()));
            String startVertex = null;
            String endVertex = null;

            for(Property p: s.getPropertiesList()) {
                if (p.getKey().equals("start_vertex")) {
                    startVertex = p.getValue();
                }
                if (p.getKey().equals("end_vertex")) {
                    endVertex = p.getValue();
                }
            }
            
            String[] rawStart = startVertex.split(",");
            double x_Start = Double.parseDouble(rawStart[0]);
            double y_Start = Double.parseDouble(rawStart[1]);

            String[] rawEnd = endVertex.split(",");
            double x_End = Double.parseDouble(rawEnd[0]);
            double y_End = Double.parseDouble(rawEnd[1]);

            //set the start and end poitns of each line segment from the properties
            Point2D startPoint = new Point2D.Double(x_Start,y_Start);
            Point2D endPoint = new Point2D.Double(x_End,y_End);
            
            Line2D line = new Line2D.Double(startPoint,endPoint);

            canvas.draw(line);
            canvas.fill(line);
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

}
