package lenoverd.graph.pathfinder;

import lenoverd.graph.Node;

import java.util.List;

public interface PathFinder {

    List<Node> findPath(Node source, Node target);


}
