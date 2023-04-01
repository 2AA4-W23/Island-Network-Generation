package lenoverd.graph;

import lenoverd.graph.exceptions.NodePropertyNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    static NodeProperty<Double> testDouble = new NodeProperty<>("double", 1.234);
    static NodeProperty<Integer> testInteger = new NodeProperty<>("integer",1);
    static NodeProperty<Float> testFloat = new NodeProperty<>("float",1.2f);
    static NodeProperty<String> testString = new NodeProperty<>("string","test!");

    @Test
    @Order(1)
    void addProperty() throws NodePropertyNotFoundException {

        Node test = new Node("testNode");

        test.addProperty(testDouble);
        test.addProperty(testInteger);
        test.addProperty(testFloat);
        test.addProperty(testString);

        // Check if values match
        assertEquals(test.getProperty("double", 1.1).getValue(), testDouble.getValue());
        assertEquals(test.getProperty("integer", 1).getValue(), testInteger.getValue());
        assertEquals(test.getProperty("float", 1.1f).getValue(), testFloat.getValue());
        assertEquals(test.getProperty("string", "hello!").getValue(), testString.getValue());

    }

    @Test
    @Order(2)
    void removeAndOverwriteProperty() throws NodePropertyNotFoundException {

        Node test = new Node("testNode");

        test.addProperty(testDouble);
        test.addProperty(testInteger);
        test.addProperty(testFloat);
        test.addProperty(testString);

        // Check if removing values are possible
        assertTrue(test.removeProperty("integer",2));
        assertThrows(NodePropertyNotFoundException.class,() -> test.getProperty("integer",2));

        // Check if overwriting values are possible
        NodeProperty<Double> testDouble2 = new NodeProperty<>("double", 6.789);
        test.addProperty(testDouble2);
        assertEquals(test.getPropertiesSize(),3);
        assertEquals(test.getProperty("double",1.1).getValue(),6.789);

    }

    @Test
    @Order(3)
    void testLiveManipulation() throws NodePropertyNotFoundException {

        Node test = new Node("testNode");

        test.addProperty(testDouble);
        test.addProperty(testInteger);
        test.addProperty(testFloat);
        test.addProperty(testString);

        assertEquals(test.getProperty("double",1.1).getValue(),1.234);

        // Get property from node
        NodeProperty<Double> testDouble2 = test.getProperty("double",1.1);
        // Change it's value
        testDouble2.changeValue(5.55);
        // Verify property is live
        assertEquals(test.getProperty("double",1.1).getValue(),5.55);

    }

}