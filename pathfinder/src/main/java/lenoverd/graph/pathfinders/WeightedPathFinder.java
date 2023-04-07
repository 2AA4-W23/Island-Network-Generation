package lenoverd.graph.pathfinders;

import lenoverd.graph.graphs.graphComponents.Node;

public interface WeightedPathFinder extends PathFinder {

    Double getDistanceValueFromSource(Node source, Node target);

}
