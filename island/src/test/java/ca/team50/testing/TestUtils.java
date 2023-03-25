package ca.team50.testing;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Circle;
import ca.team50.shapes.Elipse;
import ca.team50.shapes.IslandShape;
import ca.team50.shapes.Rectangle;
import junit.framework.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TestUtils {

    // ------ Testing for proper isVertexInside Implementation ------ //
    @ParameterizedTest
    @MethodSource("createParameters")
    public void testShapeInputs(IslandShape shapeToTest, List<Polygons> validPolygons, List<Polygons> invalidPolygons) {

        for (Polygons curPoly : validPolygons) {
            Assert.assertTrue(shapeToTest.isVertexInside(curPoly.getCentroid()));
        }

        for (Polygons curPoly : invalidPolygons) {
            Assert.assertFalse(shapeToTest.isVertexInside(curPoly.getCentroid()));
        }

    }

    // Method to generate parameters for the test method above
    private static Stream<Arguments> createParameters() {

        // Parameters can be changed, this is just testing with an empty list
        List<Polygons> validPolygons = new ArrayList<>();
        List<Polygons> invalidPolygons = new ArrayList<>();

        return Stream.of(
                Arguments.of(new Circle(Structs.Vertex.newBuilder().setX(500).setY(500).build(),50),validPolygons,invalidPolygons),
                Arguments.of(new Elipse(Structs.Vertex.newBuilder().setX(500).setY(500).build(),500,1000,1.2),validPolygons,invalidPolygons),
                Arguments.of(new Rectangle(Structs.Vertex.newBuilder().setX(500).setY(500).build(),Structs.Vertex.newBuilder().setX(800).setY(500).build(),Structs.Vertex.newBuilder().setX(800).setY(800).build(),Structs.Vertex.newBuilder().setX(500).setY(800).build()),validPolygons,invalidPolygons)
        );
    }

    // ------ END TEST ------ //

}
