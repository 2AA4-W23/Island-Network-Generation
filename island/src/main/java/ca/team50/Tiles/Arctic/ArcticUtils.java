package ca.team50.Tiles.Arctic;

import ca.team50.Tiles.TileType;
import ca.team50.Tiles.Tropical.*;

import java.util.ArrayList;

public class ArcticUtils {



    /**
     * Get a specific tile within the Arctic biome given altitude
     * @return a TileType object corresponding to parameter
     */
    public static TileType getTileFormProperty(double polygonAltitude, double polygonHumidity) {
        if (( polygonAltitude < 0.2 && 0 <= polygonAltitude) && (polygonHumidity <= 1.0 && 0.1 <= polygonAltitude))
            return new BorealTile();
        else if(( polygonAltitude < 0.4 && 0.2 <= polygonAltitude) && (polygonHumidity < 0.9 && 0.3 <= polygonAltitude))
            return new TaigaTile();
        else if(( polygonAltitude < 0.6 && 0.8 <= polygonAltitude) && (polygonHumidity < 0.7 && 0.2 <= polygonAltitude))
            return new TundraTile();
        else if(( polygonAltitude < 0.8 && 0.6 <= polygonAltitude) && (polygonHumidity <= 0.6 && 0.0 <= polygonAltitude))
            return new ColddesertTile();
        else
            return new IcecapTile();
    }

}
