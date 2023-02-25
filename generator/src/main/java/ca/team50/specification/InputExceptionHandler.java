package ca.team50.specification;

public class InputExceptionHandler {

    // Method to handle any input exceptions
    public static void handleInputException(InputException inputException) {
        System.out.println("Whoops! It seems your input was unrecognized!");
        System.out.println("Message from error: " + inputException.getMessage());
        System.out.println("As a reminder, the following information below details how to correctly input arguments:");
        CLInterface.printHelp();
        System.exit(0);
    }

}
