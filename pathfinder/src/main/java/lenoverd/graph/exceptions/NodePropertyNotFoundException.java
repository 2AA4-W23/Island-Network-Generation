package lenoverd.graph.exceptions;

public class NodePropertyNotFoundException extends Exception {

    public NodePropertyNotFoundException(String propertyName) {
        super("Property with name \"" + propertyName + "\" was not found");
    }

}
