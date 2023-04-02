package lenoverd.graph.pathfinder;

import lenoverd.graph.Edge;
import lenoverd.graph.Graph;
import lenoverd.graph.Node;
import lenoverd.graph.Property;
import lenoverd.graph.exceptions.NodePropertyNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {

    @Test
    void findPath() throws NodePropertyNotFoundException {

        // All nodes
        Node node1 = new Node("1");
        Node node2 = new Node("2");
        Node node3 = new Node("3");
        Node node4 = new Node("4");
        Node node5 = new Node("5");
        Node node6 = new Node("6");

        // All edges

        // Node 1
        Edge edge16 = new Edge(node1,node6);
        Edge edge12 = new Edge(node1,node2);
        Edge edge13 = new Edge(node1,node3);

        Property<Double> weightValue16 = new Property<>("weightValue",1.0);
        Property<Double> weightValue12 = new Property<>("weightValue",0.0);
        Property<Double> weightValue13 = new Property<>("weightValue",4.0);

        edge16.addProperty(weightValue16);
        edge12.addProperty(weightValue12);
        edge13.addProperty(weightValue13);

        // Node 2
        Edge edge21 = new Edge(node2,node1);
        Edge edge23 = new Edge(node2,node3);
        Edge edge24 = new Edge(node2,node4);

        Property<Double> weightValue23 = new Property<>("weightValue",9.0);
        Property<Double> weightValue24 = new Property<>("weightValue",0.0);

        edge21.addProperty(weightValue12);
        edge23.addProperty(weightValue23);
        edge24.addProperty(weightValue24);


        // Node 3
        Edge edge31 = new Edge(node3,node1);
        Edge edge32 = new Edge(node3,node2);
        Edge edge34 = new Edge(node3,node4);
        Edge edge35 = new Edge(node3,node5);

        Property<Double> weightValue34 = new Property<>("weightValue",3.0);
        Property<Double> weightValue35 = new Property<>("weightValue",2.0);

        edge31.addProperty(weightValue13);
        edge32.addProperty(weightValue23);
        edge34.addProperty(weightValue34);
        edge35.addProperty(weightValue35);


        // Node 4
        Edge edge42 = new Edge(node4,node2);
        Edge edge43 = new Edge(node4,node3);

        edge42.addProperty(weightValue24);
        edge43.addProperty(weightValue34);

        // Node 5
        Edge edge53 = new Edge(node5,node3);

        edge53.addProperty(weightValue35);

        // Node 6
        Edge edge61 = new Edge(node6,node1);

        edge61.addProperty(weightValue16);

        Set<Node> nodes = new HashSet<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);
        nodes.add(node6);

        Set<Edge> edges = new HashSet<>();
        edges.add(edge16);
        edges.add(edge12);
        edges.add(edge13);
        edges.add(edge21);
        edges.add(edge23);
        edges.add(edge24);
        edges.add(edge31);
        edges.add(edge32);
        edges.add(edge34);
        edges.add(edge35);
        edges.add(edge42);
        edges.add(edge43);
        edges.add(edge53);
        edges.add(edge61);

        Graph testGraph = new Graph(nodes,edges);

        List<Node> path = new Dijkstra(testGraph,"EdgeData","weightValue").findPath(node1,node5);

        // The minimal weighted shortest path for this is 1-2-4-3-5
        assertEquals(path.get(0).getNodeName(),"1");
        assertEquals(path.get(1).getNodeName(),"2");
        assertEquals(path.get(2).getNodeName(),"4");
        assertEquals(path.get(3).getNodeName(),"3");
        assertEquals(path.get(4).getNodeName(),"5");


    }
}