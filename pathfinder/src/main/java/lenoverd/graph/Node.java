package lenoverd.graph;


import lenoverd.graph.exceptions.NodePropertyNotFoundException;

import java.util.*;

// A node of a graph
public class Node {

    private Set<NodeProperty> properties = new HashSet<>();
    private String nodeName;

    public Node(String name) {
        this.nodeName = name;
    }

    public boolean addProperty(NodeProperty property) {
        // Because properties is a set and given how NodeProperty checks for equivalence, one cannot have two properties with the same name
        return properties.add(property);
    }

    public <T extends Object> boolean removeProperty(String propertyName, T propertyType) {

        // Create a new class with blank type and value fields
        // This is done because NodeProperty only cares about the name for equivalence which is what Set will look for
        NodeProperty<T> propertyToRemove = new NodeProperty(propertyName,propertyType);

        return properties.remove(propertyToRemove);
    }

    public <T extends Object> NodeProperty getProperty(String name, T propertyType) throws NodePropertyNotFoundException {

        // Create a test property with the same type as property stored (as this is how property equivalence can be tested)
        NodeProperty<T> testProperty = new NodeProperty(name,propertyType);

        for (NodeProperty curProperty : this.properties) {

            if (curProperty == testProperty) {
                return curProperty;
            }

        }

        throw new NodePropertyNotFoundException(name);

    }


}
