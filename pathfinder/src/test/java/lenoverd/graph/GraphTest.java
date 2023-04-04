package lenoverd.graph;

import lenoverd.graph.graphs.DirectedGraph;
import lenoverd.graph.graphs.Graph;
import lenoverd.graph.graphs.UndirectedGraph;
import lenoverd.graph.graphs.graphComponents.Edge;
import lenoverd.graph.graphs.graphComponents.Node;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    Node node1 = new Node("1");
    Node node2 = new Node("2");
    Node node3 = new Node("3");
    Node node4 = new Node("4");

    Edge edge12 = new Edge(node1,node2);
    Edge edge13 = new Edge(node1,node3);
    Edge edge21 = new Edge(node2,node1);
    Edge edge42 = new Edge(node4,node2);

    @Test
    void graphCreation() {

        Set<Node> testNodes = new HashSet<>();
        testNodes.add(node1);
        testNodes.add(node2);
        testNodes.add(node3);
        testNodes.add(node4);

        Set<Edge> testEdges = new HashSet<>();
        testEdges.add(edge12);
        testEdges.add(edge13);
        testEdges.add(edge21);
        testEdges.add(edge42);

        Graph testGraph = new DirectedGraph(testNodes,testEdges);

        List<Node> testNeighbours3 = testGraph.getNodeNeighbourList(node3);
        List<Node> testNeighbours1 = testGraph.getNodeNeighbourList(node1);
        List<Node> testNeighbours2 = testGraph.getNodeNeighbourList(node2);
        List<Node> testNeighbours4 = testGraph.getNodeNeighbourList(node4);

        assertEquals(testNeighbours3.size(),0);
        assertEquals(testNeighbours1.size(),2);
        assertEquals(testNeighbours2.size(),1);
        assertEquals(testNeighbours4.size(),1);

        assertEquals(testGraph.getNodeNeighbourList(new Node("test")),null);


    }

    @Test
    void addAndRemoveEdges() {

        Set<Node> testNodes = new HashSet<>();
        testNodes.add(node1);
        testNodes.add(node2);
        testNodes.add(node3);
        testNodes.add(node4);

        Set<Edge> testEdges = new HashSet<>();
        testEdges.add(edge12);
        testEdges.add(edge13);
        testEdges.add(edge21);
        testEdges.add(edge42);

        Graph testGraph = new DirectedGraph(testNodes,testEdges);

        // Add edges
        Set<Edge> testAddEdges = new HashSet<>();
        Edge edge24 = new Edge(node2,node4);
        Edge edge34 = new Edge(node3,node4);
        testAddEdges.add(edge24);
        testAddEdges.add(edge34);

        testGraph.addEdges(testAddEdges);

        List<Node> testNeighbours2 = testGraph.getNodeNeighbourList(node2);
        List<Node> testNeighbours3 = testGraph.getNodeNeighbourList(node3);

        assertEquals(testNeighbours2.size(),2);
        assertEquals(testNeighbours3.size(),1);

        // Remove edges
        Set<Edge> testRemoveEdges = new HashSet<>();
        testRemoveEdges.add(edge24);
        testGraph.removeEdges(testRemoveEdges);

        // Check if shallow copy list worked as intended
        assertEquals(testNeighbours2.size(),2);
        assertEquals(testGraph.getNodeNeighbourList(node2).size(),1);
        // Check that you cannot remove a now non-existent edge
        assertEquals(testGraph.removeEdges(testRemoveEdges),false);


    }

    @Test
    void addAndRemoveNodes() {

        Set<Node> testNodes = new HashSet<>();
        testNodes.add(node1);
        testNodes.add(node2);
        testNodes.add(node3);
        testNodes.add(node4);

        Set<Edge> testEdges = new HashSet<>();
        testEdges.add(edge12);
        testEdges.add(edge13);
        testEdges.add(edge21);
        testEdges.add(edge42);

        Graph testGraph = new DirectedGraph(testNodes,testEdges);

        Node testNode = new Node("AddMe");

        Set<Node> testAddNodes = new HashSet<>();
        testAddNodes.add(testNode);

        // Add nodes
        assertEquals(testGraph.addNodes(testAddNodes),true);
        assertEquals(testGraph.getNodeNeighbourList(testNode).size(),0);
        // Check if we cannot add the same node again
        assertEquals(testGraph.addNodes(testAddNodes),false);

        // Remove nodes
        testGraph.removeNodes(testAddNodes);
        assertEquals(testGraph.getNodeNeighbourList(testNode),null);


    }

    @Test
    void checkEdges() {


        Set<Node> testNodes = new HashSet<>();
        testNodes.add(node1);
        testNodes.add(node2);
        testNodes.add(node3);
        testNodes.add(node4);

        Set<Edge> testEdges = new HashSet<>();
        testEdges.add(edge12);
        testEdges.add(edge13);
        testEdges.add(edge21);
        testEdges.add(edge42);

        Graph testGraph = new DirectedGraph(testNodes,testEdges);

        assertEquals(testGraph.hasEdgeBetween("1","2"),true);
        assertEquals(testGraph.hasEdgeBetween("1","4"),false);

        assertEquals(testGraph.hasEdgeBetween(node1,node2),true);
        assertEquals(testGraph.hasEdgeBetween(node1,node4),false);

        // Check if a node contains an edge to itself
        assertEquals(testGraph.hasEdgeBetween("4","4"),false);

    }

    @Test
    void UndirectedGraphCreation() {
        Set<Node> testNodes = new HashSet<>();
        testNodes.add(node1);
        testNodes.add(node2);
        testNodes.add(node3);
        testNodes.add(node4);

        Set<Edge> testEdges = new HashSet<>();
        testEdges.add(edge12);
        testEdges.add(edge13);
        testEdges.add(edge42);

        Graph undirTest = new UndirectedGraph(testNodes,testEdges);

        assertEquals(undirTest.hasEdgeBetween("1","2"),true);
        assertEquals(undirTest.hasEdgeBetween("2","1"),true);

        assertEquals(undirTest.hasEdgeBetween("1","3"),true);
        assertEquals(undirTest.hasEdgeBetween("3","1"),true);

        assertEquals(undirTest.hasEdgeBetween("4","2"),true);
        assertEquals(undirTest.hasEdgeBetween("2","4"),true);

        // Check if a node contains an edge to itself
        assertEquals(undirTest.hasEdgeBetween("4","4"),false);

    }

    @Test
    void UndirectedGraphAddAndRemoveEdges() {

        Set<Node> testNodes = new HashSet<>();
        testNodes.add(node1);
        testNodes.add(node2);
        testNodes.add(node3);
        testNodes.add(node4);

        Set<Edge> testEdges = new HashSet<>();
        testEdges.add(edge12);
        testEdges.add(edge13);
        testEdges.add(edge42);

        Graph undirTest = new UndirectedGraph(testNodes,testEdges);

        // Test adding a duplicate edge
        Set<Edge> duplicateEdge = new HashSet<>();
        duplicateEdge.add(edge12);
        assertEquals(undirTest.addEdges(duplicateEdge),false);

        // Test adding a new edge
        assertEquals(undirTest.hasEdgeBetween("3","2"),false);
        assertEquals(undirTest.hasEdgeBetween("2","3"),false);
        duplicateEdge.clear();
        Edge newEdge = new Edge(node3,node2);
        duplicateEdge.add(newEdge);
        undirTest.addEdges(duplicateEdge);
        assertEquals(undirTest.hasEdgeBetween("3","2"),true);
        assertEquals(undirTest.hasEdgeBetween("2","3"),true);

        // Test removing an edge
        undirTest.removeEdges(duplicateEdge);
        assertEquals(undirTest.hasEdgeBetween("3","2"),false);
        assertEquals(undirTest.hasEdgeBetween("2","3"),false);
    }

    @Test
    void UndirectedGraphAddAndRemoveNodes() {

        Set<Node> testNodes = new HashSet<>();
        testNodes.add(node1);
        testNodes.add(node2);
        testNodes.add(node3);
        testNodes.add(node4);

        Set<Edge> testEdges = new HashSet<>();
        testEdges.add(edge12);
        testEdges.add(edge13);
        testEdges.add(edge42);

        Graph undirTest = new UndirectedGraph(testNodes,testEdges);

        // Add new node
        Set<Node> addNodes = new HashSet<>();
        Node newNode = new Node("newNode");
        addNodes.add(newNode);
        undirTest.addNodes(addNodes);

        // Check nothing has happened yet
        assertEquals(undirTest.hasEdgeBetween("newNode","2"),false);
        assertEquals(undirTest.hasEdgeBetween("2","newNode"),false);

        // Add edge
        Edge newEdge = new Edge(newNode,node2);
        Set<Edge> addEdges = new HashSet<>();
        addEdges.add(newEdge);
        undirTest.addEdges(addEdges);

        // Check if the change applied
        assertEquals(undirTest.hasEdgeBetween("newNode","2"),true);
        assertEquals(undirTest.hasEdgeBetween("2","newNode"),true);

        // Check removal
        undirTest.removeNodes(addNodes);
        assertEquals(undirTest.hasEdgeBetween("newNode","2"),false);
        assertEquals(undirTest.hasEdgeBetween("2","newNode"),false);



    }


}