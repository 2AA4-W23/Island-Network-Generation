package ca.team50.Tiles.Tropical;

import ca.team50.Tiles.Arctic.ColddesertTile;
import ca.team50.Tiles.Deserts.*;
import ca.team50.Tiles.TileType;

import java.util.ArrayList;

public class TropicalUtils {

    /**
     * Get a specific tile within the Tropical biome given altitude
     * @return a TileType object corresponding to parameter
     */
    public static TileType getTileFormProperty(double polygonAltitude) {
        if ( polygonAltitude < 0.2 && 0 <= polygonAltitude) {
            return new RainforestTile();
        } else if(polygonAltitude < 0.4 && 0.2 <= polygonAltitude) {
            return new SwampTile();
        } else if(polygonAltitude < 0.6 && 0.4 <= polygonAltitude) {
            return new MangroveTile();
        } else if (polygonAltitude < 0.7 && 0.6 <= polygonAltitude) {
            return new SavannahTile();
        }
        else
            return new CloudforestTile();
    }

}
