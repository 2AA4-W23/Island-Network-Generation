package lenoverd.graph.pathfinder;

import lenoverd.graph.Graph;
import lenoverd.graph.Node;

import java.util.HashMap;
import java.util.List;

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



        return null;
    }

    private void init() {



        // Set all weight values to infinity

    }



}
