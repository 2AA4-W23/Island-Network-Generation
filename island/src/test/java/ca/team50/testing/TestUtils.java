package ca.team50.testing;

import ca.team50.soilAbsorption.*;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.IslandGenerable;
import ca.team50.generation.TestIsland;
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

import static junit.framework.Assert.assertEquals;

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


    /*@Test
    public void testCalculateClayAbsorptionRate() {
        Clay clay = new Clay(0.6, 0.2, 0.2, 0);
        double expectedAbsorptionRate = 0.2;
        double actualAbsorptionRate = clay.calculateAbsorptionRate(0.6, 0.2, 0.2);
        assertEquals(expectedAbsorptionRate, actualAbsorptionRate, 0.001);
    }
    @Test
    public void testGenerateClaySoilProfile() {
        SoilProfile clayProfile = new Clay(0.6, 0.2, 0.2, 0.2);
        SoilProfile actualProfile = clayProfile.generateSoilProfile(0.6, 0.2, 0.2);
        assertEquals(clayProfile.getClass(), actualProfile.getClass());
    }

    @Test
    public void testGenerateSandSoilProfile() {
        SoilProfile sandProfile = new Sand(0.2, 0.7, 0.1, 0.54);
        SoilProfile actualProfile = sandProfile.generateSoilProfile(0.2, 0.7, 0.1);
        assertEquals(sandProfile.getClass(), actualProfile.getClass());
    }

    @Test
    public void testCalculateSandAbsorptionRate() {
        Sand sand = new Sand(0.2, 0.7, 0.1, 0);
        double expectedAbsorptionRate = 0.54;
        double actualAbsorptionRate = sand.calculateAbsorptionRate(0.2, 0.7, 0.1);
        assertEquals(expectedAbsorptionRate, actualAbsorptionRate, 0.001);
    }

    @Test
    public void testGenerateLoamSoilProfile() {
        SoilProfile loamProfile = new Loam(0.4, 0.4, 0.2, 0.4);
        SoilProfile actualProfile = loamProfile.generateSoilProfile(0.4, 0.4, 0.2);
        assertEquals(loamProfile.getClass(), actualProfile.getClass());
    }

    @Test
    public void testCalculateLoamAbsorptionRate() {
        Loam loam = new Loam(0.4, 0.4, 0.2, 0);
        double expectedAbsorptionRate = 0.4;
        double actualAbsorptionRate = loam.calculateAbsorptionRate(0.4, 0.4, 0.2);
        assertEquals(expectedAbsorptionRate, actualAbsorptionRate, 0.001);
    }
    @Test
    public void testGenerateSpecialSoilProfile() {
        SoilProfile specialProfile = new Special(0.6, 0.6, 0.5, 0.4);
        SoilProfile actualProfile = specialProfile.generateSoilProfile(0.7, 0.6, 0.5);
        assertEquals(specialProfile.getClass(), actualProfile.getClass());
    } */



    // ------ END TEST ------ //

    // ------ TESTING ISLAND ------ //
    /*@Test
    public void generateIslandDummy() {
        PolyMesh<Polygons> testMesh = new PolyMesh<>();
        IslandGenerable island = new TestIsland();
        island.generateIsland(testMesh);
    }*/
    // ------ END TEST ------ //

}
