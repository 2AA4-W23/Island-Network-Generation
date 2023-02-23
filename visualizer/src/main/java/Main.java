
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
import java.io.IOException;
import java.nio.file.OpenOption;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        // Extracting command line parameters
        String input = args[0];
        String output = args[1];

        PolyMesh<Polygons> mesh = DotGen.polygonGenerate();
        Graphics2D canvas = null;

        for (Polygons poly : mesh) {
            List<Structs.Vertex> vertices = poly.getVerticesList();
            for (Structs.Vertex vertex : vertices) {
                double x = vertex.getX();
                double y = vertex.getY();
                // Creating the Canvas to draw the mesh
                if (canvas == null) {
                    canvas = SVGCanvas.build((int) Math.ceil(x), (int) Math.ceil(y));
                }
            }
            GraphicRenderer renderer = new GraphicRenderer();

            // Creating Options
            Options options = new Options();
            // Create debug option and set to false initially.
            Option debug = new Option("X", "debug", false, "enable debug mode");
            options.addOption(debug);

            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();

            try {
                CommandLineParser cmd = (CommandLineParser) parser.parse(options, args);
                if (((CommandLine) cmd).hasOption("debug")) {
                    mesh.debugMode = true;
                }
                renderer.render(mesh, canvas);
                // Storing the result in an SVG file
                SVGCanvas.write(canvas, output);
                // Dump the mesh to stdout
                MeshDump dumper = new MeshDump();
                dumper.dump(mesh);

            } catch (ParseException e) {
                System.out.println(e.getMessage());
                formatter.printHelp("Main", options);
                System.exit(1);

            }
        }
    }
}