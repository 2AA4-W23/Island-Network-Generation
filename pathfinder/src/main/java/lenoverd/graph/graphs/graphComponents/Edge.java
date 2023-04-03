package lenoverd.graph.graphs.graphComponents;

import lenoverd.graph.property.PropertiesHolder;

public class Edge extends PropertiesHolder {

    private Node[] nodePair = new Node[2];

    /**
     * Create a edge object for a graph
     * @param node1 the first node in the edge
     * @param node2 the second node in the edge
     * @return a edge object
     */
    public Edge(Node node1, Node node2) {
        this.nodePair[0] = node1;
        this.nodePair[1] = node2;
    }

    /**
     * Get the first node in the edge
     * @return a node object
     */
    public Node getFirstNode() {
        return this.nodePair[0];
    }

    /**
     * Get the second node in the edge
     * @return a node object
     */
    public Node getSecondNode() {
        return this.nodePair[1];
    }


}
