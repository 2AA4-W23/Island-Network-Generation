package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.*;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Irregular;
import ca.team50.shapes.IslandShape;

public class RandomIsland implements IslandGenerable {


    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        // Get all tiles needed for generation
        TileType ocean = new OceanTile();
        TileType land = new LandTile();

        IslandShape irreg = new Irregular(1928,-0.2,CanvasUtils.getCenter(mesh),CanvasUtils.getMaxPoint(mesh));

        // Loop through all polygons
        // Get their centroids and apply appropriate colour based on where they are located relative to center and specified radius
        for (Polygons currentPolygon : mesh) {

            // Clean properties (i.e. set defaults)
            currentPolygon.cleanProperties();

            Structs.Vertex centroid = currentPolygon.getCentroid();

            // Check where the polygon is located and colour it accordingly
            if (irreg.isVertexInside(centroid)) {
                currentPolygon.unifyColor(land.getTileColour());
            } else {
                currentPolygon.unifyColor(ocean.getTileColour());
            }

        }

    }
}
