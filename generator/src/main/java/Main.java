import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        DotGen generator = new DotGen();
        DotGen.polygonGenerate();
        polygonsArray.writeTo(new FileOutputStream(args[0]));
        
    }

}
