package ca.team50.generation;

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



}
