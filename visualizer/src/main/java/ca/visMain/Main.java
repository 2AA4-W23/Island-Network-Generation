package ca.visMain;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.visualizer.GraphicRenderer;
import ca.mcmaster.cas.se2aa4.a2.visualizer.MeshDump;
import ca.mcmaster.cas.se2aa4.a2.visualizer.SVGCanvas;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import org.apache.commons.cli.*;
import org.apache.commons.cli.DefaultParser;
import ca.mcmaster.cas.se2aa4.a2.generator.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.OpenOption;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Creating Options
        Options options = new Options();
        Option debug = new Option("X", "debug", false, "enable debug mode");
        options.addOption(debug);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("ca.visMain.Main", options);
            System.exit(1);
        }

        boolean debugMode = cmd.hasOption("X");


        // Extracting command line parameters
        String inputFilePath = cmd.getArgs()[0];
        String outputFilePath = cmd.getArgs()[1];

        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFilePath))) {
            PolyMesh<Polygons> mesh = (PolyMesh<Polygons>) ois.readObject();

            Graphics2D canvas = null;

            for (Polygons poly : mesh) {
                List<Structs.Vertex> vertices = poly.getVerticesList();
                for (Structs.Vertex vertex : vertices) {
                     double x = vertex.getX();
                     double y = vertex.getY();
                     maxX = Math.max(maxX,x);
                     maxY = Math.max(maxY,y);
                }
            }

            // Create a canvas with the maximum x and y values
            if (canvas == null) {
                canvas = SVGCanvas.build((int) Math.ceil(maxX), (int) Math.ceil(maxY));
            }

            // Set up the renderer with the debug mode
            GraphicRenderer renderer = new GraphicRenderer();
            renderer.setDebugMode(debugMode);

            // Render the mesh onto the canvas
            renderer.render(mesh, canvas);

            // Storing the result in an SVG file
            SVGCanvas.write(canvas, outputFilePath);

            // Dump the mesh to stdout
            MeshDump dumper = new MeshDump();
            dumper.dump(mesh);

            System.out.println("Successfully written .svg to: " + outputFilePath);

        }
    }
}