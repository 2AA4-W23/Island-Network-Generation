import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
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

public class Main {

    public static void main(String[] args) throws IOException {
        // Creating Options
        Options options = new Options();
        // Create debug option and set to false initially.
        Option debug = new Option("X","debug", false, "enable debug mode");
        options.addOption(debug);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLineParser cmd = (CommandLineParser) parser.parse(options,args);
             PolyMesh renderer = new PolyMesh();

            if(((CommandLine) cmd).hasOption("debug")){
                renderer.debugMode = true;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Extracting command line parameters
        String input = args[0];
        String output = args[1];
        String mode = args[2];
        // Getting width and height for the canvas
        PolyMesh aMesh = new MeshFactory().read(input);
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: aMesh.getVerticesList()) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        // Creating the Canvas to draw the mesh
        Graphics2D canvas = SVGCanvas.build((int) Math.ceil(max_x), (int) Math.ceil(max_y));
        GraphicRenderer renderer = new GraphicRenderer();
        // Painting the mesh on the canvas
        renderer.render(aMesh, canvas);
        // Storing the result in an SVG file
        SVGCanvas.write(canvas, output);
        // Dump the mesh to stdout
        MeshDump dumper = new MeshDump();
        dumper.dump(aMesh);
    }
}