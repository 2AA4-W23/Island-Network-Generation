# Assignment A2: Mesh Generator

  - Davis Lenover [lenoverd@mcmaster.ca]
  - Jasmine Wang [wangj500@mcmaster.ca]
  - Michael Baskaran [baskam1@mcmaster.ca]

### If looking for A3 information, please scroll to the bottom!

## How to run the product

### Installation instructions

This product is handled by Maven, as a multi-module project. We assume here that you have cloned the project in a directory named `A2`

To install the different tooling on your computer, simply run:

```
mosser@azrael A2 % mvn install
```

After installation, you'll find an application named `generator.jar` in the `generator` directory, and a file named `visualizer.jar` in the `visualizer` one. 

### Generator

To run the generator, go to the `generator` directory, and use `java -jar` to run the product.
It takes in multiple arguments to generate a desired mesh:
| Argument name | Command Abbreviation | Command | Description | Additional Notes |
|:--:|--|---------|-------|-----|
| Help | -h |   --help   |   Prints all available commands to console | Exits the program after execution. Provides extra information such as default values for specified arguments |
| Mesh Type | -mt |   --meshtype   |   Specify the mesh type as a string to generate (all possible strings listed in program) | |
| Name File Name | -n |   --name   |   Specify the name of the .mesh file as a string: input-here.mesh | |
| Canvas Width | -w |   --width  | Specify the width of the canvas to generate as an integer | Affects the total area of each polygon |
| Canvas Length (Height) | -l |   --length  | Specify the length (height) of the canvas to generate as an integer | Affects the total area of each polygon |
| Polygon Count | -pc |   --polygoncount  |  Specify the number of polygons to generate as an integer. | Affects the total area of each polygon |
| Lloyd relaxation | -r |   --relax  | Specify the number of times to apply relaxation as an integer ||
| .obj Export | -obj |   --objexport  | Export the mesh in both .mesh and .obj formats | .obj vertex positions are a direct translation thus depending on the reader, mesh inversion may occur. .obj is exported with the same name and location as .mesh |

Example of using the arguments is shown below
```
mosser@azrael A2 % cd generator 
mosser@azrael generator % java -jar generator.jar -mt IRREGULAR -n sample -w 1000 -l 1000 -pc 50 -r 10
```
This command specifies an irregular mesh with the file name "sample.mesh", a canvas width and length of 1000, 50 total polygons and a relaxation of 10.
Once the mesh is generated, one can move onto utilizing visualizer

### Visualizer

To visualize an existing mesh, go the the `visualizer` directory, and use `java -jar` to run the product. The product take two arguments (so far): the file containing the mesh, and the name of the file to store the visualization (as an SVG image).

```
mosser@azrael A2 % cd visualizer 
mosser@azrael visualizer % java -jar visualizer.jar ../generator/sample.mesh sample.svg

... (lots of debug information printed to stdout) ...

mosser@azrael visualizer % ls -lh sample.svg
-rw-r--r--  1 mosser  staff    56K 29 Jan 10:53 sample.svg
mosser@azrael visualizer %
```
To viualize the SVG file:

  - Open it with a web browser
  - Convert it into something else with tool slike `rsvg-convert`

### Enter Debug Mode
Go the the `visualizer` directory, and use `java -jar` to run the product.

```
mosser@azrael visualizer % java -jar visualizer.jar ../generator/sample.mesh sample.svg -X
```

## How to contribute to the project

When you develop features and enrich the product, remember that you have first to `package` (as in `mvn package`) it so that the `jar` file is re-generated by maven.

## Backlog

### Definition of Done

-- Feature operates as expected (as outlined in assignment) by client and is functionally sound (i.e. has been proven correct via testing by programmer) --

### Product Backlog
Status: Pending (P), Started (S), Blocked (B), Done (D)

| Id | Feature title | Who? | Start | End | Status |
|:--:|---------------|------|-------|-----|--------|
| F01 |      Create a Polygon Class        |   All   |    2023-02-11   |  2023-02-12    |    D    |
| F02 |      Create mesh collection         |   lenoverd   |    2023-02-14   |  2023-02-16   |     D   |
| F03 |      Add drawable segments to mesh collection      |   lenoverd   |   2023-02-16  |   2023-02-17  |   D  |
| F04 |     Ensure Polygon objects can have color          |  wangj500   |  2023-02-26    |  2023-02-26    | D |
| F05 |      Ensure Vertices can have color          |   wangj500   |    2023-02-18   |   2023-02-20  |    D    |
| F06 |      Ensure Segments can have color          |   wangj500   |    2023-02-18   |  2023-02-20   |   D     |
| F07 (F07-RE) |      Ensure Vertices and Segments has an alpha value to play with transparency         |   baskam1  |   2023-02-21    |  2023-02-21 (2023-02-25 re-implemented to fit current design)  |   D   |
| F08 |      Ensure Vertices and Segmentscan have thickness information          |  wangj500    |   2023-02-21    |  2023-02-21   |   D     |
| F09 |      Ensure we are no longer use MeshFactory to generate files and use ObjectInputStream and ObjectOutputStream to read out and in files | wangj500 & baskam1 |   2023-02-19    |  2023-02-21   |   D  |
| F10 |      Ensure we are able to trigger debug mode using -X flag in commandline |  baskam1    |   2023-02-21    |  2023-02-24   |   D     |
| F11 (F11-RE) |      Ensure we can compute Delaunay Triangle |  baskam1    |   2023-02-24    |  2023-02-25 (2023-02-26 re-implemented to fit current design)   |    D    |
| F12 |      Generate random points for each expected polygon         |  lenoverd   |   2023-02-23    |  2023-02-23  |   D    |
| F13 |      Compute Voronoi diagram         |  lenoverd   |    2023-02-23   |   2023-02-23  |   D    |
| F14 |      Apply Lloyd relaxation        |  lenoverd   |  2023-02-23    |  2023-02-23   |   D     |
| F15 |      Add command line interfacing      |  lenoverd   |  2023-02-25    |  2023-02-25   |   D     |
| F16 |      Utilized convexhull to reorder polygon segments to maintain consecutiveness invariance     |  wangj500  |  2023-02-25    |  2023-02-25   |   D    |
| F17 |      Crop method ro ensure all segments are generated on canvas  |  wangj500  |  2023-02-25    |  2023-02-25   |   D    |
| F18 |      .obj exporting  |  lenoverd  |  2023-02-26    |  2023-02-26   |   D    |



# Assignment A3: Island Generation
### Generation
To generate an island:
1) Generate a mesh using generator.jar (It is recommended that you generate an irregular mesh with at least 350 polygons)
2) Run island.jar with command arguments below
   It takes in multiple arguments to generate a desired mesh:
   | Argument name | Command Abbreviation | Command | Description | Additional Notes |
   |:--:|--|---------|-------|-----|
   | Help | -h |   --help   |   Prints all available commands to console | Exits the program after execution. Provides extra information such as default values for specified arguments |
   | Mesh File Input | -i |   --input   |   Specify the mesh name (including .mesh) in your file system to read | If specifying a .mesh file from the same directory as island.jar, no file path is needed|
   | Island mesh File Output | -o |   --output   |   Specify the mesh name (including .mesh) in your file system to write an island mesh file to | If specifying a .mesh file from the same directory as island.jar, no file path is needed |
   | Island Generation Mode | -m |   --mode   |  Specify the type of island to generate | All island modes are listed by using the -h command. Normal mode is the main generation mode and lagoon will produce the MVP island |
   | Aquifer | -a |   --aquifer   | Specify the number of aquifers to be generated (as an integer) | |
   | Lakes | -l |   --lakes   | Specify the maximum number of lakes to be generated (as an integer) | This is a MAX number and strongly depends on the altitude of the island |
   | Rivers | -r |   --rivers   | Specify the maximum number of iterations for rivers to be generated (as an integer) | One pass will generate rivers where altitude difference is sufficient |
   | Seed | -s |   --seed   | Specify the seed to be used by the Normal generator |  This may drastically change how the island looks (to no island at all), it is recommended to play around with this value a lot |
   | Biome | -b |   --biomes   | Specify the biomes of island generation | Use -h to find out all biomes available |
   | Elevation (altitude) | -al |   --altitude  | Specify the altitude type of island generation | Use -h to find out all elevations available |
   | Island Shape | -sh |  --shape | Specify the shape type of island generation | Use -h to find out all shapes available |
   | soilContent | -s0 |  --soil | Specify the type of Soil Profile | Use -h to find out all soil profiles available | 
   | Cities | -c |  --cities | Specify the maximum number of cities to generate | A red dot specifies the entry point to a city. The darker the square, the more populated the city is. When creating a star network, oceans are not considered island land and thus paths between cities only via oceans are invalid and thus are not calculated. |
3) Run visualizer.jar with the outputted mesh from island.jar in island generation mode

Ex:
```
# First, generate mesh
mosser@azrael A2 % cd generator
mosser@azrael generator % java -jar generator.jar -mt IRREGULAR -n sample -w 1000 -l 1000 -pc 800 -r 30

mosser@azrael A2 % cd ..
mosser@azrael A2 % cd island
# The following command specifies the generated mesh from generator.jar to convert to an island with 3 lakes, arctic biome, mountain elevation, the shape of a circle, a seed of 1234 and 2 river iterations
mosser@azrael island % java -jar island.jar -i ../generator/sample.mesh -o island.mesh -m normal --l 3 -b Arctic -al MOUNTAINS -sh CIRCLE -s 1234 -r 2


mosser@azrael A2 % cd ..
mosser@azrael A2 % cd visualizer
# Run visualizer on the generated island from island.jar and use the -ig command to specify island visualization
mosser@azrael visualizer % java -jar visualizer.jar ../island/island.mesh island.svg -ig
```

### A3 Product Backlog
Status: Pending (P), Started (S), Blocked (B), Done (D)

| Id | Feature title | Who? | Start | End | Status |
|:--:|---------------|------|-------|-----|--------|
| F01 |      Island.jar can read PolyMeshes from file       |   lenoverd   |    2023-03-19   |  2023-03-19    |    D    |
| F02 |      Implement basic command line interface (Apache)      |   lenoverd   |    2023-03-19   |  2023-03-19   |     D   |
| F03 |      Generate Lagoon   |   lenoverd   |   2023-03-19  |   2023-03-20  |   D  |
| F04 |      Introduce the notion of Tiles   |   lenoverd   |   2023-03-20  |   2023-03-21  |   D  |
| F05 |      Implement special island renderer for visualizer.jar  |   lenoverd   |   2023-03-21  |   2023-03-21  |   D  |
| F06 |      Introduce the notion of Island Shapes  |   lenoverd   |   2023-03-21  |   2023-03-22  |   D  |
| F07-3 |     Introduce Lake Generation |   lenoverd   |   2023-03-22  |   2023-03-23  |   D  |
| F08-3 |      Advanced command line specification |   lenoverd   |   2023-03-24  | 2023-03-26  |   D  |
| F09 |      Introduce altitude and implement altemeric profiles |   wangj500   |   2023-03-24  |  2023-03-25 |  D  |
| F10-3 |      Introduce Tiles of various Biomes |   wangj500   |   2023-03-22  |  2023-03-22 |  D  |
| F11 |      Introduce River Generation |   wangj500   |   2023-03-24  | 2023-03-24   |  D |
| F12 |      Introduced Aquifer Generation |  baskam1   |   2023-03-22  | 2023-03-23  |  D  |
| F13 |      Create SoilProfileGenerable Interface |  baskam1   |   2023-03-23  | 2023-03-23  |  D  |
| F14 |      Create SoilProfile Abstract class to help develop different SoilProfiles |  baskam1   |   2023-03-23  | 2023-03-24  |  D  |
| F15 |      Generated specific SoilProfiles for users to select from |  baskam1   |   2023-03-24  | 2023-03-26  |  D  |
| F16 |      Implemented capabilities to computeRemainingWater |  baskam1   |   2023-03-25  | 2023-03-26  |  D  |
| F17-3 (2) R2 |      Implemented unified island generator |  lenoverd   |   2023-03-25  | 2023-03-29  |  D  |

# Assignment A4: Urbanism
|     Id     | Feature title                                    | Start      | End                         | Status |
|:----------:|--------------------------------------------------|------------|-----------------------------|--------|
|    4F01    | Implement the notion of a graph Node             | 2023-03-30 | 2023-03-30                  | D      |
|    4F02    | Implement the notion of graph node properties    | 2023-03-30 | 2022-04-01                  | D      |
|    4F03    | Implement the notion of a graph edge             | 2023-04-01 | 2023-04-01                  | D      |
|    4F04    | Implement the notion of graph edge properties    | 2023-04-01 | 2023-04-01                  | D      |
|    4F05    | Implement Graph ADT                              | 2023-04-01 | 2023-04-02                  | D      |
| 4F06 (_R3) | Implement Pathfinder ADT and sample algorithm    | 2023-04-02 | 2023-04-02 (_R3 2023-04-03) | D      |
|    4F07    | Implement separate city tiles based on city size | 2023-04-04 | 2023-04-04                  | D      |
|    4F08    | Implement road property                          | 2023-04-04 | 2023-04-07                  | D      |
|    4F09    | Implement city generator                         | 2023-04-04 | 2023-04-07                  | D      |
|    4F10    | Implement maximum number of cities via CMD       | 2023-04-04 | 2023-04-07                  | D      |
|    4F11    | Implement city names via a markov process        | 2023-04-08 |                 | S      |