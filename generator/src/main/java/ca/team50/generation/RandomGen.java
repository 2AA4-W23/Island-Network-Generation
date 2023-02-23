package ca.team50.generation;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

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
     * @return an array of Point objects containing unique x and y positions
     */
    public static Point[] genPoints(int maxX, int maxY, int numOfPoints) {

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

        // Create points array to store all points in
        // Note this creates an array of null points
        Point[] pointArray = new Point[numOfPoints];

        Random randomGen = new Random();

        for (int index = 0; index < pointArray.length; index++) {

            // Create newPoint Point object
            Point newPoint;

            // Convert updated point array to Arraylist to for checking if objects exist within the array (do-while loop below)
            List<Point> arrayAsList = Arrays.asList(pointArray);

            // Keep re-generating point position if a point with the position already exists
            do {
                // Number between 0 and maxX (both inclusive)
                int xPos = randomGen.nextInt(maxX+1);
                // Number between 0 and maxY (both inclusive)
                int yPos = randomGen.nextInt(maxY+1);

                // Update point with newly generated coordinates
                newPoint = factory.createPoint(new Coordinate(xPos,yPos));

            } while (arrayAsList.contains(newPoint));

            // After while loop, a unique point exists thus, add to array
            pointArray[index] = newPoint;

        }

        return pointArray;
    }



}
