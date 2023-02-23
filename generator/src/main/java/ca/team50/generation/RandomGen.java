package ca.team50.generation;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomGen {

    /**
     * Generates an array of Point (JTS) objects, each containing a unique x and y position
     * @param maxX the maximum value x can be (0 to maxX) (both values are inclusive)
     * @param maxY the maximum value y can be (0 to maxY) (both values are inclusive)
     * @param numOfPoints the number of points to be generated
     * @exception IllegalArgumentException if maxX or maxY are less than or equal to 0. If numOfPoints exceeds the maximum possible number of unique points (that is maxX*maxY)
     * @return an Arraylist of Point objects containing unique x and y positions
     */
    public static ArrayList<Point> genPoints(int maxX, int maxY, int numOfPoints) {

        // Check for illegal arguments
        if (maxX <= 0) {
            throw new IllegalArgumentException("maxX is too small: " + maxX + ", maxX must be greater than 0");
        } else if (maxX <= 0) {
            throw new IllegalArgumentException("maxY is too small: " + maxY + ", maxY must be greater than 0");
        }

        if (numOfPoints >= maxX*maxY) {
            throw new IllegalArgumentException("Number of points to generate given maxX and maxY is too large");
        }

        // Create GeometryFactory for creating new points
        GeometryFactory factory = new GeometryFactory();

        // Create points ArrayList to store all points in
        ArrayList<Point> pointArray = new ArrayList<>();

        Random randomGen = new Random();

        for (int iterate = 0; iterate < numOfPoints; iterate++) {

            // Create newPoint Point object (null)
            Point newPoint;

            // Keep re-generating point position if a point with the position already exists
            do {
                // Number between 0 and maxX (both inclusive)
                int xPos = randomGen.nextInt(maxX+1);
                // Number between 0 and maxY (both inclusive)
                int yPos = randomGen.nextInt(maxY+1);

                // Update point with newly generated coordinates
                newPoint = factory.createPoint(new Coordinate(xPos,yPos));

            } while (pointArray.contains(newPoint));

            // After while loop, a unique point exists thus, add to array
            pointArray.add(newPoint);

        }

        return pointArray;
    }



}
