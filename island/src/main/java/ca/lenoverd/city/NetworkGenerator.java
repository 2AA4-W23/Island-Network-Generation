package ca.lenoverd.city;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import lenoverd.graph.exceptions.NodePropertyNotFoundException;
import lenoverd.graph.graphs.Graph;
import lenoverd.graph.graphs.UndirectedGraph;
import lenoverd.graph.graphs.graphComponents.Edge;
import lenoverd.graph.graphs.graphComponents.Node;
import lenoverd.graph.pathfinders.Dijkstra;
import lenoverd.graph.pathfinders.PathFinder;
import lenoverd.graph.pathfinders.WeightedPathFinder;
import lenoverd.graph.property.Property;

import java.util.*;

public class NetworkGenerator {

    private Graph islandGraph;
    private PolyMesh<Polygons> mesh;

    private List<List<Node>> starNetworkNodes = new ArrayList<>();


    public NetworkGenerator(PolyMesh<Polygons> mesh) {

        this.mesh = mesh;

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

                    Polygons neighbourPoly = mesh.get(index1);

                    // Construct an new node
                    Node neighbourNode = new Node(String.valueOf(index1));

                    // Construct property with centroid
                    Property<Structs.Vertex> neighbourProp = new Property<>(String.valueOf(index1),neighbourPoly.getCentroid());

                    // Apply property to node
                    neighbourNode.addProperty(neighbourProp);

                    // Construct edge
                    Edge newEdge = new Edge(newNode,neighbourNode);

                    // Create a property with the distance between the two polygons as the weight value of the edge
                    Property<Double> weightValue = new Property<>("weightValue",getDistanceBetweenPolygons(curPoly,neighbourPoly));

                    // Apply property to edge
                    newEdge.addProperty(weightValue);

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

    public void createStarNetwork(CityGenerator cityGenerator) {

        try {

            // For every city, calculate the shortest paths to all cities
            // The city with the least weight for all paths added up is the "hub"

            List<Node> cityNodes = new ArrayList<>();

            // Get all city centroids
            Iterator<Node> parentNodes = this.islandGraph.getParentNodeIterator();

            while (parentNodes.hasNext()) {

                Node testNode = parentNodes.next();

                // Get centroid property
                Structs.Vertex centroid = (Structs.Vertex) testNode.getProperty(testNode.getNodeName(), Structs.Vertex.newBuilder().build()).getValue();

                // Check if the centroid is a city
                if (cityGenerator.isVertexACity(centroid)) {

                    // If it is, add to list of city nodes
                    cityNodes.add(testNode);

                }

            }

            // Initialize path finder and best values variables
            WeightedPathFinder pathFinder = new Dijkstra(this.islandGraph,"EdgeData","weightValue");
            double minWeight = Double.MAX_VALUE;
            Node hubNode = null;
            List<List<Node>> listOfBestPaths = new ArrayList<>();

            // Loop through each node and compare total weights
            for (Node curNode : cityNodes) {

                double weightsTotaled = 0;
                List<List<Node>> listOfCurrentPaths = new ArrayList<>();

                // Loop through all nodes and add total path weights
                for (Node testNode : cityNodes) {

                    if (!curNode.equals(testNode)) {

                        weightsTotaled += pathFinder.getDistanceValueFromSource(curNode,testNode);
                        listOfCurrentPaths.add(pathFinder.findPath(curNode,testNode));

                    }

                }

                // If the current city had a smaller weighting than what is currently known, update values
                if (weightsTotaled < minWeight) {

                    minWeight = weightsTotaled;
                    hubNode = curNode;
                    listOfBestPaths = listOfCurrentPaths;

                }

            }

            // Set star network nodes
            this.starNetworkNodes = listOfBestPaths;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public List<Structs.Vertex> getPathConnections(Structs.Vertex centroid) {

        List<Structs.Vertex> adjacentConnections = new ArrayList<>();

        for (List<Node> curPath : this.starNetworkNodes) {

            int index = 0;
            for (Node curNode : curPath) {

                try {

                    // Get node vertex property
                    Structs.Vertex curNodeCentroid = (Structs.Vertex) curNode.getProperty(curNode.getNodeName(),Structs.Vertex.newBuilder().build()).getValue();

                    // Check if it's equivalent to the given centroid
                    if (centroid.getX() == curNodeCentroid.getX() && centroid.getY() == curNodeCentroid.getY()) {

                        // If so, said centroid is a vertex

                        if (index-1 >= 0) {
                            // Get previous node
                            Node previousNode = curPath.get(index-1);
                            Structs.Vertex previousNodeCentroid = (Structs.Vertex) previousNode.getProperty(previousNode.getNodeName(),Structs.Vertex.newBuilder().build()).getValue();
                            adjacentConnections.add(previousNodeCentroid);
                        }

                        if (curPath.size() > index+1) {

                            // Get next node
                            Node nextNode = curPath.get(index+1);
                            Structs.Vertex nextNodeCentroid = (Structs.Vertex) nextNode.getProperty(nextNode.getNodeName(),Structs.Vertex.newBuilder().build()).getValue();
                            adjacentConnections.add(nextNodeCentroid);
                        }

                    }


                } catch (Exception e) {

                    System.out.println(e.getMessage());

                }

                index++;

            }

        }

        return adjacentConnections;

    }

    public boolean isCentroidARoad(Structs.Vertex centroid) {

        // Loop through all paths
        for (List<Node> curPath : this.starNetworkNodes) {

            // Loop through each node in a given path
            for (Node curNode : curPath) {

                // Check to make sure node is NOT a city (i.e. the first node and last node in the list are cities)
                if (curNode != curPath.get(0) && curNode != curPath.get(curPath.size()-1)) {

                    try {

                        // Get node vertex property
                        Structs.Vertex curNodeCentroid = (Structs.Vertex) curNode.getProperty(curNode.getNodeName(),Structs.Vertex.newBuilder().build()).getValue();

                        // Check if it's equivalent to the given centroid
                        if (centroid.getX() == curNodeCentroid.getX() && centroid.getY() == curNodeCentroid.getY()) {

                            // If so, said centroid is a vertex
                            return true;

                        }


                    } catch (Exception e) {

                        System.out.println(e.getMessage());

                    }

                }

            }

        }

        return false;

    }

    private double getDistanceBetweenPolygons(Polygons poly1, Polygons poly2) {

        return Math.sqrt(Math.pow((poly1.getCentroid().getX() - poly2.getCentroid().getX()), 2) + Math.pow((poly1.getCentroid().getY() - poly2.getCentroid().getY()), 2));

    }

}
