package ca.team50.specification;

import ca.team50.Tiles.BiomeType;
import ca.team50.elevation.ElevationType;
import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.InvalidCommandFormatException;
import ca.team50.soilAbsorption.*;
import ca.team50.water.AquiferGenerator;
import ca.team50.generation.ModeType;
import ca.team50.shapes.IslandShapeType;
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

    private static final Option biome = new Option("b", "biomes", true, "Specify the biomes of island generation, specified values are: " + getBiomeEnumValues() + ", default: " + BiomeType.values()[0].name());
    private static final Option elevation = new Option("al", "altitude", true, "Specify the altitude type of island generation, specified values are: " + getElevationEnumValues() + ", default: " + ElevationType.values()[0].name());
    private static final Option shape = new Option("sh", "shape", true, "Specify the shape type of island generation, specified values are: " + getShapeEnumValues() + ", default: " + IslandShapeType.values()[0].name());

    private static final Option soilContent = new Option("so", "soil", true, "Specify the type of Soil Profile (Clay, Loam, Sand, or Special)");

    private static final Option cities = new Option("c", "cities", true, "Specify the maximum number of cities to be generated (as an integer)");
    private static final Option cityDataset = new Option("d", "dataset", true, "Specify the text file for the base of city name generation");
    private static final int defaultNorder = 4;
    private static final int defaultWordLength = 9;
    private static final Option nOrder = new Option("or", "order", true, "Specify the length of a given n-gram for city name generation, default: " + defaultNorder);
    private static final Option maxNameLengthToAdd = new Option("le", "length", true, "Specify the addon length of a given name for city name generation. The max length of the string is (order + addon length) default: " + defaultWordLength);

    private ModeType islandMode;
    private BiomeType biomeType;
    private ElevationType elevationType;
    private IslandShapeType shapeType;
    private String meshInputString;
    private String meshOutputString;
    private String soilType;
    private String nameDatasetFilePath;

    private Integer numAquifers;
    private int numLakes;
    private int numRivers;
    private int numCities;
    private long numSeed;
    private boolean hasNameDatasetPath = false;
    private int nOrderInt;
    private int maxNameLengthToAddInt;


    public CLInterfaceIsland(String[] args) {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();

        options.addOption(meshInput);
        options.addOption(meshOutput);
        options.addOption(islandGenMode);
        options.addOption(biome);
        options.addOption(shape);
        options.addOption(elevation);
        options.addOption(helpOpt);
        options.addOption(aquifer);
        options.addOption(lakes);
        options.addOption(rivers);
        options.addOption(seed);
        options.addOption(soilContent);
        options.addOption(cities);
        options.addOption(cityDataset);
        options.addOption(nOrder);
        options.addOption(maxNameLengthToAdd);

        try {

            CommandLine commandLine = parser.parse(options,args);

            // Check if input help is needed
            if (commandLine.hasOption(helpOpt)) {
                // Print help and exit
                printHelp();
                System.exit(0);
            }

            // Get island generation mode
            String islandModeType = commandLine.getOptionValue(islandGenMode,ModeType.values()[0].name().toLowerCase());

            for (ModeType curType : ModeType.values()) {
                if (islandModeType.contains(curType.name())) {
                    this.islandMode = curType;
                    break;
                }
            }

            // Get soil type
            this.soilType = commandLine.getOptionValue("so","clay");

            // Get biome type
            String biomeModeType = commandLine.getOptionValue(biome,BiomeType.values()[0].name());

            for (BiomeType curType : BiomeType.values()) {
                if (biomeModeType.contains(curType.name())) {
                    this.biomeType = curType;
                    break;
                }
            }

            // Get elevation type
            String elevationModeType = commandLine.getOptionValue(elevation,ElevationType.values()[0].name());

            for (ElevationType curType : ElevationType.values()) {
                if (elevationModeType.contains(curType.name())) {
                    this.elevationType = curType;
                    break;
                }
            }

            // Get shape type
            String shapeModeType = commandLine.getOptionValue(shape,IslandShapeType.values()[0].name());

            for (IslandShapeType curType : IslandShapeType.values()) {
                if (shapeModeType.contains(curType.name())) {
                    this.shapeType = curType;
                    break;
                }
            }


            // Get input and output meshes
            this.meshInputString = commandLine.getOptionValue(meshInput);
            this.meshOutputString = commandLine.getOptionValue(meshOutput);

            // Get number of aquifers to be generated if requested
            this.numAquifers = Integer.parseInt(commandLine.getOptionValue(aquifer, "0"));

            // Lakes, Rivers and Seed
            this.numLakes = Integer.parseInt(commandLine.getOptionValue(lakes,"0"));
            this.numRivers = Integer.parseInt(commandLine.getOptionValue(rivers,"0"));
            this.numSeed = Integer.parseInt(commandLine.getOptionValue(seed,"90915"));

            // Cities
            this.numCities = Integer.parseInt(commandLine.getOptionValue(cities,"0"));

            // Check if names are wanted to cities
            if (this.numCities != 0 && commandLine.hasOption(cityDataset)) {
                this.hasNameDatasetPath = true;
                this.nameDatasetFilePath = commandLine.getOptionValue(cityDataset,null);
                // Get generation parameters
                this.nOrderInt = Integer.parseInt(commandLine.getOptionValue(nOrder, String.valueOf(defaultNorder)));
                this.maxNameLengthToAddInt = Integer.parseInt(commandLine.getOptionValue(maxNameLengthToAdd, String.valueOf(defaultWordLength)));

                if (nOrderInt <= 0) {
                    throw new InvalidCommandFormatException("n-order (" + nOrderInt +") must be greater than 0");
                } else if (maxNameLengthToAddInt <= 0) {
                    throw new InvalidCommandFormatException("max name length (" + maxNameLengthToAddInt +") must be greater than 0");
                }

            }


        } catch (InvalidCommandFormatException e) {
            ExceptionHandler.handleException(e);
        } catch (Exception e) {
            ExceptionHandler.handleException(new InvalidCommandFormatException("Failed to parse arguments. Were commands inputted correctly?"));
        }

    }

    public ModeType getIslandMode() {
        return islandMode;
    }
    public BiomeType getBiomeType() {
        return biomeType;
    }

    public ElevationType getElevationType() {
        return elevationType;
    }

    public IslandShapeType getShapeType() {
        return shapeType;
    }

    public String getMeshInput() {
        return meshInputString;
    }

    public String getMeshOutput() {
        return meshOutputString;
    }

    public String getSoilType() { return soilType;}

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
    public Integer getNumCities() {
        return numCities;
    }
    public String getNameDatasetFilePath() {
        return this.nameDatasetFilePath;
    }
    public boolean hasNameDatasetPath() {
        return this.hasNameDatasetPath;
    }
    public int getnOrder() {
        return this.nOrderInt;
    }
    public int getMaxNameLengthToAdd() {
        return this.maxNameLengthToAddInt;
    }


    // Method to print generator command line input help
    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        options.addOption(meshInput);
        options.addOption(meshOutput);
        options.addOption(islandGenMode);
        options.addOption(biome);
        options.addOption(shape);
        options.addOption(elevation);
        options.addOption(helpOpt);
        options.addOption(aquifer);
        options.addOption(lakes);
        options.addOption(rivers);
        options.addOption(seed);
        options.addOption(soilContent);
        options.addOption(cities);
        options.addOption(cityDataset);
        options.addOption(nOrder);
        options.addOption(maxNameLengthToAdd);

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

    private static String getBiomeEnumValues() {
        // Method to get all values as a string for printing
        String returnString = "[";

        BiomeType[] values = BiomeType.values();

        for (BiomeType currentType : values) {
            returnString+= currentType.name();
            if (currentType == values[values.length-1]) {
                break;
            }
            returnString+= ", ";
        }

        returnString+="]";

        return returnString;

    }

    private static String getElevationEnumValues() {
        // Method to get all values as a string for printing
        String returnString = "[";

        ElevationType[] values = ElevationType.values();

        for (ElevationType currentType : values) {
            returnString+= currentType.name();
            if (currentType == values[values.length-1]) {
                break;
            }
            returnString+= ", ";
        }

        returnString+="]";

        return returnString;

    }

    private static String getShapeEnumValues() {
        // Method to get all values as a string for printing
        String returnString = "[";

        IslandShapeType[] values = IslandShapeType.values();

        for (IslandShapeType currentType : values) {
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