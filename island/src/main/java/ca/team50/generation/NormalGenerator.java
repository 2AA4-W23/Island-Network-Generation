package ca.team50.generation;

import ca.team50.Tiles.TileType;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.IslandShape;
import ca.team50.specification.CLInterface;
import ca.team50.specification.CLInterfaceIsland;

import java.util.List;

public class NormalGenerator implements IslandGenerable {

    private CLInterfaceIsland specification;

    public NormalGenerator(CLInterfaceIsland islandSpecification) {
        this.specification = islandSpecification;
    }


    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        // Tiles
        // Island Shape
        // Any generators





    }
}
