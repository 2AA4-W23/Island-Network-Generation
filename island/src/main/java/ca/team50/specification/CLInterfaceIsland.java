package ca.team50.specification;

import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.InvalidCommandFormatException;
import ca.team50.generation.ModeType;
import org.apache.commons.cli.*;
import ca.team50.soilAbsorption.*;

public class CLInterfaceIsland {

    private static final Option meshInput = new Option("i", "input", true, "Specify the name of the input mesh file (as a string): <input>.mesh (MUST INCLUDE .mesh in name!)");
    private static final Option meshOutput = new Option("o", "output", true, "Specify the name of the output mesh file (as a string): <output>.mesh (MUST INCLUDE .mesh in name!)");
    private static final Option islandGenMode = new Option("m", "mode", true, "Specify the mode of island generation, specified values are: " + getEnumValues() + ", default: lagoon");
    private static final Option helpOpt = new Option("h", "help", false, "Display input help");
    private static final Option aquifer = new Option("a", "aquifer", true, "Specify the number of aquifers to be generated: ");
    private static final Option soilContent = Option.builder("s")
            .longOpt("soil")
            .hasArgs()
            .numberOfArgs(3)
            .valueSeparator(',')
            .desc("Specify the clay content, sand content, and loam content of the soil (values between 0 and 1)")
            .build();
    private ModeType islandMode;
    private String meshInputString;
    private String meshOutputString;

    private Integer numAquifers;
    private double clayContent;
    private double sandContent;
    private double loamContent;


    public CLInterfaceIsland(String[] args) {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();

        options.addOption(meshInput);
        options.addOption(meshOutput);
        options.addOption(islandGenMode);
        options.addOption(helpOpt);
        options.addOption(aquifer);
        options.addOption(soilContent);

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
            }


            // Get input and output meshes
            this.meshInputString = commandLine.getOptionValue(meshInput);
            this.meshOutputString = commandLine.getOptionValue(meshOutput);

            // Get number of aquifers to be generated if requested
            String numAquifersString = commandLine.getOptionValue(aquifer);
            if (numAquifersString != null) {
                this.numAquifers = Integer.parseInt(numAquifersString);
            } else {
                this.numAquifers = 0; // default value if not specified
            }

            // Get soil content values
            String[] soilContentValues = commandLine.getOptionValues(soilContent);
            if (soilContentValues != null) {
                try{
                    clayContent = Double.parseDouble(soilContentValues[0]);
                    sandContent = Double.parseDouble(soilContentValues[1]);
                    loamContent = Double.parseDouble(soilContentValues[2]);
                    if (clayContent < 0 || clayContent > 1 || sandContent < 0 || sandContent > 1 || loamContent < 0 || loamContent > 1) {
                        System.exit(1);
                    }

                    // Create instance of Soil Profile and set the values
                    SoilProfile soilProfile = null;
                    soilProfile.setClayContent(clayContent);
                    soilProfile.setSandContent(sandContent);
                    soilProfile.setLoamContent(loamContent);

                } catch (NumberFormatException e) { System.exit(1);}
            }
            else{
                System.exit(1);
            }

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


    // Method to print generator command line input help
    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        options.addOption(meshInput);
        options.addOption(meshOutput);
        options.addOption(islandGenMode);
        options.addOption(helpOpt);
        options.addOption(aquifer);


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
