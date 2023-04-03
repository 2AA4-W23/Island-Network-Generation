package lenoverd.graph.pathfinder;

import lenoverd.graph.Edge;
import lenoverd.graph.DirectedGraph;
import lenoverd.graph.Graph;
import lenoverd.graph.Node;
import lenoverd.graph.exceptions.NodePropertyNotFoundException;

import java.util.*;

public class Dijkstra implements PathFinder {

    private Graph referenceGraph;
    private String weightPropertyName;
    private String weightValueName;

    private HashMap<Node,Double> shortestDistances = new HashMap<>();
    private HashMap<Node,Node> previousNodes = new HashMap<>();

    public Dijkstra(Graph graph, String weightPropertyName, String weightValueName) {
        this.referenceGraph = graph;
        this.weightPropertyName = weightPropertyName;
        this.weightValueName = weightValueName;
    }

    @Override
    public List<Node> findPath(Node source, Node target) {

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
            System.out.println(e.getMessage());
        }
            return null;
    }

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

            path.add(previousNode);

            previousNode = this.previousNodes.get(previousNode);

        }

        // Add the source node and reverse the order of the list so the path becomes source -> target and not target -> source
        path.add(source);
        Collections.reverse(path);

        return path;


    }



}
