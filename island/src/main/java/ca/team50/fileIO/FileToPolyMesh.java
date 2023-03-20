package ca.team50.fileIO;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.exceptions.FileReadException;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class FileToPolyMesh {

    /**
     * Get a polymesh object from a .mesh file
     * @param inputFilePath path to file as a string
     * @exception FileReadException if failure to convert given inputFilePath occurs
     * @return a polymesh object
     */
    public PolyMesh<Polygons> getPolyMeshFromFile(String inputFilePath) throws FileReadException {

        try {

            // Get object input stream from file path
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFilePath));

            // Cast to PolyMesh
            PolyMesh<Polygons> mesh = (PolyMesh<Polygons>) ois.readObject();

            return mesh;


        } catch (Exception e) {
            throw new FileReadException(inputFilePath);
        }

    }

}
