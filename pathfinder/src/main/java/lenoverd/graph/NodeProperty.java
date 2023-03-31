package lenoverd.graph;

import lenoverd.graph.exceptions.NodePropertyValueFormatException;

public class NodeProperty {

    private String propertyName;
    private Class propertyType;
    private Object value;

    public NodeProperty(String name, Class type, Object value) {
        this.propertyName = name;
        this.propertyType = type;
        this.value = value;
    }

    public Class getType() {
        return this.propertyType;
    }

    public Object getValue() {
        return this.value;
    }

    public String getName() {
        return this.propertyName;
    }

    public void changeValue(Object value) throws NodePropertyValueFormatException {

        // Classes between value and propertyType must match
        if (value.getClass() == this.propertyType.getClass()) {
            this.value = value;
            return;
        }

        throw new NodePropertyValueFormatException(this.propertyType.getClass(),value);

    }


    public boolean equals(Object obj) {

        // Check if the object is a NodeProperty
        if (obj.getClass() == NodeProperty.class) {

            NodeProperty testProperty = (NodeProperty) obj;

            // Only testing if the names are the same avoids confusing edge cases in sets such as testing for properties with the same name but of different value types
            // So for all purposes, we assume property names with the same name are the same property object
            // This helps to organize properties better
            if (testProperty.getName() == this.propertyName) {
                return true;
            }

        }

        return false;

    }
}
