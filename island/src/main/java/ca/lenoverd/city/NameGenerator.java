package ca.lenoverd.city;

import ca.team50.fileIO.TextFileToString;

import java.io.FileReader;
import java.util.*;

public class NameGenerator {

    private List<String> dataSet = new ArrayList<>();
    public int nOrder = 8;
    private HashMap<String, List<String>> ngrams = new HashMap<>();
    private List<String> nameBeginnings = new ArrayList<>();

    public NameGenerator(String datasetPath) {

        loadDataSet(datasetPath);

        // Loop through each name
        for (int nameIndex = 0; nameIndex < dataSet.size(); nameIndex++) {

            String curName = dataSet.get(nameIndex);

            // Build the table of n-grams and all their next possible states
            for (int i = 0; i <= curName.length() - nOrder; i++) {

                String gram = curName.substring(i,i+nOrder);

                // Add beginning of name to beginnings list if i = 0 (as this tells us gram is the first n characters)
                if (i == 0) {
                    nameBeginnings.add(gram);
                }

                if (!ngrams.containsKey(gram)) {

                    // Create new entry in map is the gram does not yet exist in it
                    List<String> newGramList = new ArrayList<>();
                    ngrams.put(gram,newGramList);

                }

                // Get the next character after the n-gram and add it
                ngrams.get(gram).add(curName.substring(i+nOrder,i+nOrder));

            }

        }


    }

    public String generateName() {

        // Get a random beginning
        String currentGram = nameBeginnings.get(randomNumber(0, nameBeginnings.size()-1));
        String result = currentGram;

        // Loop through each current n-gram and get the next possible character
        // Add the next character and then repeat the process with the next n-gram with that new letter
        // (i.e. look at the last n characters of the result)
        for (int i = 0; i<20; i++) {

            List<String> possibleCharacters = ngrams.get(currentGram);

            // Check if there are no possible characters to add
            if (possibleCharacters.size() == 0) {
                // If so just break
                break;
            }

            // Pick a random element (between 0 and max index)
            String character = possibleCharacters.get(randomNumber(0, possibleCharacters.size()-1));

            // Create the word
            result+=character;

            // Get length
            int lengthOfResult = result.length();
            // The next n-gram is the last three characters of the generated result
            currentGram = result.substring(lengthOfResult-nOrder,lengthOfResult);

        }

        return result;

    }

    // Generate random number between min (inclusive) and max (inclusive)
    private int randomNumber(int min, int max) {
        Random random = new Random();
        return (random.nextInt(max - min + 1) + min);
    }

    // Get list of strings from file to use
    private void loadDataSet(String filePath) {

        TextFileToString.getStringListFromFile(filePath);

    }



}
