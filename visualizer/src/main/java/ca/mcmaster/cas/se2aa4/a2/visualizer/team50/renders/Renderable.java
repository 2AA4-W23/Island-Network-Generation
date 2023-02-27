package ca.mcmaster.cas.se2aa4.a2.visualizer.team50.renders;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.awt.*;

public interface Renderable {
    public void render(PolyMesh<? extends Polygons> polygons, Graphics2D canvas); // Any class which is readable must specify this method

}
