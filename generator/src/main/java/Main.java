import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.obj.OBJFormat;
import ca.team50.specification.CLInterface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {

    public static void main(String[] args) throws IOException {

        // Parse args input
        CLInterface parsedInput = new CLInterface(args);

        // Generate corresponding mesh
        PolyMesh<Polygons> mesh = DotGen.polygonGenerate(parsedInput.getMeshType(), parsedInput.getCanvasWidth(), parsedInput.getCanvasHeight(), parsedInput.getNumOfPolygons(), parsedInput.getRelaxLevel());
        // Store in file named based off of input
        FileOutputStream fout= new FileOutputStream (parsedInput.getMeshName()+".mesh");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(mesh);
        fout.close();

        if (parsedInput.isWillExportAsObj()) {
            OBJFormat.writeToObj(mesh, parsedInput.getMeshName());
            System.out.println("Mesh has been generated in both .obj and .mesh formats!");
            System.out.println("Please use visualizer.jar to convert to .mesh to svg and view it!");
        } else {
            System.out.println("Mesh has been generated! Please use visualizer.jar to convert to svg and view it!");
        }
        
    }

}
