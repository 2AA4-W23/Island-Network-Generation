package ca.team50.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.generation.GenerationUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.Vector;

public class Irregular implements IslandShape {

    private long seed;
    private double threshold;
    private Structs.Vertex max;
    private Structs.Vertex center;

    /**
     * Generate a random irregular shape based off of noise
     * @param seed the seed for noise generation
     * @param threshold the threshold in which the method isVertexInside should return true for after noise calculation of a given position
     * @param centerOfIsland the center of the island on canvas
     * @param max the maximum value a point can be from the center of the island
     * @return an Irregular shape object
     */
    public Irregular(long seed, double threshold, Structs.Vertex centerOfIsland, Structs.Vertex max) {
        this.seed = seed;
        this.threshold = threshold;
        this.center = centerOfIsland;
        this.max = max;
    }

    /**
     * Test if any given point exists within the Irregular shape
     * @param vertexToCheck the position (as a vertex) to check
     * @return a boolean value, true if the point exists within the shape, false otherwise
     * Note that within the shape means the noise level of a particular point is beyond the threshold specified
     * This value is calculated based on the noise generated and how far the given point is from the center of the island
     */
    @Override
    public boolean isVertexInside(Structs.Vertex vertexToCheck) {

        // Get noise level via checking the vertex position against noise map
        double noiseLevel = GenerationUtils.worleyNoise(seed, vertexToCheck);

        // The noise map will have a lot of variation and for an island, we want to keep most of the island in one place
        // Thus we can use the distance from the center of the island as a "gradient" and subtract it's normalized value for any given point by the noise level
        // This will leave us with a more condensed noise map in a specific center
        double total = noiseLevel-normalizeNumber(getDistanceToCenter(this.center,vertexToCheck),0,getDistanceToCenter(center,max));

        return (total>threshold);
    }

    // Method to get the distance between the island center and given point
    private static double getDistanceToCenter(Structs.Vertex center, Structs.Vertex pointToCheck) {
        double distance = Math.sqrt(Math.pow((pointToCheck.getX()-center.getX()),2) + Math.pow((pointToCheck.getY()-center.getY()),2));
        return distance;
    }

    // Since the radius can vary between canvases, we need to normalize the distance number between 0 and 1
    private double normalizeNumber(double numToNormalize, double minNum, double maxNum) {
        return (((numToNormalize - minNum) / (maxNum - minNum)));
    }

}
