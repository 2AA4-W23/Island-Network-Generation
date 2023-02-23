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

To run the generator, go to the `generator` directory, and use `java -jar` to run the product. The product takes one single argument (so far), the name of the file where the generated mesh will be stored as binary.

```
mosser@azrael A2 % cd generator 
mosser@azrael generator % java -jar generator.jar sample.mesh
mosser@azrael generator % ls -lh sample.mesh
-rw-r--r--  1 mosser  staff    29K 29 Jan 10:52 sample.mesh
mosser@azrael generator % 
```

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

## How to contribute to the project

When you develop features and enrich the product, remember that you have first to `package` (as in `mvn package`) it so that the `jar` file is re-generated by maven.

## Backlog

### Definition of Done

-- Insert here your definition of done for your features --

### Product Backlog

| Id | Feature title | Who? | Start | End | Status |
|:--:|---------------|------|-------|-----|--------|
| F01 |      Create a Polygon Class         |   All   |    2023-02-11   |  2023-02-12    |    D    |
| F02 |      Create mesh collection         |   lenoverd   |    2023-02-14   |  2023-02-16   |     D   |
| F03 |      Add drawable segments to mesh collection      |   lenoverd   |   2023-02-16  |   2023-02-17  |   D  |
| F04 |     Ensure Polygon object can have color          |     |      |     | |
| F05 |      Ensure Vertices can have color          |   wangj500   |    2023-02-18   |   2023-02-20  |    IN PROGRESS    |
| F06 |      Ensure Segments can have color          |   wangj500   |    2023-02-18   |  2023-02-20   |   IN PROGRESS     |
| F07 |      Ensure Vertices and Segments has an alpha value to play with transparency         |      |       |     |        |
| F08 |      Ensure Vertices and Segmentscan have thickness information          |  wangj500    |   2023-02-21    |  2023-02-21   |   IN PROGRESS     |
| F12 |      Generate random points for each expected Polygon         |  lenoverd   |   2023-02-23    |     |   IN PROGRESS     |
| F13 |      Compute Voronoi diagram         |  lenoverd   |       |     |   B(F12)     |
| F14 |      Apply Lloyd relaxation        |  lenoverd   |      |     |   B(F13)     |


