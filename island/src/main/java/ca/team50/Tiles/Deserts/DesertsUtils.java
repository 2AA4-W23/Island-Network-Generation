package ca.team50.Tiles.Deserts;

import ca.team50.Tiles.Arctic.*;
import ca.team50.Tiles.TileType;

import java.util.ArrayList;

public class DesertsUtils {

    /**
     * Get a specific tile within the Desert biome given altitude
     * @return a TileType object corresponding to parameter
     */
    public static TileType getTileFormProperty(double polygonAltitude, double polygonHumidity) {
        if (( polygonAltitude < 0.2 && 0 <= polygonAltitude) && ( polygonHumidity < 0.2 && 0.0 <= polygonHumidity)){
            return new SandTile();
        } else if((polygonAltitude < 0.4 && 0.2 <= polygonAltitude) && ( polygonHumidity < 0.2 && 0 <= polygonHumidity)){
            return new SagebrushTile();
        } else if((polygonAltitude < 0.6 && 0.4 <= polygonAltitude) && ( polygonHumidity <= 1.0 && 0.6 <= polygonHumidity)){
            return new OasisTile();
        } else if((polygonAltitude < 0.8 && 0.6 <= polygonAltitude) && ( polygonHumidity < 0.7 && 0.0 <= polygonHumidity)){
            return new GrasslandTile();
        }
        else
            return new BadlandsTile();
    }

}
