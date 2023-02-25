import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.specification.CLInterface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {

    public static void main(String[] args) throws IOException {

        // Parse args input
        CLInterface parsedInput = new CLInterface(args);

        PolyMesh<Polygons> mesh = DotGen.polygonGenerate();
        FileOutputStream fout= new FileOutputStream (args[0]);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(mesh);
        fout.close();
        
    }

}
