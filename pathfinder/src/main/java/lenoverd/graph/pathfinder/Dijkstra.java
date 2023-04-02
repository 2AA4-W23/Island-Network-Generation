package lenoverd.graph.pathfinder;

import lenoverd.graph.Edge;
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

        init(source);
        PriorityQueue<Node> pQueue = new PriorityQueue<>(getComparator());
        List<Node> visitedNodes = new ArrayList<>();

        pQueue.add(source);

        while (!pQueue.isEmpty()) {

            Node examineNode = pQueue.remove();

            // Examine every edge that has not been visited by current node
            for (Node neighbourNode : this.referenceGraph.getNodeNeighbourList(examineNode)) {

                if (!visitedNodes.contains(neighbourNode)) {

                    try {

                        // Get the edge holding the weight value
                        Edge edge = (Edge) neighbourNode.getProperty(weightPropertyName,new Edge(null,null)).getValue();

                        // Get weight value
                        double weightValue = (Double) edge.getProperty(weightValueName,0.0).getValue();

                    } catch (NodePropertyNotFoundException e) {

                    }

                }

            }


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

    private void init(Node source) {

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



}
