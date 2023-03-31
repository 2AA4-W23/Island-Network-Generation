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

    public boolean removeProperty(String propertyName) {

        // Create a new class with blank type and value fields
        // This is done because NodeProperty only cares about the name for equivalence which is what Set will look for
        NodeProperty propertyToRemove = new NodeProperty(propertyName,new Object().getClass(),new Object());

        return properties.remove(propertyToRemove);
    }

    public NodeProperty getProperty(String name) throws NodePropertyNotFoundException {

        for (NodeProperty curProperty : this.properties) {

            if (curProperty.getName() == name) {

                return curProperty;

            }

        }

        throw new NodePropertyNotFoundException(name);

    }


}
