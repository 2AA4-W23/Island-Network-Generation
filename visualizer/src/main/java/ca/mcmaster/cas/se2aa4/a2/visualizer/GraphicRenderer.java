package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.visualizer.team50.renders.DebugRender;
import ca.mcmaster.cas.se2aa4.a2.visualizer.team50.renders.NormalRender;
import ca.mcmaster.cas.se2aa4.a2.visualizer.team50.renders.Renderable;
import ca.team50.adt.PolyMesh;
import ca.mcmaster.cas.se2aa4.a2.visualizer.team50.properties.*;

import java.awt.*;
import java.util.ArrayList;

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

        // Setup canvas
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);

        // Define static render type
        Renderable renderToUse;

        // Check which renderer visualizer should be using
        if (this.debugMode) {
            renderToUse = new DebugRender();
            renderToUse.render(polygons,canvas);
        } else {
            renderToUse = new NormalRender();
            renderToUse.render(polygons,canvas);
        }
        
    }

}
