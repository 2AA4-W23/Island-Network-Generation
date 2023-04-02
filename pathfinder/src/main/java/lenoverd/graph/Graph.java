package lenoverd.graph;

import lenoverd.graph.exceptions.UnknownNodeException;

import java.util.*;

public class Graph {

    private static final String edgeDataName = "EdgeData";

    // 2D Array list where the inner list holds all nodes that share edges with the first indexed node
    //private ArrayList<ArrayList<Node>> adjacencyList = new ArrayList<>();

    // A using a map as a substitute for an adjacency list makes knowing the parent node a lot easier
    // After all, for a 2D array list, a lot of people would probably not read that the first node in the arraylist corresponds to the parent node
    // It makes more sense to use the key as a parent node here
    private HashMap<Node,ArrayList<Node>> adjacencyMap = new HashMap<>();

    public Graph(Set<Node> nodeSet, Set<Edge> edgeSet) {

        constructAdjList(nodeSet,edgeSet);

    }

    // Method to construct the adjacency list
    private void constructAdjList(Set<Node> nodeSet, Set<Edge> edgeSet) {

        // For each node, go through the list of edges and find where the node is in an edge and is the first node
        for (Node curNode : nodeSet) {

            // Check if node doesn't already exist in adj list (this is just a failsafe check)
            if (!doesExistInAdj(curNode)) {

                ArrayList<Node> adjacentNodes = new ArrayList<>();

                for (Edge curEdge : edgeSet) {

                    if (curEdge.getFirstNode() == curNode) {

                        // Get the second node
                        Node neighbourNode = curEdge.getSecondNode();

                        // Construct a new property that contains the edge object
                        Property<Edge> edgeData = new Property<>(edgeDataName,curEdge);
                        // Apply this property to the node (this way we can reference the weightings later)
                        neighbourNode.addProperty(edgeData);

                        // Add the adjacent node to the list
                        adjacentNodes.add(curEdge.getSecondNode());

                    }

                }

                // Add constructed neighbouring nodes list to adjacency map with the parent node as key
                adjacencyMap.put(curNode,adjacentNodes);

            }

        }
    }

    public boolean addEdges(Set<Edge> edgeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        for (Edge curEdge : edgeSet) {

            Node firstNode = curEdge.getFirstNode();
            Node secondNode = curEdge.getSecondNode();

            // Note a slight difference to the method isValidEdges on hasEdgeBetween
            if (!doesExistInAdj(firstNode) || !doesExistInAdj(secondNode) || hasEdgeBetween(firstNode,secondNode)) {
                return false;
            }

        }

        for (Edge curEdge : edgeSet) {

            Node firstNode = curEdge.getFirstNode();
            Node secondNode = curEdge.getSecondNode();

            // Add edge property to secondNode
            secondNode.addProperty(new Property<Edge>(edgeDataName,curEdge));

            // Find neighbours list for first node and add second node
            ArrayList<Node> firstNodeNeighbourList = getNodeNeighbourList(firstNode);
            firstNodeNeighbourList.add(secondNode);

        }

        return true;

    }

    public boolean removeEdges(Set<Edge> edgeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        if (isValidEdges(edgeSet)) {

            for (Edge curEdge : edgeSet) {

                // Get nodes in edge
                Node firstNode = curEdge.getFirstNode();
                Node secondNode = curEdge.getSecondNode();

                // Get the neighbour list for the first node
                ArrayList<Node> neighbourList = getNodeNeighbourList(firstNode);
                // Remove the second node from said list
                neighbourList.remove(secondNode);

            }

            return true;

        }

        return false;

    }

    public boolean addNodes(Set<Node> nodeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        if (!isValidNodes(nodeSet)) {

            for (Node curNode : nodeSet) {

                // Add the new node to the adj map
                this.adjacencyMap.put(curNode,new ArrayList<>());

            }

            return true;

        }

        return false;

    }

    public boolean removeNodes(Set<Node> nodeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        if (isValidNodes(nodeSet)) {

            for (Node curNode : nodeSet) {

                // Remove node from map
                this.adjacencyMap.remove(curNode);

            }

            return true;
        }

        return false;

    }

    public ArrayList<Node> getNodeNeighbourList(Node node) {

        // First check if node actually exists in adj
        if (doesExistInAdj(node)) {

            // Loop through adj and find the list for the given node
            for (Node curNode : adjacencyMap.keySet()) {

                if (curNode == node) {

                    // Return the corresponding arraylist
                    return adjacencyMap.get(curNode);
                }

            }

        }

        return null;

    }

    public Iterator<ArrayList<Node>> iterator() {
        return new Iterator<ArrayList<Node>>() {
            Iterator<Node> parentNodeIterator = adjacencyMap.keySet().iterator();

            @Override
            public boolean hasNext() {
                return (parentNodeIterator.hasNext());
            }

            @Override
            public ArrayList<Node> next() {

                return adjacencyMap.get(parentNodeIterator.next());


            }
        };
    }

    public ArrayList<Node> getNodeNeighbourList(String nodeName) {

        for (Node curNode : this.adjacencyMap.keySet()) {

            if (curNode.getNodeName() == nodeName) {

                return this.adjacencyMap.get(curNode);

            }

        }

        return null;

    }

    // Method to check if a given node exists within the adj map
    private boolean doesExistInAdj(Node testNode) {

        for (Node curNode : adjacencyMap.keySet()) {

            if (curNode == testNode) {

                return true;

            }

        }

        return false;

    }

    // Method to check if two Nodes share an edge in the order of firstNode to secondNode
    private boolean hasEdgeBetween(Node firstNode, Node secondNode) {

        // Check if both nodes exist within the graph
        if (doesExistInAdj(firstNode) && doesExistInAdj(secondNode)) {

            // Loop through each neighbour list for all nodes
            for (Node curNode : adjacencyMap.keySet()) {

                // Check if the current node we are looping on is the first node specified
                if (curNode == firstNode) {

                    // Loop through it's neighbour set
                    for (Node testNode : adjacencyMap.get(curNode)) {

                        // return true if the second node exists with in the neighbour set
                        if (testNode == secondNode) {

                            return true;

                        }

                    }

                }

            }

        }

        return false;

    }

    // Method to check if the all nodes in a set exist within the adj list
    private boolean isValidNodes(Set<Node> nodeSet) {

        for (Node curNode : nodeSet) {

            if (doesExistInAdj(curNode)) {
                return true;
            }

        }

        return false;
    }


    // Method to check if the all edge in a set exist within the adj list
    private boolean isValidEdges(Set<Edge> edgeSet) {

        for (Edge curEdge : edgeSet) {

            Node firstNode = curEdge.getFirstNode();
            Node secondNode = curEdge.getSecondNode();

            if (!doesExistInAdj(firstNode) || !doesExistInAdj(secondNode) || !hasEdgeBetween(firstNode,secondNode)) {
                return false;
            }

        }

        return true;

    }

}
