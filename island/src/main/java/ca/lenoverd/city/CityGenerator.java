package ca.lenoverd.city;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.Cities.CityUtils;
import ca.team50.Tiles.TileType;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.GenerationUtils;
import ca.team50.shapes.IslandShape;

import java.util.ArrayList;
import java.util.HashMap;

public class CityGenerator {

    private HashMap<Polygons,TileType> cities = new HashMap<>();

    static long seed = 1234;


    public CityGenerator(PolyMesh<Polygons> mesh, int maxNumberOfCities, IslandShape islandShape) {

        int curIter = 0;

        while(curIter < maxNumberOfCities) {

            // Loop through all polygons
            for (Polygons curPoly : mesh) {

                // Get centroid
                Structs.Vertex centroid = curPoly.getCentroid();

                // Check if the polygon is not already a designated city and that it exists within the island
                if (!cities.containsKey(curPoly) && islandShape.isVertexInside(centroid)) {

                    // Get the chance of becoming a specific city
                    double chanceValue = GenerationUtils.worleyNoise1DScaled(seed,centroid.getX(),0,1);

                    // Check if this polygon will be a city
                    TileType cityType = CityUtils.getTileFromProperty(chanceValue);

                    if (cityType != null) {

                        // Add polygon and corresponding city tile to hashmap
                        cities.put(curPoly,cityType);
                        break;

                    }

                }

            }

            curIter++;

        }

    }


}
