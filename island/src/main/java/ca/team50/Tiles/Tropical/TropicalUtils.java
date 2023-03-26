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
    public static TileType getTileFormProperty(double polygonAltitude, double polygonHumidity) {
        if (( polygonAltitude < 0.2 && 0.0 <= polygonAltitude) && ( polygonHumidity < 0.9 && 0.5 <= polygonHumidity)) {
            return new RainforestTile();
        } else if(( polygonAltitude < 0.4  && 0.2 <= polygonAltitude) && ( polygonHumidity <= 1.0 && 0.5 <= polygonHumidity)) {
            return new SwampTile();
        } else if(( polygonAltitude < 0.6 && 0.4 <= polygonAltitude) && ( polygonHumidity < 1.0 && 0.4 <= polygonHumidity)) {
            return new MangroveTile();
        } else if (( polygonAltitude < 0.8 && 0.6 <= polygonAltitude) || ( polygonHumidity < 0.4 && 0.0 <= polygonHumidity)) {
            return new SavannahTile();
        }
        else
            return new CloudforestTile();
    }

}
