package lenoverd.graph;

import lenoverd.graph.exceptions.NodePropertyNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    private Node test = new Node("testNode");

    @Test
    void addProperty() throws NodePropertyNotFoundException {

        NodeProperty<Double> testDouble = new NodeProperty<>("double",new Double(1.234));
        NodeProperty<Integer> testInteger = new NodeProperty<>("integer",new Integer(1));
        NodeProperty<Float> testFloat = new NodeProperty<>("float",new Float(1.1));
        NodeProperty<String> testString = new NodeProperty<>("string",new String("Test!"));

        test.addProperty(testDouble);
        test.addProperty(testInteger);
        test.addProperty(testFloat);
        test.addProperty(testString);

        assertEquals(test.getProperty("double",new Double(0)).getValue(),testDouble.getValue());
        assertEquals(test.getProperty("integer",new Integer(0)).getValue(),testInteger.getValue());
        assertEquals(test.getProperty("float",new Float(0)).getValue(),testFloat.getValue());
        assertEquals(test.getProperty("string",new String("hello!")).getValue(),testString.getValue());


    }

    @Test
    void removeProperty() {
    }

    @Test
    void getProperty() {
    }
}