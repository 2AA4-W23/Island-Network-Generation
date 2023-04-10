package ca.lenoverd.city;

import ca.team50.exceptions.GenerationException;
import ca.team50.fileIO.TextFileToString;


import java.util.*;

public class NameGenerator {

    private List<String> dataSet = new ArrayList<>();
    private int nOrder;
    private HashMap<String, List<String>> ngrams = new HashMap<>();
    private List<String> nameBeginnings = new ArrayList<>();

    public NameGenerator(String datasetPath, int nOrder) throws GenerationException {

        // Set order and load data set
        this.nOrder = nOrder;
        loadDataSet(datasetPath);

        // Build a map for every possible character that comes after each n-gram
        // Use a list for increased chance of certain letters that appear more frequently
        // Loop through each name
        for (int nameIndex = 0; nameIndex < dataSet.size(); nameIndex++) {

            String curName = dataSet.get(nameIndex);

            // Build a map of n-grams and all their next possible states
            for (int i = 0; i <= curName.length() - nOrder - 1; i++) {

                // Get current n-gram
                String gram = curName.substring(i,i+nOrder);

                // Add beginning of name to beginnings list if i = 0 (as this tells us gram is the first n characters in the current name)
                if (i == 0) {
                    nameBeginnings.add(gram);
                }

                if (!ngrams.containsKey(gram)) {

                    // Create new entry in map is the gram does not yet exist in it
                    List<String> newGramList = new ArrayList<>();
                    ngrams.put(gram,newGramList);

                }

                // Get the next character after the n-gram and add it
                ngrams.get(gram).add(String.valueOf(curName.charAt(i+nOrder)));

            }

        }


    }

    public String generateName(int maxLengthToAdd) {

        // Get a random beginning to build from
        String currentGram = nameBeginnings.get(randomNumber(0, nameBeginnings.size()-1));
        String generatedName = currentGram;

        // Loop through each current n-gram and get the next possible character
        // Add the next character and then repeat the process with the next n-gram with that new letter
        // (i.e. look at the last n characters of the result)
        for (int i = 0; i<maxLengthToAdd; i++) {

            // Get the list of all possible next characters
            List<String> possibleCharacters = ngrams.get(currentGram);

            // Check if there are no possible characters to add
            if (possibleCharacters == null || possibleCharacters.size() == 0) {
                // If so just break
                break;
            }

            // Pick a random element (between 0 and max index)
            String character = possibleCharacters.get(randomNumber(0, possibleCharacters.size()-1));

            // Create the word
            generatedName+=character;

            // Get length
            int lengthOfResult = generatedName.length();
            // The next n-gram is the last n characters of the generated result
            currentGram = generatedName.substring(lengthOfResult-nOrder,lengthOfResult);

        }

        return generatedName;

    }

    // Generate random number between min (inclusive) and max (inclusive)
    private int randomNumber(int min, int max) {
        Random random = new Random();
        return (random.nextInt(max - min + 1) + min);
    }

    // Get list of strings from file to use
    private void loadDataSet(String filePath) throws GenerationException {

        List<String> dataSet;

        try {
            dataSet = TextFileToString.getStringListFromFile(filePath);

            for (String name : dataSet) {

                this.dataSet.add(name);

            }
        } catch (Exception e) {

            throw new GenerationException(e.getMessage());

        }


    }



}
