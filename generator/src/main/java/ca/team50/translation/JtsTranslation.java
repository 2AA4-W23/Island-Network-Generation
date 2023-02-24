package ca.team50.translation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.team50.adt.Polygons;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JtsTranslation {

    public static Polygons convertToPolygons(Polygon jtsPolygon) {

        List<Vertex> convertedCoordsList = new ArrayList<>();

        // Extract all coordinates from Polygon geometry
        Coordinate[] polygonCoords = jtsPolygon.getCoordinates();

        // For each coordinate object, convert to a Vertex and add it to the converted list
        for (Coordinate currentCoord : polygonCoords) {

            Vertex newVertex = Vertex.newBuilder().setX(currentCoord.getX()).setY(currentCoord.getY()).build();
            convertedCoordsList.add(newVertex);

        }

        // Get centroid as a Vertex
        Point centroidCoord = jtsPolygon.getCentroid();
        Vertex centroid = Vertex.newBuilder().setX(centroidCoord.getX()).setY(centroidCoord.getY()).build();

        // TESTING PROPERTIES ----
        ArrayList<Vertex> vertWithPropList = new ArrayList<>();
        Random bag = new Random();
        Random bag1 = new Random();
        for (Vertex curVert : convertedCoordsList) {
            Float vertexWidth = 2.0f;
            String width = String.valueOf(vertexWidth);
            Structs.Property thickness = Structs.Property.newBuilder().setKey("thickness").setValue(width).build();
            curVert = curVert.toBuilder().addProperties(thickness).build();

            int red = bag1.nextInt(255);
            int green = bag1.nextInt(255);
            int blue = bag1.nextInt(255);
            String colorCode = red + "," + green + "," + blue;
            Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Vertex colored = curVert.toBuilder().addProperties(color).build();
            vertWithPropList.add(colored);

        }


        // -------------

        // Return new Polygons instance with all info
        return new Polygons(vertWithPropList,centroid);

    }

}
