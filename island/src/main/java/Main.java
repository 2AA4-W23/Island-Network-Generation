import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.exceptions.ExceptionHandler;
import ca.team50.exceptions.InvalidCommandFormatException;
import ca.team50.fileIO.FileToPolyMesh;
import ca.team50.specification.CLInterface;

public class Main {

    public static void main(String[] args) {

        // Get all arguments from command line
        CLInterface cli = new CLInterface(args);

        // Convert corresponding input .mesh file into a PolyMesh
        PolyMesh<Polygons> polyMesh = FileToPolyMesh.getPolyMeshFromFile(cli.getMeshInput());


    }

}
