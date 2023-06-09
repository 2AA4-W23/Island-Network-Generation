package lenoverd.graph.pathfinders;

import lenoverd.graph.graphs.graphComponents.Edge;
import lenoverd.graph.graphs.Graph;
import lenoverd.graph.graphs.graphComponents.Node;
import lenoverd.graph.exceptions.NodePropertyNotFoundException;
import lenoverd.graph.property.Property;

import java.util.*;

public class Dijkstra implements WeightedPathFinder {

    private Graph referenceGraph;
    private String weightPropertyName;
    private String weightValueName;

    private HashMap<Node,Double> shortestDistances = new HashMap<>();
    private HashMap<Node,Node> previousNodes = new HashMap<>();


    /**
     * Create a Dijkstra pathfinder
     * @param graph the graph to compute paths from
     * @param weightPropertyName the name of the edge object property that is stored in children nodes. This edge contains the weight value between two nodes.
     * @param weightValueName the actual name of the weight property stored inside the weight property edge. This is used to compare weights between nodes.
     * @return a Dijkstra object
     */
    public Dijkstra(Graph graph, String weightPropertyName, String weightValueName) {
        this.referenceGraph = graph;
        this.weightPropertyName = weightPropertyName;
        this.weightValueName = weightValueName;
    }

    /**
     * Compute the minimal weighted path between two nodes
     * @param source the source Node object (where the path starts from)
     * @param target the target Node object to get to from source
     * @return a List object containing the path between source (inclusive) and target (inclusive) IF the path exists, null otherwise
     */
    @Override
    public List<Node> findPath(Node source, Node target) {

        // We can save some calculation complexity by checking if the result was already computed
        if (this.shortestDistances.get(source) != null) {
            if (this.shortestDistances.get(source) == 0 && this.previousNodes.get(source).equals(source)) {

                return getPathFromMap(source,target);

            }
        }


        try {

            init(source);
            PriorityQueue<Node> pQueue = new PriorityQueue<>(getComparator());
            List<Node> visitedNodes = new ArrayList<>();

            pQueue.add(source);

            while (!pQueue.isEmpty()) {

                Node examineNode = pQueue.remove();

                // Get current smallest weight value
                double currentSmallestWeight = this.shortestDistances.get(examineNode);

                // Examine every edge that has not been visited by current node
                for (Node neighbourNode : this.referenceGraph.getNodeNeighbourList(examineNode)) {

                    if (!visitedNodes.contains(neighbourNode)) {

                        // Get the edge holding the weight value (we specified the name of the parameter in the graph adt)
                        Edge edge = (Edge) neighbourNode.getProperty(weightPropertyName+examineNode.getNodeName()+neighbourNode.getNodeName().strip(), new Edge(null, null)).getValue();

                        // Get weight value
                        double weightValue = (Double) edge.getProperty(weightValueName, 0.0).getValue();

                        // Add the weights of the examining node and the neighbour node as if we get to the neighbour node through the examining node
                        double weightValuePassingThroughNeighbour = weightValue + currentSmallestWeight;

                        if (weightValuePassingThroughNeighbour < this.shortestDistances.get(neighbourNode)) {

                            // If the weights are smaller than what is already present in the neighbour node, update the value
                            this.shortestDistances.put(neighbourNode,weightValuePassingThroughNeighbour);
                            // Update hashmap for previous nodes to specify the node we took to get to the neighbour node
                            this.previousNodes.put(neighbourNode,examineNode);

                            // Add neighbour node to the queue
                            pQueue.add(neighbourNode);

                        }
                    }
                }

                // Once the examineNode has been examined (went over all neighbours), mark it as visited
                visitedNodes.add(examineNode);
            }

            // Once queue is empty, we have all paths from source to any given node stored in the maps
            // Thus construct the path and return it
            return getPathFromMap(source,target);

        } catch (NodePropertyNotFoundException e){

        }
            return null;
    }

    // Pathfinder classes must implement a comparator class as they may have their own way to comparing weights
    @Override
    public Comparator<Node> getComparator() {
        return new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {

                double distanceo1 = shortestDistances.get(o1);
                double distanceo2 = shortestDistances.get(o2);

                if (distanceo1 < distanceo2) {
                    return -1;
                } else if (distanceo1 > distanceo2) {
                    return 1;
                } else if (distanceo1 - distanceo2 < 0.0000001) {
                    return 0;
                }

                return 0;
            }
        };
    }

    /**
     * Get the distance to any target node in the graph from the specified source node
     * @param target the target Node object within the graph
     * @return a double value containing the minimum distance to get from the source node to the specified target node. Null if path is invalid
     */
    public Double getDistanceValueFromSource(Node source, Node target) {

        // We can decrease the computation complexity by looking at what is already stored
        // If source is 0 and the previous node is itself, then the shortest paths is currently set to this node
        // Thus we do not need to re-run the algorithm
        if (this.shortestDistances.get(source) != null) {

            if (this.shortestDistances.get(source) == 0 && this.previousNodes.get(source).equals(source)) {

                return this.shortestDistances.get(target);

            } else {
                findPath(source,target);
            }

            return this.shortestDistances.get(target);

        } else {
            findPath(source,target);
        }

        return this.shortestDistances.get(target);

    }

    // Method to initialize values/variables
    private void init(Node source) {

        // Clear maps
        this.shortestDistances.clear();
        this.previousNodes.clear();

        Iterator<Node> parentNodeIterator = this.referenceGraph.getParentNodeIterator();

        while(parentNodeIterator.hasNext()) {

            // Get parent node
            Node curParentNode = parentNodeIterator.next();

            if (curParentNode == source) {
                shortestDistances.put(curParentNode,0.0);
            } else {
                // Add to shortestDistances and previousNodes hashmap with default values
                shortestDistances.put(curParentNode,Double.MAX_VALUE);
            }

            previousNodes.put(curParentNode,curParentNode);

        }

    }

    // Method to construct the path from the two nodes after processing
    private List<Node> getPathFromMap(Node source, Node target) {

        List<Node> path = new ArrayList<>();

        // Add the target node
        path.add(target);

        // Get the previous node that was used to get to target
        Node previousNode = this.previousNodes.get(target);

        // Keep adding all previous nodes used from one another until hitting the source node
        while (previousNode != source) {

            // Check if the previous node is equivalent to itself (and we already checked that the previous node was not source)
            if (this.previousNodes.get(previousNode) == previousNode) {

                // This means the graph does not contain a valid path between source and target
                return null;

            }

            path.add(previousNode);

            previousNode = this.previousNodes.get(previousNode);

        }

        // Add the source node and reverse the order of the list so the path becomes source -> target and not target -> source
        path.add(source);
        Collections.reverse(path);

        return path;


    }



}
