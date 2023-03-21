package ca.team50.fileIO;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.FileWriteException;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class PolyMeshToFile {

    /**
     * Write a polymesh object to a .mesh file
     * @param mesh PolyMesh object to store in a file
     * @param fileName name of file to store (must include .mesh)
     * @exception FileWriteException if failure to convert given inputFilePath occurs. Which ExceptionHandler will be invoked for this exception throw.
     * @return a polymesh object
     */
    public static void writeMeshToFile(PolyMesh<Polygons> mesh, String fileName) {

        try {
            // Store in file named based off of input
            FileOutputStream fout = new FileOutputStream (fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(mesh);
            fout.close();

        } catch (Exception e) {
            ExceptionHandler.handleException(new FileWriteException(e.getMessage()));
        }
    }

}
