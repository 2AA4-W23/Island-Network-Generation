package ca.team50.generators;

import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.generation.RandomGen;
import ca.team50.generation.VoronoiGen;
import ca.team50.translation.JtsTranslation;

public class Irregular implements Generable {

    private final int relaxLevel;

    public Irregular(int relaxLevel) {
        this.relaxLevel = relaxLevel;
    }

    @Override
    public PolyMesh<Polygons> generate(int width, int height, int numOfPolygons) {
        // First, generate the voronoi diagram given the specified arguments
        // RandomGen.genCoords generates X number of unique coordinates to draw polygons around
        VoronoiGen vDiagram = new VoronoiGen(width,height, RandomGen.genCoords(width,height,numOfPolygons));

        // Relax the diagram
        vDiagram.relax(this.relaxLevel);

        // Finally, the outputted diagram is in the form of a Geometry JTS object thus convert to PolyMesh and return
        return JtsTranslation.convertToPolyMesh(vDiagram.getGeoCollection());
    }
}
