package ca.team50.fileIO;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.FileReadException;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class FileToPolyMesh {

    /**
     * Get a polymesh object from a .mesh file
     * @param inputFilePath file name as a string, note the file must be in the same directory as the island.jar
     * @exception FileReadException if failure to convert given inputFilePath occurs. Which ExceptionHandler will be invoked for this exception throw.
     * @return a polymesh object
     */
    public static PolyMesh<Polygons> getPolyMeshFromFile(String inputFilePath) {

        try {

            // Get object input stream from file path
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFilePath));

            // Cast to PolyMesh
            PolyMesh<Polygons> mesh = (PolyMesh<Polygons>) ois.readObject();

            return mesh;


        } catch (Exception e) {
            ExceptionHandler.handleException(new FileReadException(inputFilePath));
            return null;
        }

    }

}
