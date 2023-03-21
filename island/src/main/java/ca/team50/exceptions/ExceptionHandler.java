package ca.team50.exceptions;

import ca.team50.specification.CLInterfaceIsland;

public class ExceptionHandler {

    // Method handles invalid formatting of commands
    public static void handleException(InvalidCommandFormatException exception) {

        System.out.println("An exception occurred!");
        System.out.println("Exception: " + exception.getClass());
        System.out.println("Message: " + exception.getMessage());
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

}
