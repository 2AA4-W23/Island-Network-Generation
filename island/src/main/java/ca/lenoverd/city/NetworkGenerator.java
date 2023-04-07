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


    /**
     * Generate a graph representation of a mesh
     * @param mesh the mesh as a Polymesh object
     * @return a NetworkGenerator object containing the graph representation of the Polymesh object
     * @Note Nodes are represented as vertices and edges are segments connecting said vertices
     */
    public NetworkGenerator(PolyMesh<Polygons> mesh) {

        this.mesh = mesh;

        Set<Node> nodeSet = new HashSet<>();
        Set<Edge> edgeSet = new HashSet<>();

        for (Polygons curPoly : mesh) {

            // Get list of verticies in polygon
            for (Structs.Vertex curVertex : curPoly.getVerticesList()) {

                // idVertex is for the vertex index inside the polygon

                // Each vertex corresponds to a node in the graph
                Node newNode = new Node(curVertex.getX()+":"+curVertex.getY());

                // Create a new Property to store the vertex
                // The name will be the index of the polygon concatenated with the index of the vertex
                Property<Structs.Vertex> newProp = new Property<>(curVertex.getX()+":"+curVertex.getY(),curVertex);

                // Apply property to node
                newNode.addProperty(newProp);

                // Add node to set
                nodeSet.add(newNode);

                // Find all neighbours of the vertex by looping through segments (to add as edges)'
                for(Structs.Segment curSeg : curPoly.getSegmentsList()) {

                    // Get the first vertex of the segment
                    Structs.Vertex vertex1 = curPoly.getVerticesList().get(curSeg.getV1Idx());
                    Structs.Vertex vertex2 = curPoly.getVerticesList().get(curSeg.getV2Idx());

                    // Check if it's equal to curVertex
                    if (vertex1.equals(curVertex)) {

                        // Construct a new node
                        Node neighbourNode = new Node(vertex2.getX()+":"+vertex2.getY());

                        // Construct property with centroid
                        Property<Structs.Vertex> neighbourProp = new Property<>(vertex2.getX()+":"+vertex2.getY(),vertex2);

                        // Apply property to node
                        neighbourNode.addProperty(neighbourProp);

                        // Construct edge
                        Edge newEdge = new Edge(newNode,neighbourNode);

                        // Create a property with the distance between the two polygons as the weight value of the edge
                        Property<Double> weightValue = new Property<>("weightValue",getDistanceBetweenPoints(vertex1,vertex2));

                        // Apply property to edge
                        newEdge.addProperty(weightValue);

                        // Add node and edge to sets
                        nodeSet.add(neighbourNode);
                        edgeSet.add(newEdge);

                    }

                }

            }

        }

        // Create an undirected graph with the given nodes and edges
        this.islandGraph = new UndirectedGraph(nodeSet,edgeSet);


    }

    /**
     * Generate a star network for a given set of city polygons
     * @param cityGenerator the generator object which contains the selected city polygons
     * @Note One can call getPathConnections to get specific vertices which are apart of the star network
     */
    public void createStarNetwork(CityGenerator cityGenerator) {

        try {

            // For every city, calculate the shortest paths to all cities
            // The city with the least weight for all paths added up is the "hub"

            List<Node> cityNodes = new ArrayList<>();

            // Get all city centroids
            Iterator<Node> parentNodes = this.islandGraph.getParentNodeIterator();

            while (parentNodes.hasNext()) {

                Node testNode = parentNodes.next();

                // Get x and y of the vertex
                String[] data = testNode.getNodeName().split(":");

                Structs.Vertex testVert = Structs.Vertex.newBuilder().setX(Double.valueOf(data[0])).setY(Double.valueOf(data[1])).build();

                // Check if the centroid is a city
                if (cityGenerator.isVertexACity(testVert)) {

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

    // Get all adjacent connections for all vertices in a polygon
    // For the returned list, the index corresponds to the index of vertices found within polygons
    /**
     * Get all path connections that a given polygon contains
     * @param polygon the polygon to test
     * @return a Map containing the corresponding vertex and the vertices it connects to via paths
     */
    public Map<Structs.Vertex,List<Structs.Vertex>> getPathConnections(Polygons polygon) {

        HashMap<Structs.Vertex,List<Structs.Vertex>> allAdjacentConnections = new HashMap<>();

        for (Structs.Vertex curVertex : polygon.getVerticesList()) {

            List<Structs.Vertex> adjacentConnections = new ArrayList<>();

            for (List<Node> curPath : this.starNetworkNodes) {

                int index = 0;
                for (Node curNode : curPath) {

                    try {

                        // Get node vertex property
                        Structs.Vertex curNodeVertex = (Structs.Vertex) curNode.getProperty(curNode.getNodeName(),Structs.Vertex.newBuilder().build()).getValue();

                        // Check if it's equivalent to the given vertex
                        if (curVertex.getX() == curNodeVertex.getX() && curVertex.getY() == curNodeVertex.getY()) {

                            // If so, said get nodes before and after specified vertex (if applicable)

                            if (index-1 >= 0) {
                                // Get previous node
                                Node previousNode = curPath.get(index-1);
                                Structs.Vertex previousNodeVertex = (Structs.Vertex) previousNode.getProperty(previousNode.getNodeName(),Structs.Vertex.newBuilder().build()).getValue();
                                adjacentConnections.add(previousNodeVertex);
                            }

                            if (curPath.size() > index+1) {

                                // Get next node
                                Node nextNode = curPath.get(index+1);
                                Structs.Vertex nextNodeVertex = (Structs.Vertex) nextNode.getProperty(nextNode.getNodeName(),Structs.Vertex.newBuilder().build()).getValue();
                                adjacentConnections.add(nextNodeVertex);
                            }

                        }


                    } catch (Exception e) {

                        System.out.println(e.getMessage());

                    }

                    index++;

                }

            }

            if (adjacentConnections.size() > 0) {

                allAdjacentConnections.put(curVertex,adjacentConnections);

            }

        }
        return allAdjacentConnections;

    }

    /**
     * Check if a given vertex is apart of a road path
     * @param vertex the vertex to test
     * @return true if the vertex is apart of a road, false otherwise
     */
    public boolean isVertexARoad(Structs.Vertex vertex) {

        // Loop through all paths
        for (List<Node> curPath : this.starNetworkNodes) {

            // Loop through each node in a given path
            for (Node curNode : curPath) {

                // Check to make sure node is NOT a city (i.e. the first node and last node in the list are cities)
                if (curNode != curPath.get(0) && curNode != curPath.get(curPath.size()-1)) {

                    try {

                        // Get node vertex property
                        Structs.Vertex curNodeVertex = (Structs.Vertex) curNode.getProperty(curNode.getNodeName(),Structs.Vertex.newBuilder().build()).getValue();

                        // Check if it's equivalent to the given vertex
                        if (vertex.getX() == curNodeVertex.getX() && vertex.getY() == curNodeVertex.getY()) {

                            // If so, said vertex is apart of a road
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

    /**
     * Apply the generated star network to the mesh for visualization
     */
    public void applyStarNetworkToMesh() {

        for (Polygons curPoly : this.mesh) {

            // Get all path connections for ths given polygon
            Map<Structs.Vertex,List<Structs.Vertex>> adjacentPositionsList = this.getPathConnections(curPoly);

            // Get all vertices
            List<Structs.Vertex> vertList = curPoly.getVerticesList();

            // Loop through all vertices within the polygon
            for (int vertIndex = 0; vertIndex < vertList.size(); vertIndex++) {

                // Get vertex
                Structs.Vertex curVert = vertList.get(vertIndex);

                // Check if the vertex has a path
                if (adjacentPositionsList.containsKey(curVert)) {

                    // Construct string
                    String listData = "";

                    // Get all connections for that vertex
                    List<Structs.Vertex> connectionsToCurVert = adjacentPositionsList.get(curVert);

                    for (int index = 0; index < connectionsToCurVert.size(); index++) {

                        Structs.Vertex adjVert = connectionsToCurVert.get(index);
                        listData+=adjVert.getX()+":"+ adjVert.getY();

                        if (connectionsToCurVert.size()-1 >= index+1) {

                            listData+=",";

                        }
                    }

                    // Apply property to vertex
                    curPoly.addPropertyToVertex(vertIndex,"roadConnections",listData);

                }

            }

        }

    }

    private double getDistanceBetweenPoints(Structs.Vertex vertex1, Structs.Vertex vertex2) {

        return Math.sqrt(Math.pow((vertex1.getX() - vertex2.getX()), 2) + Math.pow((vertex1.getY() - vertex2.getY()), 2));

    }

}
