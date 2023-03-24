package ca.team50.generation;

import java.util.Random;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.team50.Tiles.TileType;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Elipse;
import ca.team50.shapes.IslandShape;
import ca.team50.Tiles.LandTile;
import ca.team50.Tiles.OceanTile;

public class Aquifer implements IslandGenerable {

    private int numAquifers;

    public Aquifer() {
        this.numAquifers = 1;
    }

    // Setter method for numAquifers
    public void setNumAquifers(int numAquifers) {
        this.numAquifers = numAquifers;
    }

    public void generateIsland(PolyMesh<Polygons> mesh) {

        // Find center of canvas, the center will act as the center of elipse
        Vertex center = CanvasUtils.getCenter(mesh);

        // Initialize the elipse shape with the center vertex, height, width, and rotation
        Elipse elipse = new Elipse(center, 10, 20, 0);

        // Create an elipse shape for the island
        IslandShape elpiseIsland = elipse;

        // Create LandTile and OceanTile objects for coloring the polygons
        TileType land = new LandTile();
        TileType ocean = new OceanTile();

        // Loop through the polygons in the PolyMesh
        for (Polygons polygon : mesh) {

            // Get the centroid of the polygon
            Vertex centroid = polygon.getCentroid();

            // Check if the polygon is inside the island
            if (elpiseIsland.isVertexInside(centroid)) {

                // Color the polygon with the land tile color
                polygon.unifyColor(land.getTileColour());

                // Generate a random number between 0 and 1 to determine if this polygon will be an aquifer
                Random rand = new Random();
                int randomNum = rand.nextInt(2);

                // Ensure randomly selected land polygons are not aquifers and if more aquifers are necessary
                if (randomNum == 1 && numAquifers > 0) {
                    polygon.aquiferExists("True");
                    numAquifers--;
                }
            }

            else {
                // Color the polygon with the ocean tile color
                polygon.unifyColor(ocean.getTileColour());
            }

        }
    }
}
