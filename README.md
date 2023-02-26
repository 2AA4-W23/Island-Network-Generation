# Assignment A2: Mesh Generator

  - Davis Lenover [lenoverd@mcmaster.ca]
  - Jasmine Wang [wangj500@mcmaster.ca]
  - Michael Baskaran [baskam1@mcmaster.ca]

## How to run the product

_This section needs to be edited to reflect how the user can interact with thefeature released in your project_

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
| Name File Name | -n |   --name   |   Specify the name of the .mesh file as a string: <input>.mesh | |
| Canvas Width | -w |   --width  | Specify the width of the canvas to generate as an integer | Affects the total area of each polygon |
| Canvas Length (Height) | -l |   --length  | Specify the length (height) of the canvas to generate as an integer | Affects the total area of each polygon |
| Polygon Count | -pc |   --polygoncount  |  Specify the number of polygons to generate as an integer. ||
| Lloyd relaxation | -r |   --relax  | Specify the number of times to apply relaxation as an integer ||

Example of using the arguments is shown below
```
mosser@azrael A2 % cd generator 
mosser@azrael generator % java -jar generator.jar -mt IRREGULAR -n sample -w 1000 -l 1000 -pc 50 -r 10
```
This command specifies an irregular mesh with the file name "sample.mesh", a canvas width and length of 1000, 50 total polygons and a relaxation of 10

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

-- Insert here your definition of done for your features --

### Product Backlog

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


