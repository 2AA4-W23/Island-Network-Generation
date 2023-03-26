package ca.team50.specification;

import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.InvalidCommandFormatException;
import ca.team50.generation.Aquifer;
import ca.team50.generation.ModeType;
import org.apache.commons.cli.*;

public class CLInterfaceIsland {

    private static final Option meshInput = new Option("i", "input", true, "Specify the name of the input mesh file (as a string): <input>.mesh (MUST INCLUDE .mesh in name!)");
    private static final Option meshOutput = new Option("o", "output", true, "Specify the name of the output mesh file (as a string): <output>.mesh (MUST INCLUDE .mesh in name!)");
    private static final Option islandGenMode = new Option("m", "mode", true, "Specify the mode of island generation, specified values are: " + getEnumValues() + ", default: lagoon");
    private static final Option helpOpt = new Option("h", "help", false, "Display input help");
    private static final Option aquifer = new Option("a", "aquifer", true, "Specify the number of aquifers to be generated (as an integer)");
    private static final Option lakes = new Option("l", "lakes", true, "Specify the maximum number of lakes to be generated (as an integer)");
    private static final Option rivers = new Option("r", "rivers", true, "Specify the maximum number of rivers to be generated (as an integer)");
    private static final Option seed = new Option("s", "seed", true, "Specify the seed of the generation (as a long value)");
    // TODO SOIL AND BIOMES

    private ModeType islandMode;
    private String meshInputString;
    private String meshOutputString;

    private Integer numAquifers;
    private int numLakes;
    private int numRivers;
    private long numSeed;


    public CLInterfaceIsland(String[] args) {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();

        options.addOption(meshInput);
        options.addOption(meshOutput);
        options.addOption(islandGenMode);
        options.addOption(helpOpt);
        options.addOption(aquifer);
        options.addOption(lakes);
        options.addOption(rivers);
        options.addOption(seed);

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
            } else if (islandModeType.contains("test")) {
                islandMode = ModeType.test;
            } else if (islandModeType.contains("aquifer")) {
                islandMode = ModeType.aquifer;
            }


            // Get input and output meshes
            this.meshInputString = commandLine.getOptionValue(meshInput);
            this.meshOutputString = commandLine.getOptionValue(meshOutput);

            // Get number of aquifers to be generated if requested
            this.numAquifers = Integer.parseInt(commandLine.getOptionValue(aquifer, "0"));

            // Lakes, Rivers and Seed
            this.numLakes = Integer.parseInt(commandLine.getOptionValue(lakes,"0"));
            this.numRivers = Integer.parseInt(commandLine.getOptionValue(rivers,"0"));
            this.numSeed = Integer.parseInt(commandLine.getOptionValue(seed,"1234"));



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

    public Integer getNumAquifers() { return  numAquifers;}

    public Integer getNumLakes() {
        return numLakes;
    }
    public Integer getNumRivers() {
        return numRivers;
    }
    public Long getSeed() {
        return numSeed;
    }


    // Method to print generator command line input help
    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        options.addOption(meshInput);
        options.addOption(meshOutput);
        options.addOption(islandGenMode);
        options.addOption(helpOpt);
        options.addOption(aquifer);
        options.addOption(lakes);
        options.addOption(rivers);
        options.addOption(seed);


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
