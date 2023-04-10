package ca.lenoverd.city;

import ca.team50.exceptions.GenerationException;
import ca.team50.fileIO.TextFileToString;

import java.io.FileReader;
import java.util.*;

public class NameGenerator {

    private List<String> dataSet = new ArrayList<>();
    public int nOrder;
    private HashMap<String, List<String>> ngrams = new HashMap<>();
    private List<String> nameBeginnings = new ArrayList<>();

    public NameGenerator(String datasetPath, int nOrder) throws GenerationException {

        // Set order and load data set
        this.nOrder = nOrder;
        loadDataSet(datasetPath);

        //System.out.println(Arrays.toString(dataSet.toArray()));

        // Loop through each name
        for (int nameIndex = 0; nameIndex < dataSet.size(); nameIndex++) {

            String curName = dataSet.get(nameIndex);

            // Build the table of n-grams and all their next possible states
            for (int i = 0; i <= curName.length() - nOrder - 1; i++) {

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
                ngrams.get(gram).add(String.valueOf(curName.charAt(i+nOrder)));

            }

        }


    }

    public String generateName(int maxLength) {

        // Get a random beginning
        String currentGram = nameBeginnings.get(randomNumber(0, nameBeginnings.size()-1));
        String result = currentGram;

        // Loop through each current n-gram and get the next possible character
        // Add the next character and then repeat the process with the next n-gram with that new letter
        // (i.e. look at the last n characters of the result)
        for (int i = 0; i<maxLength; i++) {

            List<String> possibleCharacters = ngrams.get(currentGram);

            //System.out.println(Arrays.toString(possibleCharacters.toArray()));

            // Check if there are no possible characters to add
            if (possibleCharacters == null || possibleCharacters.size() == 0) {
                // If so just break
                break;
            }

            // Pick a random element (between 0 and max index)
            String character = possibleCharacters.get(randomNumber(0, possibleCharacters.size()-1));

            // Create the word
            result+=character;

            // Get length
            int lengthOfResult = result.length();
            // The next n-gram is the last n characters of the generated result
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
