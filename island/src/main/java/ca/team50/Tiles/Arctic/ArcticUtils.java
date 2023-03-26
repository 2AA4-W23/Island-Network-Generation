package ca.team50.Tiles.Arctic;

import ca.team50.Tiles.TileType;
import ca.team50.Tiles.Tropical.*;

import java.util.ArrayList;

public class ArcticUtils {



    /**
     * Get a specific tile within the Arctic biome given altitude
     * @return a TileType object corresponding to parameter
     */
    public static TileType getTileFormProperty(double polygonAltitude) {
        if ( polygonAltitude < 0.2 && 0 <= polygonAltitude)
            return new BorealTile();
        else if(polygonAltitude < 0.4 && 0.2 <= polygonAltitude)
            return new TaigaTile();
        else if(polygonAltitude < 0.6 && 0.4 <= polygonAltitude)
            return new TundraTile();
        else if(polygonAltitude < 0.8 && 0.6 <= polygonAltitude)
            return new ColddesertTile();
        else
            return new IcecapTile();
    }

}
