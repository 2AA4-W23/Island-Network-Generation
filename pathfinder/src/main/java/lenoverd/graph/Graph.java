package lenoverd.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface Graph {

    boolean addEdges(Set<Edge> edgeSet);
    boolean removeEdges(Set<Edge> edgeSet);

    boolean addNodes(Set<Node> nodeSet);
    boolean removeNodes(Set<Node> nodeSet);

    List<Node> getNodeNeighbourList(Node node);
    List<Node> getNodeNeighbourList(String nodeName);

    Iterator<Node> getParentNodeIterator();
    Iterator<ArrayList<Node>> iterator();

    boolean hasEdgeBetween(Node firstNode, Node secondNode);
    boolean hasEdgeBetween(String firstNode, String secondNode);



}
