package lenoverd.graph;

import lenoverd.graph.exceptions.UnknownNodeException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
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

        Graph testGraph = new Graph(testNodes,testEdges);

        ArrayList<Node> testNeighbours3 = testGraph.getNodeNeighbourList(node3);
        ArrayList<Node> testNeighbours1 = testGraph.getNodeNeighbourList(node1);
        ArrayList<Node> testNeighbours2 = testGraph.getNodeNeighbourList(node2);
        ArrayList<Node> testNeighbours4 = testGraph.getNodeNeighbourList(node4);

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

        Graph testGraph = new Graph(testNodes,testEdges);

        // Add edges
        Set<Edge> testAddEdges = new HashSet<>();
        Edge edge24 = new Edge(node2,node4);
        Edge edge34 = new Edge(node3,node4);
        testAddEdges.add(edge24);
        testAddEdges.add(edge34);

        testGraph.addEdges(testAddEdges);

        ArrayList<Node> testNeighbours2 = testGraph.getNodeNeighbourList(node2);
        ArrayList<Node> testNeighbours3 = testGraph.getNodeNeighbourList(node3);

        assertEquals(testNeighbours2.size(),2);
        assertEquals(testNeighbours3.size(),1);

        // Remove edges
        Set<Edge> testRemoveEdges = new HashSet<>();
        testRemoveEdges.add(edge24);
        testGraph.removeEdges(testRemoveEdges);

        assertEquals(testNeighbours2.size(),1);
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

        Graph testGraph = new Graph(testNodes,testEdges);

        Node testNode = new Node("AddMe");

        Set<Node> testAddNodes = new HashSet<>();
        testAddNodes.add(testNode);

        // Add nodes
        assertEquals(testGraph.getNodeNeighbourList(testNode).size(),0);

        // Remove nodes
        testGraph.removeNodes(testAddNodes);
        assertEquals(testGraph.getNodeNeighbourList(testNode),null);


    }


}