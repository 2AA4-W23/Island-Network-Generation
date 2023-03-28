package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.team50.Tiles.*;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Circle;
import ca.team50.shapes.IslandShape;

// MVP Island
public class Lagoon implements IslandGenerable {

    private static final double oceanRadius = 350;
    private static final double lagoonRadius = 200;

    public void generateIsland(PolyMesh<Polygons> mesh) {


        // Find center of canvas, the center will act as the center of the lagoon island
        Vertex center = CanvasUtils.getCenter(mesh);

        // Get all tiles needed for generation
        TileType beach = new BeachTile();
        TileType lagoon = new LagoonTile();
        TileType ocean = new OceanTile();
        TileType land = new LandTile();

        // Get all island shapes needed for land (or rather borders for what makes up the island)
        IslandShape lagoonCircle = new Circle(center,lagoonRadius);
        IslandShape oceanCircle = new Circle(center,oceanRadius);

        // Loop through all polygons
        // Get their centroids and apply appropriate colour based on where they are located relative to center and specified radius
        for (Polygons currentPolygon : mesh) {

            // Clean properties (i.e. set defaults)
            currentPolygon.cleanProperties();

            Vertex centroid = currentPolygon.getCentroid();

            // Check where the polygon is located and colour it accordingly
            if (lagoonCircle.isVertexInside(centroid)) {
                currentPolygon.unifyColor(lagoon.getTileColour());
            } else if (!lagoonCircle.isVertexInside(centroid) && oceanCircle.isVertexInside(centroid)) {
                currentPolygon.unifyColor(land.getTileColour());
            } else if (!lagoonCircle.isVertexInside(centroid) && !oceanCircle.isVertexInside(centroid)) {
                currentPolygon.unifyColor(ocean.getTileColour());
            }

        }

        // For beach colouring, check if the polygon is a neighbour to some water and check if it exists as a land piece
        for (int index = 0; index < mesh.size(); index++) {

           for (int index1 = 0; index1 < mesh.size(); index1++) {

               if (index != index1 && mesh.isNeighbor(index,index1)) {

                   Polygons polygon1 = mesh.get(index);
                   Polygons polygon2 = mesh.get(index1);

                   Vertex centroid1 = polygon1.getCentroid();
                   Vertex centroid2 = polygon2.getCentroid();

                   // Check if polygon 1 is classified as water and polygon 2 is classified as land
                   if ((lagoonCircle.isVertexInside(centroid1) || !oceanCircle.isVertexInside(centroid1)) && (!lagoonCircle.isVertexInside(centroid2) && oceanCircle.isVertexInside(centroid2))) {
                       // Then polygon 2 should be of beach type
                       polygon2.unifyColor(beach.getTileColour());

                       // Check other way around
                   } else if ((lagoonCircle.isVertexInside(centroid2) || !oceanCircle.isVertexInside(centroid2)) && (!lagoonCircle.isVertexInside(centroid1) && oceanCircle.isVertexInside(centroid1))) {
                       polygon1.unifyColor(beach.getTileColour());
                   }


               }

           }
        }


    }

}
