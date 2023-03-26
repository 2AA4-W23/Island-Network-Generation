package ca.team50.water;

import java.util.ArrayList;
import java.util.Random;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.team50.Tiles.TileType;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.IslandGenerable;
import ca.team50.shapes.IslandShape;
import ca.team50.Tiles.LandTile;
import ca.team50.Tiles.OceanTile;

public class AquiferGenerator {

    private int numAquifers;

    private ArrayList<Polygons> aquifers;

    public AquiferGenerator() {
        this.numAquifers = 0;
        this.aquifers = new ArrayList<Polygons>();
    }

    // Setter method for numAquifers
    public void setNumAquifers(int numAquifers) {
        this.numAquifers = numAquifers;
    }

    public void generateAquifers(PolyMesh<Polygons> mesh, IslandShape islandShape) {

        ArrayList<Polygons> aquifers = new ArrayList<>();

        IslandShape island = islandShape;

        // Create LandTile and OceanTile objects for coloring the polygons
        TileType land = new LandTile();
        TileType ocean = new OceanTile();

        // Loop through the polygons in the PolyMesh
        for (Polygons polygon : mesh) {

            // Get the centroid of the polygon
            Vertex centroid = polygon.getCentroid();

            // Check if the polygon is inside the island
            if (island.isVertexInside(centroid)) {

                // Color the polygon with the land tile color
                polygon.unifyColor(land.getTileColour());

                // Generate a random number between 0 and 1 to determine if this polygon will be an aquifer
                Random rand = new Random();
                int randomNum = rand.nextInt(2);

                // Ensure randomly selected land polygons are created only if more aquifers are necessary
                if (randomNum == 1 && numAquifers > 0) {
                    polygon.aquiferExists("True");
                    aquifers.add(polygon);
                    numAquifers--;
                }
            }

            else {
                // Color the polygon with the ocean tile color
                polygon.unifyColor(ocean.getTileColour());
            }

        }
    }

    public ArrayList<Polygons> getAquifers() {
        return this.aquifers;
    }
}