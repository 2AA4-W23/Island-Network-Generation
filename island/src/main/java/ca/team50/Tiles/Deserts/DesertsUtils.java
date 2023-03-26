package ca.team50.Tiles.Deserts;

import ca.team50.Tiles.Arctic.*;
import ca.team50.Tiles.TileType;

import java.util.ArrayList;

public class DesertsUtils {

    /**
     * Get a specific tile within the Desert biome given altitude
     * @return a TileType object corresponding to parameter
     */
    public static TileType getTileFormProperty(double polygonAltitude) {
        if ( polygonAltitude < 0.2 && 0 <= polygonAltitude) {
            return new SandTile();
        } else if(polygonAltitude < 0.4 && 0.2 <= polygonAltitude) {
            return new SagebrushTile();
        } else if(polygonAltitude < 0.6 && 0.4 <= polygonAltitude) {
            return new OasisTile();
        } else if(polygonAltitude < 0.7 && 0.6 <= polygonAltitude) {
            return new BadlandsTile();
        }
        else
            return new GrasslandTile();
    }

}
