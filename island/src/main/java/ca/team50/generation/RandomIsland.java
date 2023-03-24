package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.*;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Elipse;
import ca.team50.shapes.Irregular;
import ca.team50.shapes.IslandShape;
import ca.team50.water.LakeGenerator;

import java.util.ArrayList;

public class RandomIsland implements IslandGenerable {


    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        // Get all tiles needed for generation
        TileType ocean = new OceanTile();
        TileType land = new LandTile();
        TileType lake = new LagoonTile();

        // IslandShape irreg = new Irregular(1928,-0.2,CanvasUtils.getCenter(mesh),CanvasUtils.getMaxPoint(mesh));

        // Create an elipse shape
        IslandShape irreg = new Elipse(CanvasUtils.getCenter(mesh),2000,3000,0.2);

        // This same elipse shape is then used is the lake generator
        LakeGenerator lakeGen = new LakeGenerator(mesh,irreg,3,250,0.6,1928);

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

        // Colour all polygons that were defined as lakes
        // Note this is a 2D array list so we have to double for loop
        for (ArrayList<Polygons> lakeList : lakeGen.getLakes()) {
            for (Polygons curPoly : lakeList) {

                curPoly.unifyColor(lake.getTileColour());

            }
        }

    }
}
