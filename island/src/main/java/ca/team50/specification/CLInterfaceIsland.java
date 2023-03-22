package ca.team50.specification;

import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.InvalidCommandFormatException;
import ca.team50.generation.ModeType;
import org.apache.commons.cli.*;

public class CLInterfaceIsland {

    private static final Option meshInput = new Option("i", "input", true, "Specify the name of the input mesh file (as a string): <input>.mesh (MUST INCLUDE .mesh in name!)");
    private static final Option meshOutput = new Option("o", "output", true, "Specify the name of the output mesh file (as a string): <output>.mesh (MUST INCLUDE .mesh in name!)");
    private static final Option islandGenMode = new Option("m", "mode", true, "Specify the mode of island generation, specified values are: " + getEnumValues() + ", default: lagoon");
    private static final Option helpOpt = new Option("h", "help", false, "Display input help");

    private ModeType islandMode;
    private String meshInputString;
    private String meshOutputString;


    public CLInterfaceIsland(String[] args) {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();

        options.addOption(meshInput);
        options.addOption(meshOutput);
        options.addOption(islandGenMode);
        options.addOption(helpOpt);

        try {

            CommandLine commandLine = parser.parse(options,args);

            // Check if input help is needed
            if (commandLine.hasOption(helpOpt)) {
                // Print help and exit
                printHelp();
                System.exit(0);
            }

            // Get island generation mode
            String islandModeType = commandLine.getOptionValue(islandGenMode,"lagoon");

            if (islandModeType.contains("lagoon")) {
                islandMode = ModeType.lagoon;
            } else if (islandModeType.contains("random")) {
                islandMode = ModeType.random;
            }

            // Get input and output meshes
            this.meshInputString = commandLine.getOptionValue(meshInput);
            this.meshOutputString = commandLine.getOptionValue(meshOutput);

        } catch (Exception e) {
            ExceptionHandler.handleException(new InvalidCommandFormatException("Failed to parse arguments. Were commands inputted correctly?"));
        }


    }

    public ModeType getIslandMode() {
        return islandMode;
    }

    public String getMeshInput() {
        return meshInputString;
    }

    public String getMeshOutput() {
        return meshOutputString;
    }


    // Method to print generator command line input help
    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        options.addOption(meshInput);
        options.addOption(meshOutput);
        options.addOption(islandGenMode);
        options.addOption(helpOpt);


        formatter.printHelp("-<short or -- for long command> <numerical or string argument if required>",options);

    }


    private static String getEnumValues() {
        // Method to get all values as a string for printing
        String returnString = "[";

        ModeType[] values = ModeType.values();

        for (ModeType currentType : values) {
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
