package ca.team50.islandMain;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.fileIO.FileToPolyMesh;
import ca.team50.fileIO.PolyMeshToFile;
import ca.team50.generation.*;
import ca.team50.soilAbsorption.*;
import ca.team50.specification.CLInterfaceIsland;
import ca.team50.water.AquiferGenerator;

import java.awt.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        // Get all arguments from command line
        CLInterfaceIsland cli = new CLInterfaceIsland(args);

        // Convert corresponding input .mesh file into a PolyMesh
        PolyMesh<Polygons> polyMesh = FileToPolyMesh.getPolyMeshFromFile(cli.getMeshInput());

        IslandGenerable island = null;

        if (cli.getIslandMode() == ModeType.lagoon) {
            island = new Lagoon();
        } else if (cli.getIslandMode() == ModeType.test) {
            island = new TestIsland();
        } else if (cli.getIslandMode() == ModeType.normal) {
            island = new NormalGenerator(cli);
        }

        // Generate lagoon
        island.generateIsland(polyMesh);

        // Write mesh to file
        PolyMeshToFile.writeMeshToFile(polyMesh, cli.getMeshOutput());

        System.out.println("Complete! Please run visualizer on the generated .mesh file to convert to an .svg!");
        System.exit(0);

    }

}
