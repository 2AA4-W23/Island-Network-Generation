package ca.team50.generators;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

public interface Generable {

    public PolyMesh<Polygons> generate(int width, int height, int numOfPolygons);

}
