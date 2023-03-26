package ca.team50.water;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.GenerationUtils;
import ca.team50.shapes.IslandShape;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.ArrayList;

public class LakeGenerator {

    private ArrayList<ArrayList<Polygons>> lakes = new ArrayList<>();

    /**
     * Generate a list of lakes (each with a list of polygons) that make up lakes on an island
     * @param mesh the PolyMesh object containing all canvas polygons
     * @param islandShape the shape of the island used in the island generation method (same as the shape being used for island generation)
     * @param maxLakes the maximum amount of lakes to generate
     * @param maxRadius the maximum size a single lake can be
     * @param thresholdAltitude all polygons with the altitude property LESS THAN OR EQUAL to this value will be considered for lake generation
     * @param seed the seed for noise generator
     * @return a LakeGenerator object containing a list (ArrayList) of all lakes. This list contains sublists (ArrayList) that hold all polygons objects for a given lake
     * Note that when the term "maximum" is specified, this does NOT mean the generator will generate all lakes. Polygons that do not have an altitude property are assumed to have a value of 0.
     */
    public LakeGenerator(PolyMesh<Polygons> mesh, IslandShape islandShape, int maxLakes, double maxRadius, double thresholdAltitude, long seed) {

        ArrayList<Polygons> lakeCandidates = new ArrayList<>();

        // Find polygons that are under the threshold to be considered for lake generation
        for(Polygons currentPolygon : mesh) {

            if (islandShape.isVertexInside(currentPolygon.getCentroid()) && getElevation(currentPolygon) <= thresholdAltitude) {
                lakeCandidates.add(currentPolygon);
            }

        }

        // Attempt to generate maxLakes lakes
        for (int curIter = 0; curIter < maxLakes; curIter++) {

            // Determine center of lake
            Polygons lakeCenter = null;
            Double maxNoise = null;

            // Find the polygon with the most noise, this will be the center of the lake
            for (Polygons currentCandidate : lakeCandidates) {

                double noiseLevel = GenerationUtils.worleyNoise(seed,currentCandidate.getCentroid());

                if (maxNoise == null) {
                    lakeCenter = currentCandidate;
                    maxNoise = noiseLevel;
                    continue;
                }

                if (noiseLevel > maxNoise) {
                    lakeCenter = currentCandidate;
                    maxNoise = noiseLevel;
                }

            }

            // If lake center is still null, this means no more candidates for lakes exist thus, stop generation
            if (lakeCenter == null) {
                break;
            }

            ArrayList<Polygons> lake = new ArrayList<>();
            lake.add(lakeCenter);

            Structs.Vertex lakeCenterCentroid = lakeCenter.getCentroid();

            // Once the lake center has been found
            // Generate a lake from this center
            // To do so, loop through lake candidates, check if they are within the radius of the center based off of lake size
            // Check said polygons neighbours if they have a neighbour that is in the lake already
            // If they do, add them to lake list
            // If not, come back to the polygon later
            // Keep doing this until we loop through the entire candidate list once and no actions are taken

            boolean actionTaken;

            do {
                actionTaken = false;
                for (Polygons candidate : lakeCandidates) {

                    if (!lake.contains(candidate)) {

                        if (getDistanceToCenter(lakeCenterCentroid,candidate.getCentroid()) <= maxRadius) {

                            for (Polygons inLakePoly : lake) {

                                if (isNeighbour(mesh,candidate,inLakePoly)) {
                                    lake.add(candidate);
                                    actionTaken = true;
                                    break;
                                }

                            }

                        }
                    }


                }
            } while (actionTaken);

            // Add the created lake to the lakes list
            this.lakes.add(lake);

            // Remove all lake candidates that are already in a lake
            for (ArrayList<Polygons> currentLake : lakes) {

                for (Polygons candidateToDelete : currentLake) {

                    lakeCandidates.remove(candidateToDelete);

                }

            }

        }

    }

    // Method to get lakes
    /**
     * Gets the list of lakes
     * @return an ArrayList object containing sublists of groups of polygons that make up a given lake
     * Note this list does not have a predefined length, it can be empty (i.e. the generator did not generate a lake)
     */
    public ArrayList<ArrayList<Polygons>> getLakes() {
        return this.lakes;
    }

    /**
     * Check if a polygon is classified as apart of a lake
     * @return an boolean value (true is yes, false otherwise)
     */
    public boolean isPolygonApartOfLake(Polygons polygonToTest) {

        for (ArrayList<Polygons> lake : this.lakes) {
            for (Polygons lakePolygon : lake) {
                if (lakePolygon == polygonToTest) {
                    return true;
                }
            }
        }

        return false;

    }



    // PolyMesh provides an index based isNeighbour method, this takes in two specific polygons that were in mesh and determines if they are neighbours
    private boolean isNeighbour(PolyMesh<Polygons> mesh, Polygons polygon1, Polygons polygon2) {

        int polygon1Index;
        int polygon2Index;

        for (polygon1Index = 0; polygon1Index < mesh.size(); polygon1Index++) {

            Polygons testPoly = mesh.get(polygon1Index);
            if (testPoly == polygon1) {
                break;
            }

        }

        for (polygon2Index = 0; polygon2Index < mesh.size(); polygon2Index++) {

            Polygons testPoly = mesh.get(polygon2Index);
            if (testPoly == polygon2) {
                break;
            }

        }

        return mesh.isNeighbor(polygon1Index,polygon2Index) ? true : false;

    }



    // Method to get the distance between the lake center and given polygon centroid
    private double getDistanceToCenter(Structs.Vertex center, Structs.Vertex pointToCheck) {
        double distance = Math.sqrt(Math.pow((pointToCheck.getX()-center.getX()),2) + Math.pow((pointToCheck.getY()-center.getY()),2));
        return distance;
    }

    // Property extractor for getting polygon elevation
    private double getElevation(Polygons polygon) {

        String val = null;
        for (Structs.Property p : polygon.getCentroid().getPropertiesList()) {
            if (p.getKey().equals("altitude")) {
                val = p.getValue();
            }
        }
        if (val == null) {
            return 0;
        }

        double alphaVal = Double.parseDouble(val);

        return alphaVal;
    }

}
