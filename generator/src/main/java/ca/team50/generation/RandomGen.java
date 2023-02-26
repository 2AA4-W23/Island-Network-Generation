package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import org.locationtech.jts.geom.Coordinate;

import java.util.*;

public class RandomGen {

    /**
     * Generates an Arraylist of Coordinate (JTS) objects, each containing a unique x and y position
     * @param maxX the maximum value x can be (0 to maxX) (both values are inclusive)
     * @param maxY the maximum value y can be (0 to maxY) (both values are inclusive)
     * @param numOfCoords the number of coordinates to be generated
     * @exception IllegalArgumentException if maxX or maxY are less than or equal to 0. If numOfCoords exceeds the maximum possible number of unique coordinates (that is maxX*maxY)
     * @return an Arraylist of Point objects containing unique x and y positions
     */
    public static ArrayList<Coordinate> genCoords(int maxX, int maxY, int numOfCoords) {

        // Check for illegal arguments
        if (maxX <= 0) {
            throw new IllegalArgumentException("maxX is too small: " + maxX + ", maxX must be greater than 0");
        } else if (maxX <= 0) {
            throw new IllegalArgumentException("maxY is too small: " + maxY + ", maxY must be greater than 0");
        }

        if (numOfCoords >= maxX*maxY) {
            throw new IllegalArgumentException("Number of points to generate given maxX and maxY is too large");
        }

        // Create points ArrayList to store all points in
        ArrayList<Coordinate> coordArray = new ArrayList<>();

        Random randomGen = new Random();

        for (int iterate = 0; iterate < numOfCoords; iterate++) {

            // Create newCoord Coordinate (Point) object (null)
            Coordinate newCoord;

            // Keep re-generating point position if a point with the position already exists
            do {
                // Number between 0 and maxX (both inclusive)
                int xPos = randomGen.nextInt(maxX+1);
                // Number between 0 and maxY (both inclusive)
                int yPos = randomGen.nextInt(maxY+1);

                // Update point with newly generated coordinates
                newCoord = new Coordinate(xPos,yPos);

            } while (coordArray.contains(newCoord));

            // After while loop, a unique point exists thus, add to array
            coordArray.add(newCoord);

        }


        return coordArray;
    }

    /**
     * Applies thickness to a Vertex object (io) with a random thickness between 0 (inclusive) and 5 (exclusive)
     * @param vertex the vertex to apply the thickness property to
     * @return a Vertex object (io) with a random thickness between 0 (inclusive) and 5 (exclusive)
     */
    public static Vertex thicknessGen(Vertex vertex) {

        // Create random thickness value
        Random bag = new Random();
        Float vertexWidth = bag.nextFloat(5);

        // Construct thickness property and return a vertex with property applied
        String width = String.valueOf(vertexWidth);
        Property thickness = Property.newBuilder().setKey("thickness").setValue(width).build();
        return vertex.toBuilder().addProperties(thickness).build();

    }

    /**
     * Applies a random RGB value to a Vertex object (io)
     * @param vertex the vertex to apply the RGB property to
     * @return a Vertex object (io) with a random RGB value (each between 0 (inclusive) and 255 (exclusive))
     */
    public static Vertex colorGen(Vertex vertex) {

        // Generate random RGB values
        Random bag = new Random();
        int red = bag.nextInt(255);
        int green = bag.nextInt(255);
        int blue = bag.nextInt(255);

        // Create property and return Vertex with applied property
        String colorCode = red + "," + green + "," + blue;
        Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        return vertex.toBuilder().addProperties(color).build();

    }



    /**
     * Applies a random alpha value to a Vertex object (io)
     * @param vertex the vertex to apply the alpha property to
     * @return a Vertex object (io) with a random alpha value (each between 0 (inclusive) and 1 (inclusive))
     */
    public static Vertex alphaGen(Vertex vertex) {

        // Generate random alpah value
        Random bag = new Random();
        // Add a small number at the end since nextDouble does not include 1.0 so if we get per say 0.9999, we can add to round it up to 1.0
        String alpha = Double.toString(bag.nextDouble() + 0.0000001);

        Property alphaProp = Property.newBuilder().setKey("alpha").setValue(alpha).build();

        return vertex.toBuilder().addProperties(alphaProp).build();

    }

}
