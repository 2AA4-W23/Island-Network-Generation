package lenoverd.graph;

import lenoverd.graph.exceptions.NodePropertyValueFormatException;

public class NodeProperty<T> {

    private String propertyName;
    private T value;

    public NodeProperty(String name, T value) {
        this.propertyName = name;
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public String getName() {
        return this.propertyName;
    }

    public void changeValue(T value) {

        this.value = value;

    }


    public boolean equals(Object obj) {

        // Check if the object is a NodeProperty
        if (obj.getClass() == this.getClass()) {

            NodeProperty testProperty = (NodeProperty) obj;

            // Check if value types are the same
            if (testProperty.getValue().getClass() == this.getValue().getClass()) {

                // Only testing if the names are the same avoids confusing edge cases in sets such as testing for properties with the same name, same value types but different values
                // So for all purposes, we assume property names with the same name and value TYPE are the same property object
                // This helps to organize properties better
                if (testProperty.getName() == this.propertyName) {
                    return true;
                }
            }

        }

        return false;

    }
}
