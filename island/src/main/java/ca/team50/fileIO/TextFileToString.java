package ca.team50.fileIO;

import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.FileReadException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));

            // Get first line
            String line = reader.readLine();

            // Read each line
            while (line != null) {

                // Add line to list
                String stringToAdd = line.replace("\n","");
                returnString.add(stringToAdd);
                // Read next line
                line = reader.readLine();

            }

            return returnString;


        } catch (Exception e) {
            ExceptionHandler.handleException(new FileReadException(inputFilePath));
            return null;
        }

    }

}
