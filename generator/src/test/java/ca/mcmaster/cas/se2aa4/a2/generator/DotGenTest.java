package ca.mcmaster.cas.se2aa4.a2.generator;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import ca.team50.specification.MeshType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class DotGenTest {

    @Test
    public void meshIsNotNull() {

        PolyMesh<Polygons> mesh = DotGen.polygonGenerate(MeshType.GRID,500,500,625,0);
        assertNotNull(mesh);
    }

}
