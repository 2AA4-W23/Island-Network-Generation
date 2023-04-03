package lenoverd.graph;

import java.util.*;

public class UndirectedGraph implements Graph {

    private static final String edgeDataName = "EdgeData";

    // A using a map as a substitute for an adjacency list makes knowing the parent node a lot easier
    // After all, for a 2D array list, a lot of people would probably not read that the first node in the arraylist corresponds to the parent node
    // It makes more sense to use the key as a parent node here
    private HashMap<Node,ArrayList<Node>> adjacencyMap = new HashMap<>();

    /**
     * Create a graph
     * @param nodeSet the set of nodes (Node objects) in the graph
     * @param edgeSet the set of edges (Edge objects) in the graph. Edges that do not specify any Node objects in nodeSet are unused.
     * @return a Graph object
     */
    public UndirectedGraph(Set<Node> nodeSet, Set<Edge> edgeSet) {

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

                        // Construct a new property that contains the edge object (along with the names of the two nodes together)
                        // Note that nodes are live, meaning if we add a property with the same name, that property is overwritten in all references of this node object
                        Property<Edge> edgeData = new Property<>(edgeDataName+curNode.getNodeName()+neighbourNode.getNodeName(),curEdge);

                        // Apply this property to the node (this way we can reference the weightings later)
                        neighbourNode.addProperty(edgeData);

                        // Add the adjacent node to the list
                        adjacentNodes.add(neighbourNode);

                        // Copy the edge to the neighbour node (as the graph is undirected so we duplicate the edge and just flip the node order in the edge)
                        copyEdge(curEdge);

                    }

                }

                // Add constructed neighbouring nodes list to adjacency map with the parent node as key
                adjacencyMap.put(curNode,adjacentNodes);

            } else {

                // copyEdge method adds a node to the adj map if needed, thus it's possible for a node that has other edges to be skipped
                // Thus if it does already exist in the map, we just add onto it's neighbour list if applicable
                ArrayList<Node> adjacentNodes = (ArrayList<Node>) getNodeNeighbourList(curNode);

                for (Edge curEdge : edgeSet) {

                    if (curEdge.getFirstNode() == curNode) {

                        // Get the second node
                        Node neighbourNode = curEdge.getSecondNode();

                        if (!adjacentNodes.contains(neighbourNode)) {

                            // Construct a new property that contains the edge object (along with the names of the two nodes together)
                            // Note that nodes are live, meaning if we add a property with the same name, that property is overwritten in all references of this node object
                            Property<Edge> edgeData = new Property<>(edgeDataName+curNode.getNodeName()+neighbourNode.getNodeName(),curEdge);

                            // Apply this property to the node (this way we can reference the weightings later)
                            neighbourNode.addProperty(edgeData);

                            // Add the adjacent node to the list
                            adjacentNodes.add(neighbourNode);

                            // Copy the edge to the neighbour node (as the graph is undirected so we duplicate the edge and just flip the node order in the edge)
                            copyEdge(curEdge);
                        }

                    }

                }

            }

        }
    }

    // Method to create an identical edge just with nodes flipped and store in graph
    private void copyEdge(Edge edge) {

        Node secondNode = edge.getSecondNode();

        // Get the first node
        Node firstNode = edge.getFirstNode();

        // Create a new edge and swap the nodes
        Edge swappedEdge = new Edge(secondNode,firstNode);

        // copy all properties to this new edge
        for (Property propToCopy : edge.getAllProperties()) {
            swappedEdge.addProperty(propToCopy);
        }

        // Construct a new property that contains the edge object (along with the names of the two nodes together)
        // Note that nodes are live, meaning if we add a property with the same name, that property is overwritten in all references of this node object
        Property<Edge> edgeData = new Property<>(edgeDataName+secondNode.getNodeName()+firstNode.getNodeName(),swappedEdge);

        // Add property to first node
        firstNode.addProperty(edgeData);

        // Check if the second node already exists in the graph
        if (!doesExistInAdj(secondNode)) {

            // If the node does not exist as a key in the map yet, create an entry for it

            // Create new neighbouring list
            ArrayList<Node> neighbourNodes = new ArrayList<>();
            neighbourNodes.add(firstNode);

            // Add to adj map
            this.adjacencyMap.put(secondNode,neighbourNodes);

        } else {

            // Get neighbour nodes list
            ArrayList<Node> neighbourList = this.adjacencyMap.get(secondNode);
            // Add new neighbour to list
            neighbourList.add(firstNode);

        }


    }

    /**
     * Adds edges to a graph
     * @param edgeSet the set of edges (Edge objects) to add to the graph
     * @return true if all edges were added, false is one or more edges were not added
     * If any edges contain unspecified nodes in the graph, the method will return false and no edge will be added regardless whether some edges contained valid nodes.
     * A valid node is a node that exists within the Graph object
     */
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
            ArrayList<Node> firstNodeNeighbourList = (ArrayList<Node>) getNodeNeighbourList(firstNode);
            firstNodeNeighbourList.add(secondNode);

            // Call copy edge to copy the edge to the second node (as this graph is undirected)
            copyEdge(curEdge);

        }

        return true;

    }

    /**
     * Remove edges from a graph
     * @param edgeSet the set of edges (Edge objects) to remove from the graph
     * @return true if all edges were removed, false is one or more edges were not valid
     * If any edges contain unspecified nodes in the graph, the method will return false and no edge will be removed regardless whether some edges were valid.
     * A valid edge is an edge where the edge exists within the graph
     */
    public boolean removeEdges(Set<Edge> edgeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        if (isValidEdges(edgeSet)) {

            for (Edge curEdge : edgeSet) {

                // Get nodes in edge
                Node firstNode = curEdge.getFirstNode();
                Node secondNode = curEdge.getSecondNode();

                // Get the neighbour list for the first node
                ArrayList<Node> neighbourList = (ArrayList<Node>) getNodeNeighbourList(firstNode);
                // Remove the second node from said list
                neighbourList.remove(secondNode);

                // Given this graph is undirected, we do the same with the second node
                // Get the neighbour list for the second node
                ArrayList<Node> neighbourListSecond = (ArrayList<Node>) getNodeNeighbourList(secondNode);
                // Remove the first node from said list
                neighbourListSecond.remove(firstNode);

            }

            return true;

        }

        return false;

    }

    /**
     * Adds nodes to a graph
     * @param nodeSet the set of nodes (Node objects) to add to the graph
     * @return true if all nodes were added, false is one or more nodes were not added
     * Nodes to add must be invalid, otherwise false is returned and no node is added from the set
     * A invalid node is a node that does not exist within the Graph object
     */
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

    /**
     * Removes nodes from a graph and the neighbour reference list for the given node.
     * This consequently removes edges where the first node within the edge is the node to be removed.
     * @param nodeSet the set of nodes (Node objects) to remove from the graph
     * @return true if all nodes were removed, false is one or more nodes were not removed
     * Nodes to remove must be valid, otherwise false is returned and no node is removed from the set.
     * A valid node is a node that does exist within the Graph object
     */
    public boolean removeNodes(Set<Node> nodeSet) {

        // First check if there is a problem with any given edge before attempting to add it to the adj list
        if (isValidNodes(nodeSet)) {

            for (Node curNode : nodeSet) {

                // Remove node from map
                this.adjacencyMap.remove(curNode);

                // Also have to iterate through all the map and remove any references to the node
                for (ArrayList<Node> neighbourNodes : this.adjacencyMap.values()) {

                    neighbourNodes.remove(curNode);
                }

            }

            return true;
        }

        return false;

    }

    /**
     * Gets the neighbour reference list for the given node (i.e., all child nodes of this parent node)
     * @param node the Node object to get the list from
     * @return an ArrayList (Node) object containing all child node's. Null if the specified parent node is invalid.
     * A invalid node is a node that does not exist within the Graph object
     */
    public List<Node> getNodeNeighbourList(Node node) {

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

    /**
     * Gets the neighbour reference list for the given node (i.e., all child nodes of this parent node)
     * @param nodeName the node name as a String to get the list from
     * @return an ArrayList (Node) object containing all child node's. Null if the specified parent node is invalid.
     * A invalid node is a node that does not exist within the Graph object
     */
    public List<Node> getNodeNeighbourList(String nodeName) {

        for (Node curNode : this.adjacencyMap.keySet()) {

            if (curNode.getNodeName() == nodeName) {

                return this.adjacencyMap.get(curNode);

            }

        }

        return null;

    }

    /**
     * Iterate through all parent nodes
     * @return a Node object
     */
    public Iterator<Node> getParentNodeIterator() {
        return new Iterator<Node>() {
            Iterator<Node> parentNodeIterator = adjacencyMap.keySet().iterator();

            @Override
            public boolean hasNext() {
                return (parentNodeIterator.hasNext());
            }

            @Override
            public Node next() {

                return parentNodeIterator.next();


            }
        };
    }

    /**
     * Iterate through all neighbour node lists
     * @return an ArrayList (Node) object containing all child node's for the given iteration
     */
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

    // Method to check if a given node exists within the adj map
    private boolean doesExistInAdj(Node testNode) {

        for (Node curNode : adjacencyMap.keySet()) {

            if (curNode == testNode) {

                return true;

            }

        }

        return false;

    }

    // Overrided method from above
    private boolean doesExistInAdj(String testNode) {

        for (Node curNode : adjacencyMap.keySet()) {

            if (curNode.getNodeName() == testNode) {

                return true;

            }

        }

        return false;

    }


    /**
     * Checks if the given two nodes share an edge (directed)
     * @param firstNode the first Node object in the edge
     * @param secondNode the second Node object in the edge
     * @return true if (firstNode,secondNode) exists as an edge, false otherwise (including invalid edges).
     * An invalid edge is an edge that does not exist within the Graph object
     */
    // Method to check if two Nodes share an edge in the order of firstNode to secondNode
    public boolean hasEdgeBetween(Node firstNode, Node secondNode) {

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


    /**
     * Checks if the given two nodes share an edge (directed)
     * @param firstNode the first node string in the edge
     * @param secondNode the second node string in the edge
     * @return true if (firstNode,secondNode) exists as an edge, false otherwise (including invalid edges).
     * An invalid edge is an edge that does not exist within the Graph object
     */
    // Method to check if two Nodes share an edge in the order of firstNode to secondNode
    public boolean hasEdgeBetween(String firstNode, String secondNode) {

        // Check if both nodes exist within the graph
        if (doesExistInAdj(firstNode) && doesExistInAdj(secondNode)) {

            // Loop through each neighbour list for all nodes
            for (Node curNode : adjacencyMap.keySet()) {

                // Check if the current node we are looping on is the first node specified
                if (curNode.getNodeName() == firstNode) {

                    // Loop through it's neighbour set
                    for (Node testNode : adjacencyMap.get(curNode)) {

                        // return true if the second node exists with in the neighbour set
                        if (testNode.getNodeName() == secondNode) {

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
