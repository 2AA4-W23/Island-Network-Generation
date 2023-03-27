package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.Arctic.*;
import ca.team50.Tiles.LagoonTile;
import ca.team50.Tiles.LandTile;
import ca.team50.Tiles.OceanTile;
import ca.team50.Tiles.TileType;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Circle;
import ca.team50.shapes.Elipse;
import ca.team50.shapes.Irregular;
import ca.team50.shapes.IslandShape;
import ca.team50.elevation.*;
import ca.team50.water.RiverCentroidsGenerator;
import ca.team50.water.RiverGenerator;

import java.util.ArrayList;

import static ca.team50.elevation.Mountains.mountainAltitude;

public class TestIsland implements IslandGenerable {

    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        for (Polygons currentPolygon : mesh) {
            // Clean properties (i.e. set defaults)
            currentPolygon.cleanProperties();
        }

        Structs.Vertex centreOfMesh =  CanvasUtils.getCenter(mesh);

        // Get all tiles needed for generation (Command line argument : choose specific biome)
        TileType ocean = new OceanTile();
        TileType cold = new ColddesertTile();
        TileType taiga = new TaigaTile();
        TileType boreal = new BorealTile();
        TileType ice = new IcecapTile();
        TileType tundra = new TundraTile();

        //Commandline argument: choose shape, and island dimensions
        double height = 500;
        double width = 600;
        IslandShape elipse = new Elipse(CanvasUtils.getCenter(mesh),height,width,1.2);

        ArrayList<Polygons> islandPoly = new ArrayList<>();

        for (Polygons currentPolygon : mesh) {

            Structs.Vertex centroid = currentPolygon.getCentroid();

            if (elipse.isVertexInside(centroid))
                islandPoly.add(currentPolygon);

        }

        //Apply altimeric profile to entire mesh, base altitude and flutuation will come from a command line later
        double topAltitude = 1.0;
        double botAltitude = 0.0;
        double area = 25.0;

        //Volcano.volcanoAltitude(islandPoly, centreOfMesh, topAltitude, botAltitude, height, width, area);

        double numOf = 3.0;
        double slopeRadius = 300;

        mountainAltitude(islandPoly, numOf, topAltitude, botAltitude, slopeRadius);

        RiverCentroidsGenerator rivers = new RiverCentroidsGenerator(mesh, 3, 0.8);

            // Loop through all polygons (Eventually gonna be part of a biome interface)
        for (Polygons currentPolygon : mesh) {

            Structs.Vertex centroid = currentPolygon.getCentroid();
            // Check where the polygon is located and colour it accordingly
            if (elipse.isVertexInside(centroid)) {
                double polygonAltitude = extractProperties(centroid.getPropertiesList(), "altitude");

                if ( polygonAltitude < 0.2 && 0 <= polygonAltitude)
                    currentPolygon.unifyColor(boreal.getTileColour());
                else if(polygonAltitude < 0.4 && 0.2 <= polygonAltitude)
                    currentPolygon.unifyColor(taiga.getTileColour());
                else if(polygonAltitude < 0.6 && 0.4 <= polygonAltitude)
                    currentPolygon.unifyColor(tundra.getTileColour());
                else if(polygonAltitude < 0.8 && 0.6 <= polygonAltitude)
                    currentPolygon.unifyColor(cold.getTileColour());
                else
                    currentPolygon.unifyColor(ice.getTileColour());


            } else {
                currentPolygon.cleanProperties();
                currentPolygon.unifyColor(ocean.getTileColour());
            }

        }

    }

    public static double extractProperties(java.util.List<Structs.Property> properties, String property){

        String val = "0";
        for(Structs.Property p: properties) {
            if (p.getKey().equals(property)) {
                val = p.getValue();
            }
        }

        return  Double.parseDouble(val);
    }
}
