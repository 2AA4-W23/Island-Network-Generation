package ca.team50.Tiles.Tropical;

import ca.team50.Tiles.TileType;

import java.util.ArrayList;

public class TropicalUtils {

    /**
     * Get all tiles within the tropical biome
     * @return an ArrayList containing all tiles from Tropical biome
     */
    public static ArrayList<TileType> getAllTiles() {

        ArrayList<TileType> tileTypeArrayList = new ArrayList<>();

        TileType tile = new CloudforestTile();
        tileTypeArrayList.add(tile);
        tile = new MangroveTile();
        tileTypeArrayList.add(tile);
        tile = new RainforestTile();
        tileTypeArrayList.add(tile);
        tile = new SavannahTile();
        tileTypeArrayList.add(tile);
        tile = new SwampTile();
        tileTypeArrayList.add(tile);

        return tileTypeArrayList;

    }

}
