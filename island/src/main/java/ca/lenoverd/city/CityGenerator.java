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

    // TEMP SEED FOR TESTING
    static long seed = 1234;

    /**
     * Generate a list of cities (and their types) for a given mesh
     * @param mesh the PolyMesh object containing all canvas polygons
     * @param islandShape the shape of the island used in the island generation method (same as the shape being used for island generation)
     * @param maxNumberOfCities the maximum amount of cities to generate
     * @return a CityGenerator object which contains a list of polygons which are designated as cities.
     * @Note That when the term "maximum" is specified, this does NOT mean the generator will generate all cities.
     */
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


    /**
     * Check if a given vertex or centroid from a polygon is a city
     * @param vertex any vertex/centroid of the given polygon
     * @return true if the polygon is a city, false otherwise
     */
    public boolean isVertexACity(Structs.Vertex vertex) {

        for (Polygons curPoly : cities.keySet()) {

            // The first vertex in a polygon is considered the entry point to a city
            Structs.Vertex firstVert = curPoly.getVerticesList().get(0);

            if (firstVert.getX() == vertex.getX() && firstVert.getY() == vertex.getY()) {

                return true;

            }

            Structs.Vertex curPolyCentroid = curPoly.getCentroid();

            if (curPolyCentroid.getX() == vertex.getX() && curPolyCentroid.getY() == vertex.getY()) {

                return true;

            }

        }

        return false;

    }

    /**
     * Get the type of city (as a Tile) from a polygon centroid
     * @param centroid the centroid of the given polygon
     * @return a TileType object which contains a specific city colour
     * @Note if the polygon centroid is not a city, this method will return null
     */
    public TileType getCityType(Structs.Vertex centroid) {

        for (Polygons curPoly : cities.keySet()) {

            Structs.Vertex curPolyCentroid = curPoly.getCentroid();

            if (curPolyCentroid.getX() == centroid.getX() && curPolyCentroid.getY() == centroid.getY()) {

                return cities.get(curPoly);

            }

        }

        return null;

    }

}
