package lenoverd.graph;

import lenoverd.graph.exceptions.NodePropertyNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    @Test
    void LiveTest() throws NodePropertyNotFoundException {

        // Blank property nodes
        Node node1 = new Node("test1");
        Node node2 = new Node("test2");

        // Create edge and add property
        Edge edge = new Edge(node1,node2);
        Property<Double> testDouble = new Property<>("double", 1.234);
        edge.addProperty(testDouble);

        // Check property was inserted correctly
        assertEquals(edge.getProperty("double",1.1).getValue(),1.234);

        // Get property object
        Property<Double> testDouble2 = edge.getProperty("double",1.5);
        // Change it's value
        testDouble2.changeValue(3.456);

        // Check if property was live
        assertEquals(edge.getProperty("double",1.1).getValue(),3.456);

    }

}