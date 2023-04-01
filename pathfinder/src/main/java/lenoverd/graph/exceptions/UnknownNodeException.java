package lenoverd.graph.exceptions;

public class UnknownNodeException extends Exception {

    public UnknownNodeException(String nodeName) {

        super("Unable to locate node \"" + nodeName + "\" in graph");

    }

}
