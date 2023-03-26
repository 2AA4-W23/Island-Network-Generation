package ca.team50.Tiles.Deserts;

import ca.team50.Tiles.TileType;

import java.util.ArrayList;

public class DesertsUtils {

    /**
     * Get all tiles within the deserts biome
     * @return an ArrayList containing all tiles from deserts biome
     */
    public static ArrayList<TileType> getAllTiles() {

        ArrayList<TileType> tileTypeArrayList = new ArrayList<>();

        TileType tile = new BadlandsTile();
        tileTypeArrayList.add(tile);
        tile = new GrasslandTile();
        tileTypeArrayList.add(tile);
        tile = new OasisTile();
        tileTypeArrayList.add(tile);
        tile = new SagebrushTile();
        tileTypeArrayList.add(tile);
        tile = new SandTile();
        tileTypeArrayList.add(tile);

        return tileTypeArrayList;

    }

}
