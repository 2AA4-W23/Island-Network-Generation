package ca.team50.generation;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

public interface IslandGenerable {

    public void generateIsland(PolyMesh<Polygons> mesh);

}
