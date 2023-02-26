package ca.team50.specification;

import org.apache.commons.cli.*;

public class CLInterface {

    // Specify defaults
    private static final int defaultRelax = 0;
    private static final int defaultNumOfPolygons = 625;
    private static final int defaultHeight = 500;
    private static final int defaultWidth = 500;
    private static final String defaultMeshName = "default";

    // Specify options
    private static final Option meshTypeOpt = new Option("mt", "meshtype", true, "Specify the mesh type as a string to generate, specified values are: " + getEnumValues() + ", default: GRID");
    private static final Option meshNameOpt = new Option("n", "name", true, "Specify the name of the .mesh file (as a string): <input>.mesh, default: " + defaultMeshName);
    private static final Option canvasWidthOpt = new Option("w", "width", true, "Specify the width of the canvas to generate (as an integer), default: " + defaultWidth);
    private static final Option canvasHeightOpt = new Option("l", "length", true, "Specify the height of the canvas to generate (as an integer), default: " + defaultHeight);
    private static final Option numOfPolygonsOpt = new Option("pc", "polygoncount", true, "Specify the number of polygons to generate (as an integer), default: " + defaultNumOfPolygons);
    private static final Option relaxLevelOpt = new Option("r", "relax", true, "Specify the level of relaxation (as an integer), default: " + defaultRelax);
    private static final Option helpOpt = new Option("h", "help", false, "Display input help");

    // Declare information variables
    private MeshType meshType;
    private String meshName;
    private int canvasWidth;
    private int canvasHeight;
    private int numOfPolygons;
    private int relaxLevel;


    /**
     * Parser for arguments taken in from generator
     * @param args the arguments from entry input
     * @return a CLInterface object which can be used to extract argument properties
     * @implNote Note that improper argument use causes CLInterface to call InputExceptionHandler. This will cause the program to exit!
     */
    public CLInterface(String[] args) {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();

        options.addOption(helpOpt);
        options.addOption(meshTypeOpt);
        options.addOption(meshNameOpt);
        options.addOption(canvasWidthOpt);
        options.addOption(canvasHeightOpt);
        options.addOption(numOfPolygonsOpt);
        options.addOption(relaxLevelOpt);

        try {
            // Parse input
            CommandLine commandLine = parser.parse(options, args);

            // Check if input help is needed
            if (commandLine.hasOption(helpOpt)) {
                // Print help and exit
                printHelp();
                System.exit(0);
            }

            // Get and store mesh type first
            String meshTypeString = commandLine.getOptionValue(meshTypeOpt,"GRID");

            if (meshTypeString.contains("GRID")) {
                this.meshType = MeshType.GRID;
            } else if (meshTypeString.contains("IRREGULAR")) {
                this.meshType = MeshType.IRREGULAR;
            }

            // Get mesh name
            this.meshName = commandLine.getOptionValue(meshNameOpt,defaultMeshName);

            // Get width and height
            this.canvasWidth = Integer.valueOf(commandLine.getOptionValue(canvasWidthOpt,String.valueOf(defaultWidth)));
            this.canvasHeight = Integer.valueOf(commandLine.getOptionValue(canvasHeightOpt,String.valueOf(defaultHeight)));

            // Get and set number of polygons to generate
            this.numOfPolygons = Integer.valueOf(commandLine.getOptionValue(numOfPolygonsOpt,String.valueOf(defaultNumOfPolygons)));

            // Get and set relax level
            this.relaxLevel = Integer.valueOf(commandLine.getOptionValue(relaxLevelOpt,String.valueOf(defaultRelax)));

        } catch (ParseException e) {
            InputExceptionHandler.handleInputException(new InputException(e.getMessage()));
        } catch (Exception e) {
            InputExceptionHandler.handleInputException(new InputException(e.getMessage()));
        }

    }

    // Getters for all data needed for generator to generate a mesh
    public MeshType getMeshType() {
        return this.meshType;
    }
    public String getMeshName() {
        return this.meshName;
    }
    public int getCanvasWidth() {
        return this.canvasWidth;
    }
    public int getCanvasHeight() {
        return this.canvasHeight;
    }
    public int getNumOfPolygons() {
        return this.numOfPolygons;
    }
    public int getRelaxLevel() {
        return this.relaxLevel;
    }

    // Method to print generator command line input help
    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        options.addOption(helpOpt);
        options.addOption(meshTypeOpt);
        options.addOption(meshNameOpt);
        options.addOption(canvasWidthOpt);
        options.addOption(canvasHeightOpt);
        options.addOption(numOfPolygonsOpt);
        options.addOption(relaxLevelOpt);

        formatter.printHelp("-<short or -- for long command> <numerical argument if required>",options);

    }

    // Method for printing MeshType enum values
    private static String getEnumValues() {
        // Method to get all values as a string for printing
        String returnString = "[";

        MeshType[] values = MeshType.values();

        for (MeshType currentType : values) {
            returnString+= currentType.name();
            if (currentType == values[values.length-1]) {
                break;
            }
            returnString+= ", ";
        }

        returnString+="]";

        return returnString;

    }

}
