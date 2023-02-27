package ca.mcmaster.cas.se2aa4.a2.generator;

import java.util.*;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.RandomGen;
import ca.team50.generation.VoronoiGen;
import ca.team50.generators.Generable;
import ca.team50.generators.Grid;
import ca.team50.generators.Irregular;
import ca.team50.specification.MeshType;
import ca.team50.translation.JtsTranslation;


public class DotGen {


    public static PolyMesh<Polygons> polygonGenerate(MeshType meshType, int width, int height, int numOfPolygons, int relaxLevel) {

        System.out.println("Generating mesh type: " + meshType.name());
        System.out.println("Canvas Width: " + width + ", Height: " + height);
        System.out.println("Number of Polygons: " + numOfPolygons);
        System.out.println("Relax Level: " + relaxLevel);

        // Define static Generable type
        Generable gen;

        // Generate grid mesh
        if (meshType == MeshType.GRID) {
            System.out.println("Since mesh is GRID type, relax level will be ignored!");
            gen = new Grid();
            return gen.generate(width, height, numOfPolygons);

            // Irregular mesh gen
        } else {
            gen = new Irregular(relaxLevel);
            return gen.generate(width, height, numOfPolygons);

        }
    
    } 

}

