package ca.lenoverd.city;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import lenoverd.graph.graphs.Graph;
import lenoverd.graph.graphs.UndirectedGraph;
import lenoverd.graph.graphs.graphComponents.Edge;
import lenoverd.graph.graphs.graphComponents.Node;
import lenoverd.graph.property.Property;

import java.util.*;

public class NetworkGenerator {

    private Graph islandGraph;


    public NetworkGenerator(PolyMesh<Polygons> mesh, CityGenerator cityGenerator) {

        int idNode = 0;

        Set<Node> nodeSet = new HashSet<>();
        Set<Edge> edgeSet = new HashSet<>();

        for (Polygons curPoly : mesh) {

            // Create a new node to represent the polygon
            Node newNode = new Node(String.valueOf(idNode));

            // Create a new Property to store the centroid in
            Property<Structs.Vertex> newProp = new Property<>(String.valueOf(idNode),curPoly.getCentroid());

            // Apply property to node
            newNode.addProperty(newProp);

            // Add node to set
            nodeSet.add(newNode);

            // Find all neighbours of the polygon to add as edges
            for (int index1 = 0; index1 < mesh.size(); index1++) {

                // Since the idNode corresponds to the current index of curPoly in the mesh list, we can use it to check for neighbours
                if (mesh.isNeighbor(idNode,index1)) {

                    // Construct an new node
                    Node neighbourNode = new Node(String.valueOf(index1));

                    // Construct property with centroid
                    Property<Structs.Vertex> neighbourProp = new Property<>(String.valueOf(index1),mesh.get(index1).getCentroid());

                    // Apply property to node
                    neighbourNode.addProperty(neighbourProp);

                    // Construct edge
                    Edge newEdge = new Edge(newNode,neighbourNode);

                    // Add node and edge to sets
                    nodeSet.add(neighbourNode);
                    edgeSet.add(newEdge);


                }

            }

            // Increment idNode
            idNode++;

        }

        // Create an undirected graph with the given nodes and edges
        this.islandGraph = new UndirectedGraph(nodeSet,edgeSet);




    }

}
