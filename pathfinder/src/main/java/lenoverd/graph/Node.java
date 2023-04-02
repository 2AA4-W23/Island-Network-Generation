package lenoverd.graph;

// A node of a graph
public class Node extends PropertiesHolder {
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
     * Get the name of the node
     * @return a string value describing the name of the node
     */
    public String getNodeName() {
        return this.nodeName;
    }

    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {

            Node testNode = (Node) obj;

            if (testNode.getNodeName() == this.getNodeName()) {

                return true;

            }

        }

        return false;
    }


}
