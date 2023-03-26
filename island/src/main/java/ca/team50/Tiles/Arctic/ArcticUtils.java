package ca.team50.Tiles.Arctic;

import ca.team50.Tiles.TileType;
import ca.team50.Tiles.Tropical.*;

import java.util.ArrayList;

public class ArcticUtils {

    /**
     * Get all tiles within the Arctic biome
     * @return an ArrayList containing all tiles from Arctic biome
     */
    public static ArrayList<TileType> getAllTiles() {

        ArrayList<TileType> tileTypeArrayList = new ArrayList<>();

        TileType tile = new BorealTile();
        tileTypeArrayList.add(tile);
        tile = new ColddesertTile();
        tileTypeArrayList.add(tile);
        tile = new IcecapTile();
        tileTypeArrayList.add(tile);
        tile = new TaigaTile();
        tileTypeArrayList.add(tile);
        tile = new TundraTile();
        tileTypeArrayList.add(tile);

        return tileTypeArrayList;

    }

}
