package lenoverd.graph;

import lenoverd.graph.exceptions.UnknownNodeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Graph {

    private static final String edgeDataName = "EdgeData";

    // 2D Array list where the inner list holds all nodes that share edges with the first indexed node
    private ArrayList<ArrayList<Node>> adjacencyList = new ArrayList<>();
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
                adjacentNodes.add(curNode);

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

                // Add constructed neighbouring nodes list to adjacency list
                adjacencyList.add(adjacentNodes);

            }

        }
    }

    public boolean addEdges(Set<Edge> edgeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        for (Edge curEdge : edgeSet) {

            Node firstNode = curEdge.getFirstNode();
            Node secondNode = curEdge.getSecondNode();

            if (!doesExistInAdj(firstNode) || !doesExistInAdj(secondNode) || hasEdgeBetween(firstNode,secondNode)) {
                return false;
            }

        }

        for (Edge curEdge : edgeSet) {

            Node firstNode = curEdge.getFirstNode();
            Node secondNode = curEdge.getSecondNode();

            // Check if both nodes exist within the adjacency list and that there does not already exist an edge between the two nodes
            if (doesExistInAdj(firstNode) && doesExistInAdj(secondNode) && !hasEdgeBetween(firstNode,secondNode)) {

                // Add edge property to secondNode
                secondNode.addProperty(new Property<Edge>(edgeDataName,curEdge));

                // Find neighbours list for first node and add second node
                try {
                    ArrayList<Node> firstNodeNeighbourList = getNodeNeighbourList(firstNode);
                    firstNodeNeighbourList.add(secondNode);
                } catch (UnknownNodeException e) {
                    return false;
                }
            }

        }

        return true;

    }

    public boolean removeEdges(Set<Edge> edgeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        for (Edge curEdge : edgeSet) {

            Node firstNode = curEdge.getFirstNode();
            Node secondNode = curEdge.getSecondNode();

            if (!doesExistInAdj(firstNode) || !doesExistInAdj(secondNode) || !hasEdgeBetween(firstNode,secondNode)) {
                return false;
            }

        }


        for (Edge curEdge : edgeSet) {

            // Get nodes in edge
            Node firstNode = curEdge.getFirstNode();
            Node secondNode = curEdge.getSecondNode();

            // Get the neighbour list for the first node
            try {
                ArrayList<Node> neighbourList = getNodeNeighbourList(firstNode);
                // Remove the second node from said list
                neighbourList.remove(secondNode);
            } catch (UnknownNodeException e) {
                return false;
            }

        }

        return true;

    }

    public boolean addNodes(Set<Node> nodeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        for (Node curNode : nodeSet) {

            if (doesExistInAdj(curNode)) {
                return false;
            }

        }

        // If there is no problem, loop through nodes again
        for (Node curNode : nodeSet) {

            // Create a new neighbour arraylist for each node
            ArrayList<Node> newNeighbourList = new ArrayList<>();
            newNeighbourList.add(curNode);

            // Add the new list to the adj list
            this.adjacencyList.add(newNeighbourList);

        }

        return true;

    }

    public boolean removeNodes(Set<Node> nodeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        for (Node curNode : nodeSet) {

            if (doesExistInAdj(curNode)) {
                return false;
            }

        }

        for (Node curNode : nodeSet) {

            try {
                // Remove node neighbour list
                ArrayList<Node> removalList = getNodeNeighbourList(curNode);

                this.adjacencyList.remove(removalList);

                // Removal all references in other neighbouring lists
                for (ArrayList<Node> curNeighbours : this.adjacencyList) {

                    // Calling to remove on an object for all array list does not throw an error thus it can be called for every list
                    curNeighbours.remove(curNode);

                }
            } catch (UnknownNodeException e) {
                return false;
            }

        }

        return true;

    }

    public ArrayList<Node> getNodeNeighbourList(Node node) throws UnknownNodeException {

        // First check if node actually exists in adj
        if (doesExistInAdj(node)) {

            // Loop through adj and find the list for the given node
            for (ArrayList<Node> curNeighbourList : this.adjacencyList) {

                if (curNeighbourList.get(0) == node) {

                    // Return the corresponding arraylist
                    return curNeighbourList;
                }

            }

        }

        throw new UnknownNodeException(node.getNodeName());

    }

    public Iterator<ArrayList<Node>> iterator() {
        return new Iterator<ArrayList<Node>>() {
            private int pos = 0;
            @Override
            public boolean hasNext() {
                return (adjacencyList.size() > pos);
            }

            @Override
            public ArrayList<Node> next() {

                return adjacencyList.get(pos++);


            }
        };
    }

    // Method to check if a given node exists within the adj list
    private boolean doesExistInAdj(Node testNode) {

        for (ArrayList<Node> currentNeighbours : this.adjacencyList) {

            if (currentNeighbours.get(0) == testNode) {

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
            for (ArrayList<Node> curNeighbours : this.adjacencyList) {

                // Check if the first node in the sub ArrayList is the firstNode (this is the list that contains all neighbours of firstNode)
                if (curNeighbours.get(0) == firstNode) {

                    // Check if the secondNode exists within this list
                    for (Node testNode : curNeighbours) {

                        if (testNode == secondNode) {
                            return true;
                        }

                    }

                    return false;

                }

            }

        }

        return false;

    }

}
