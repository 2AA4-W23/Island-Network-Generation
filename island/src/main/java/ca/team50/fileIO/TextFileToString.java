package ca.team50.fileIO;

import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.FileReadException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextFileToString {


    /**
     * Get a list of Strings via a file
     * @param inputFilePath file name as a string, note the file must be in the same directory as the island.jar
     * @exception FileReadException if failure to convert given inputFilePath occurs. Which ExceptionHandler will be invoked for this exception throw.
     * @return a List object containing strings. Each string corresponds to a line in the given text file
     */
    public static List<String> getStringListFromFile(String inputFilePath) {

        try {

            List<String> returnString = new ArrayList<>();

            // Get file input stream from file path
            File file = new File(inputFilePath);

            Scanner scanner = new Scanner(file);

            // Read each line
            while (scanner.hasNextLine()) {

                // Add line to list
                String stringToAdd = scanner.next().replace("\n","");
                returnString.add(stringToAdd);

            }

            return returnString;


        } catch (Exception e) {
            ExceptionHandler.handleException(new FileReadException(inputFilePath));
            return null;
        }

    }

}
