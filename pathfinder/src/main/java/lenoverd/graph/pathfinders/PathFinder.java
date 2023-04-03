package lenoverd.graph.pathfinders;

import lenoverd.graph.graphs.graphComponents.Node;

import java.util.Comparator;
import java.util.List;

public interface PathFinder {

    List<Node> findPath(Node source, Node target);

    Comparator<Node> getComparator();


}
