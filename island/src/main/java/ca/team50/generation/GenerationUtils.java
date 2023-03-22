package ca.team50.generation;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator;

public class GenerationUtils {

    // Generates a value from -1 to 1 based on a seed and a given position
    /**
     * Generate a value based on a Worley noise map
     * @param seed the seed to control the noise
     * @param position the position to extract a noise value from (as a Vertex)
     * @return a double containing a value between -1 and 1 based on the position input
     */
    public static double worleyNoise(long seed, Structs.Vertex position) {

        // Create a worley noise generator and build a noise generator based off the given seed
        WorleyNoiseGenerator wolNoise = WorleyNoiseGenerator.newBuilder().setSeed(seed).build();

        // Return the value at the given point on the noise map generated
        return wolNoise.evaluateNoise(position.getX(),position.getY());
    }

}
