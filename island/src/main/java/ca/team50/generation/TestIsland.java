package ca.team50.generation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.Tiles.Arctic.BorealTile;
import ca.team50.Tiles.Arctic.TaigaTile;
import ca.team50.Tiles.Arctic.TundraTile;
import ca.team50.Tiles.LandTile;
import ca.team50.Tiles.OceanTile;
import ca.team50.Tiles.TileType;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;
import ca.team50.shapes.Elipse;
import ca.team50.shapes.Irregular;
import ca.team50.shapes.IslandShape;
import ca.team50.elevation.*;

public class TestIsland implements IslandGenerable {

    @Override
    public void generateIsland(PolyMesh<Polygons> mesh) {

        for (Polygons currentPolygon : mesh) {
            // Clean properties (i.e. set defaults)
            currentPolygon.cleanProperties();
        }

        // Get all tiles needed for generation (Command line argument : choose specific biome)
        TileType ocean = new OceanTile();
        TileType land = new LandTile();
        TileType lAltitude = new TundraTile();
        TileType mAltitude = new TaigaTile();
        TileType hAltitude = new BorealTile();

        //Commandline argument: choose shape, and island dimensions
        double height = 300;
        double width = 500;
        IslandShape elipse = new Elipse(CanvasUtils.getCenter(mesh),height,width,1.2);

        //Apply altimeric profile to entire mesh, base altitude and flutuation will come from a command line later
        double altitude = 0.3;
        double fluctuation = 0.2;
        Plains.plainsAltitude(mesh, altitude, fluctuation);


        // Loop through all polygons
        for (Polygons currentPolygon : mesh) {

            Structs.Vertex centroid = currentPolygon.getCentroid();
            // Check where the polygon is located and colour it accordingly
            if (elipse.isVertexInside(centroid)) {
                Double polygonAltitude = extractProperties(centroid.getPropertiesList(), "altitude");
                if ( polygonAltitude < 0.23 && 0.1 < polygonAltitude)
                    currentPolygon.unifyColor(lAltitude.getTileColour());
                else if(polygonAltitude > 0.23 && polygonAltitude < 0.36)
                    currentPolygon.unifyColor(hAltitude.getTileColour());
                else
                    currentPolygon.unifyColor(mAltitude.getTileColour());

            } else {
                currentPolygon.cleanProperties();
                currentPolygon.unifyColor(ocean.getTileColour());
            }

        }

    }

    public double extractProperties(java.util.List<Structs.Property> properties, String property){

        String val = "0";
        for(Structs.Property p: properties) {
            if (p.getKey().equals(property)) {
                val = p.getValue();
            }
        }

        return  Double.parseDouble(val);
    }
}
