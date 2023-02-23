package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.team50.adt.PolyMesh;
import ca.team50.adt.Polygons;

import java.io.IOException;
import java.util.List;

public class MeshDump {

    public void dump(PolyMesh aMesh) {
        PolyMesh<Polygons> mesh = aMesh;

        for (Polygons poly : mesh){
            List<Vertex> vertices = poly.getVerticesList();
            System.out.println("|Vertices| = " + vertices.size());
            for (Vertex v : vertices){
                StringBuffer line = new StringBuffer();
                line.append(String.format("(%.2f,%.2f)",v.getX(), v.getY()));
                line.append(" [");
                for(Property p: v.getPropertiesList()){
                    line.append(String.format("%s -> %s, ", p.getKey(), p.getValue()));
                }
                line.append("]");
                System.out.println(line);
            }
        }
    }
}