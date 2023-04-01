package lenoverd.graph;

import lenoverd.graph.exceptions.NodePropertyNotFoundException;

import java.util.*;

// A node of a graph
public class Node {

    private List<NodeProperty> properties = new ArrayList<>();
    private String nodeName;

    /**
     * Create a node object for a graph
     * @param name the name to reference the node as
     * @return a node object
     */
    public Node(String name) {
        this.nodeName = name;
    }

    /**
     * Add a NodeProperty object to the node
     * @param property a NodeProperty object of any parametrized type
     * @return true if the property is added to the properties list
     * Only one property with the same name and value type can exist in a node at any given time, if a NodeProperty object is added and the given Node already contains such NodeProperty (same value and type), the existing property is overwritten
     */
    public <T extends Object> boolean addProperty(NodeProperty<T> property) {

        // Because properties should not contain the same property names and given how NodeProperty checks for equivalence, overwrite existing property if they have the same name and type
        if (this.properties.contains(property)) {
            this.properties.remove(property);
        }

        return this.properties.add(property);
    }

    /**
     * Remove a NodeProperty object from the node
     * @param propertyName the property name
     * @param propertyType the property value type (does NOT have to be the same value, just the same type)
     * @return true if the property is removed from the properties list
     */
    public <T extends Object> boolean removeProperty(String propertyName, T propertyType) {

        // Create a new class with blank type and value fields
        // This is done because NodeProperty only cares about the name and value type for equivalence which is what the list will look for
        NodeProperty<T> propertyToRemove = new NodeProperty(propertyName,propertyType);

        return this.properties.remove(propertyToRemove);
    }

    /**
     * Get a NodeProperty object from the node
     * @param propertyName the property name
     * @param propertyType the property value type (does NOT have to be the same value, just the same type)
     * @return the NodeProperty object. This object is LIVE, meaning making changes to the returned object will automatically update the property in the node
     * @throws NodePropertyNotFoundException if the specified property is not found
     */
    public <T extends Object> NodeProperty getProperty(String propertyName, T propertyType) throws NodePropertyNotFoundException {

        // Create a test property with the same type as property stored (as this is how property equivalence can be tested)
        NodeProperty<T> testProperty = new NodeProperty(propertyName,propertyType);

        if (this.properties.contains(testProperty)) {

            for (NodeProperty curProperty : this.properties) {
                if (curProperty.equals(testProperty)) {
                    return curProperty;
                }
            }

        }

        throw new NodePropertyNotFoundException(propertyName);

    }

    /**
     * Get the number of properties in the node
     * @return an integer value describing the number of properties within the node
     */
    public int getPropertiesSize() {
        return this.properties.size();
    }

    /**
     * Get the name of the node
     * @return a string value describing the name of the node
     */
    public String getNodeName() {
        return this.nodeName;
    }


}
