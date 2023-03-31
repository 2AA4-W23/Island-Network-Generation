package lenoverd.graph.exceptions;

public class NodePropertyValueFormatException extends Exception {

    public NodePropertyValueFormatException(Class propertyType, Object value) {
        super("Class of value (" + value.getClass() + ") is not the same as property type (" + propertyType.getClass() + ")");
    }

}
