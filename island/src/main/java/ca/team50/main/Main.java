package ca.team50.main;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.InvalidCommandFormatException;
import ca.team50.fileIO.FileToPolyMesh;
import ca.team50.fileIO.PolyMeshToFile;
import ca.team50.generation.IslandGenerable;
import ca.team50.generation.Lagoon;
import ca.team50.specification.CLInterface;

public class Main {

    public static void main(String[] args) {

        // Get all arguments from command line
        CLInterface cli = new CLInterface(args);

        // Convert corresponding input .mesh file into a PolyMesh
        PolyMesh<Polygons> polyMesh = FileToPolyMesh.getPolyMeshFromFile(cli.getMeshInput());

        IslandGenerable lagoon = new Lagoon();

        // Generate lagoon
        lagoon.generateIsland(polyMesh);

        // Write mesh to file
        PolyMeshToFile.writeMeshToFile(polyMesh, cli.getMeshOutput());

        System.out.println("Complete!");
        System.exit(0);

    }

}
