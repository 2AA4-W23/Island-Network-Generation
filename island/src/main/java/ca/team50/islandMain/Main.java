package ca.team50.islandMain;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.fileIO.FileToPolyMesh;
import ca.team50.fileIO.PolyMeshToFile;
import ca.team50.generation.IslandGenerable;
import ca.team50.generation.Lagoon;
import ca.team50.specification.CLInterfaceIsland;

public class Main {

    public static void main(String[] args) {

        // Get all arguments from command line
        CLInterfaceIsland cli = new CLInterfaceIsland(args);

        // Convert corresponding input .mesh file into a PolyMesh
        PolyMesh<Polygons> polyMesh = FileToPolyMesh.getPolyMeshFromFile(cli.getMeshInput());

        IslandGenerable lagoon = new Lagoon();

        // Generate lagoon
        lagoon.generateIsland(polyMesh);

        // Write mesh to file
        PolyMeshToFile.writeMeshToFile(polyMesh, cli.getMeshOutput());

        System.out.println("Complete! Please run visualizer on the generated .mesh file to convert to an .svg!");
        System.exit(0);

    }

}
