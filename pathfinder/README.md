# Pathfinder Library

  - Written by Davis Lenover [lenoverd@mcmaster.ca]

## Documentation
### graphComponents
#### Node (extends PropertiesHolder)
|       Method       | Description                                                       | Additional Notes                                                                                         |
|:------------------:|-------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| Node(String name)  | Instantiate a new node with a given name                          |                                                                                                          |
|   getNodeName()    | Get the node name as a string                                     |                                                                                                          |
| equals(Object obj) | Check if two nodes are equal                                      | Returns true if both nodes have the same name. False otherwise. It does NOT take into account properties |
|     hashCode()     | Returns an integer representing the hashcode of the Node instance | hashcode is equivalent to the hashcode of the instance name                                              |
#### Edge (extends PropertiesHolder)
|            Method            | Description                                                       | Additional Notes                                                                                                         |
|:----------------------------:|-------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| Edge(Node node1, Node node2) | Instantiate a new edge with two nodes (in-order)                  |                                                                                                                          |
|        getFirstNode()        | Get the first node in the edge                                    |                                                                                                                          |
|       getSecondNode()        | Get the second node in the edge                                   |                                                                                                                          |
| equals(Object obj) | Check if two edges are equal                                      | Returns true if both edges contain the same nodes (in-order). False otherwise. It does NOT take into account properties  |
|          hashCode()          | Returns an integer representing the hashcode of the Edge instance | hashcode is equivalent to the hashcode of both node names combined (in-order)                                            |

### property
#### Property
|             Method             | Description                                                           | Additional Notes                                                                                                      |
|:------------------------------:|-----------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| Property(String name, T value) | Instantiate a new Property object with a given name and a given value | value can be any instance that generalizes from Object                                                                |
|           getValue()           | Get the value within the Property object                              | One must be aware the type value is returning and cast accordingly                                                    |
|           getName()            | Get the name of the Property object as a String                       |                                                                                                                       |
|      changeValue(T value)      | change the value within the property                                  | value must be the same as type T specified on creation of Property object                                             |
|       equals(Object obj)       | Compare Property object's                                             | returns true if the object is: a Property object, has the same value type (class), has the same name. False otherwise |
#### PropertiesHolder
|                       Method                        | Description                          | Additional Notes                                                                                                                                                                                                                                                                                       |
|:---------------------------------------------------:|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|          addProperty(Property<T> property)          | Add a Property object to the set     | Returns true if the property is added to the properties list. Only one property with the same name and value type can exist in a set at any given time, if a Property object is added and the given set already contains such NodeProperty (same value and type), the existing property is overwritten |
| removeProperty(String propertyName, T propertyType) | Remove a Property object from the set | Returns true if the property is removed from the properties list. propertyType does NOT have to be the same value, just the same type as value                                                                                                                                                         |
|  getProperty(String propertyName, T propertyType)   | Get a property object from the set   | Returns the property object. This object is LIVE, meaning making changes to the returned object will automatically update the property in the node                                                                                                                                                     |
|                 getPropertiesSize()                 | Get the size of set                  |                                                                                                                                                                                                                              |
|                 getAllProperties()                  | Get all properties as a List         |                                                                                                                                                                                 |

### graphs
#### Graph
|                       Method                        | Description                                                                                     | Additional Notes                                                                                                                                                                                                                                                                    |
|:---------------------------------------------------:|-------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|          addEdges(Set<Edge> edgeSet)          | Adds edges to a graph                                                                           | Returns true if all edges were added, false is one or more edges were not added. If any edges contain unspecified nodes in the graph, the method will return false and no edge will be added regardless whether some edges contained valid nodes.                                   |
| removeEdges(Set<Edge> edgeSet) | Remove edges from a graph                                                                       | Returns true if all edges were removed, false is one or more edges were not valid. If any edges contain unspecified nodes in the graph, the method will return false and no edge will be removed regardless whether some edges were valid.                                          |
|  addNodes(Set<Node> nodeSet)   | Adds nodes to a graph                                                                           | Returns true if all nodes were added, false is one or more nodes were not added. Nodes to add must be invalid, otherwise false is returned and no node is added from the set                                                                                                        |
|                 removeNodes(Set<Node> nodeSet)                 | Removes nodes from a graph and the neighbour reference list for the given node.                 | Returns true if all nodes were removed, false is one or more nodes were not removed. This consequently removes edges where the first node within the edge is the node to be removed. Nodes to remove must be valid, otherwise false is returned and no node is removed from the set. |
|                 getNodeNeighbourList(Node node)                | Gets the neighbour reference list for the given node (i.e., all child nodes of this parent node) | Implementations should take protecting graph structure into consideration                                                                                                                                                                                                           |
|                 getNodeNeighbourList(String nodeName)                 | Overidden method from above                                                                     |                                                                                                                                                                                                                                                                                     |
|                 getParentNodeIterator()                 | Iterate through all parent nodes                                                                |                                                                                                                                                                                                                                                                                     |
|                 iterator()                  | Iterate through all neighbour node lists                                                        |    Implementations should take protecting graph structure into consideration                                                                                                                                                                                                                                                                                  |
|                 hasEdgeBetween(Node firstNode, Node secondNode)                  | Checks if the given two nodes share an edge                                                     |                                                                                                                                                                                                                                                                                     |
|                 hasEdgeBetween(String firstNode, String secondNode)                 | Overidden method from above                                                                     |                                                                                                                                                                                                                                                                                     |
- Note that graph structure varies and is recommended to check specific implementation for further details of given methods

### pathfinders
#### PathFinder
|       Method       | Description                               | Additional Notes                                                                                                                                          |
|:------------------:|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| findPath(Node source, Node target)  | Compute the a path between two nodes      | Returns a List object containing the path between source (inclusive) and target (inclusive) IF the path exists, null otherwise                            |
|   getComparator()    | Get a Node comparator for computing paths | Classes may need to compare Nodes in a variety of ways, thus comparator asks that classes provide a concrete Comparator of how they plan to compare nodes |
#### WeightedPathFinder (extends PathFinder)
|       Method       | Description                               | Additional Notes                                                                                                                         |
|:------------------:|-------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| getDistanceValueFromSource(Node source, Node target)  | Get the distance to any target node in the graph from the specified source node      | Returns a double value containing the minimum distance to get from the source node to the specified target node. Null if path is invalid |

# General Usage
1) Import the Pathfinder library into your project
2) Create Nodes and Edges as desired with Properties (if applicable)
3) Add nodes and edges to sets
4) Instantiate a graph, there exists two types of Graphs implemented: DirectedGraph, UndirectedGraph
   - UndirectedGraph behaves like DirectedGraph however, each edge inputted is duplicated and the node order is reversed
5) Instantiate a Pathfinder. There exists an example implementation, Dijkstra.
   - Dijkstra computes the shortest paths from a source node to all other nodes
6) Call findPath() method (or any other pathfinder applicable method) to retrieve a list of shortest paths from a given source to target node