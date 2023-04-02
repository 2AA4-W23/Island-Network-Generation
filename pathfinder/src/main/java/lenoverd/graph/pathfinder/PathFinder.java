package lenoverd.graph.pathfinder;

import lenoverd.graph.Node;

import java.util.Comparator;
import java.util.List;

public interface PathFinder {

    List<Node> findPath(Node source, Node target);

    Comparator<Node> getComparator();


}
