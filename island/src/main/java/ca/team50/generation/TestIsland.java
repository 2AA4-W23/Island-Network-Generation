package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.LandTile;
import ca.team50.Tiles.OceanTile;
import ca.team50.Tiles.TileType;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Elipse;
import ca.team50.shapes.Irregular;
import ca.team50.shapes.IslandShape;

public class TestIsland implements IslandGenerable {

    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        // Get all tiles needed for generation (Command line argument : choose specific biome)
        TileType ocean = new OceanTile();
        TileType land = new LandTile();

        //Commandline argument: choose shape
        double height = 300;
        double width = 500;
        IslandShape elipse = new Elipse(CanvasUtils.getCenter(mesh),height,width,1.2);

        //Apply altimeric profile to entire mesh



        // Loop through all polygons
        // Get their centroids and apply appropriate colour based on where they are located relative to center and specified radius
        for (Polygons currentPolygon : mesh) {

            // Clean properties (i.e. set defaults)
            currentPolygon.cleanProperties();

            Structs.Vertex centroid = currentPolygon.getCentroid();

            // Check where the polygon is located and colour it accordingly
            if (elipse.isVertexInside(centroid)) {


                currentPolygon.unifyColor(land.getTileColour());
            } else {
                currentPolygon.unifyColor(ocean.getTileColour());
            }

        }

    }
}
