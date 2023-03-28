package ca.team50.exceptions;

import ca.team50.specification.CLInterfaceIsland;

public class ExceptionHandler {

    // Method handles invalid formatting of commands
    public static void handleException(InvalidCommandFormatException exception) {

        System.out.println("An exception occurred!");
        System.out.println("Exception: " + exception.getClass());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("This was due to invalid command input. As a reminder, the commands help list is below");
        CLInterfaceIsland.printHelp();
        System.exit(0);

    }

    // Method handles invalid IO mesh names
    public static void handleException(FileReadException exception) {

        System.out.println("An exception occurred!");
        System.out.println("Exception: " + exception.getClass());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("It seems the requested file was invalid, verify it's name is correct and try again.");
        System.exit(0);

    }

    public static void handleException(FileWriteException exception) {

        System.out.println("An exception occurred!");
        System.out.println("Exception: " + exception.getClass());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("It seems writing to storage did not work. Please verify you have ample storage and the file name is valid. Then try again.");
        System.exit(0);

    }

    public static void handleException(GenerationException exception) {

        System.out.println("An exception occurred!");
        System.out.println("Exception: " + exception.getClass());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("This may have been caused by a few things, in particular invalid input and/or the seed does not work with the given canvas size");
        CLInterfaceIsland.printHelp();
        System.exit(0);

    }

}
