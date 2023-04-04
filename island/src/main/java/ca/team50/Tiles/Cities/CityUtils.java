package ca.team50.Tiles.Cities;

import ca.team50.Tiles.TileType;
import ca.team50.Tiles.Tropical.*;

public class CityUtils {

    static double maxDensityThreshold = 0.80;
    static double middleDensityThreshold = 0.68;
    static double lowDensityThreshold = 0.58;
    static double minDensityThreshold = 0.5;

    /**
     * Get a specific tile within a City biome given a specific relative population size
     * @return a TileType object corresponding to parameter
     */
    public static TileType getTileFromProperty(double population) {

        if (population >= minDensityThreshold) {

            if (population >= maxDensityThreshold) {

                return new MetropolisTile();

            } else if (population >= middleDensityThreshold) {

                return new RegiopolisTile();

            } else if (population >= lowDensityThreshold) {

                return new SmallCityTile();

            }

            return new TownTile();

        }

        return null;

    }



}
