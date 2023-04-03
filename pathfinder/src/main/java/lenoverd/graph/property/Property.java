package lenoverd.graph.property;

public class Property<T> {

    private String propertyName;
    private T value;

    public Property(String name, T value) {
        this.propertyName = name;
        this.value = value;
    }

    /**
     * Get the value within the Property object
     * @return a parameterized value stored in the object
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Get the name of the Property object
     * @return a String value describing the name of the Property object
     */
    public String getName() {
        return this.propertyName;
    }

    /**
     * Change the given value within the Property object
     * @param value the given value to change the current value to
     */
    public void changeValue(T value) {

        this.value = value;

    }

    /**
     * Compare Property object's
     * @param obj the object to compare
     * @return true if the object is: a Property object, has the same value type (class), has the same name. False otherwise
     */
    public boolean equals(Object obj) {

        // Check if the object is a NodeProperty
        if (obj.getClass() == Property.class) {

            Property testProperty = (Property) obj;

            // Check if value types are the same
            if (testProperty.getValue().getClass() == this.getValue().getClass()) {

                // Only testing if the names are the same avoids confusing edge cases in sets such as testing for properties with the same name, same value types but different values
                // So for all purposes, we assume property names with the same name and value TYPE are the same property object
                // This helps to organize properties better
                if (testProperty.getName().equals(this.propertyName)) {
                    return true;
                }
            }

        }

        return false;

    }
}
