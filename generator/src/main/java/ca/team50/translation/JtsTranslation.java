package ca.team50.translation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.RandomGen;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

public class JtsTranslation {

    /**
     * Translates a Polygon object (JTS) into a Polygons object
     * @param jtsPolygon the polygon in JTS format
     * @return a Polygons object containing info from jtsPolygon
     * @implNote Note that the vertices generated contain no properties, just position
     */
    public static Polygons convertToPolygons(Polygon jtsPolygon) {

        List<Vertex> convertedCoordsList = new ArrayList<>();

        // Extract all coordinates from Polygon geometry
        Coordinate[] polygonCoords = jtsPolygon.getCoordinates();

        // For each coordinate object, convert to a Vertex and add it to the converted list
        for (Coordinate currentCoord : polygonCoords) {

            Vertex newVertex = Vertex.newBuilder().setX(currentCoord.getX()).setY(currentCoord.getY()).build();
            // Apply random thickness and color properties to newly created Vertex
            newVertex = RandomGen.colorGen(RandomGen.thicknessGen(newVertex));
            convertedCoordsList.add(newVertex);

        }

        // Get centroid as a Vertex
        Point centroidCoord = jtsPolygon.getCentroid();
        Vertex centroid = Vertex.newBuilder().setX(centroidCoord.getX()).setY(centroidCoord.getY()).build();

        // Return new Polygons instance with all info
        return new Polygons(convertedCoordsList,centroid);

    }

    /**
     * Translates a Voronoi geometry collection into a PolyMesh object
     * @implNote Note that the vertices generated contain no properties, just position
     * @param jtsGeometryCollection the collection of polygons (JTS) as a Geometry object (JTS)
     * @exception IllegalArgumentException if the collection size is 0
     * @return a PolyMesh object containing all translated Polygon objects (JTS)
     */
    public static PolyMesh<Polygons> convertToPolyMesh(Geometry jtsGeometryCollection) {

        // Error checking
        if (jtsGeometryCollection.getNumGeometries() == 0) {
            throw new IllegalArgumentException("Geometry collection must be bigger than 0");
        }

        List<Polygons> convertedPolygonsList = new ArrayList<>();

        // Loop through all Polygons, call method to translate the given instance of a JTS Polygon object into a Polygons object
        // Then add it to the Polygons list
        for (int geoIndex = 0; geoIndex < jtsGeometryCollection.getNumGeometries(); geoIndex++) {

            convertedPolygonsList.add(JtsTranslation.convertToPolygons((Polygon) jtsGeometryCollection.getGeometryN(geoIndex)));

        }

        // Create a PolyMesh and feed it all the translated Polygons
        PolyMesh<Polygons> returnMesh = new PolyMesh<>();

        returnMesh.addAll(convertedPolygonsList);

        return returnMesh;


    }

}
