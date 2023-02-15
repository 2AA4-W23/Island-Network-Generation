package ca.team50.adt;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// Any class which derives from the Polygons class can be used to create a mesh
public class PolyMesh<T extends Polygons> {

    // Mesh is a collection of Polygons and can operate on them
    List<T> polygonsList;

    boolean isNeighbor(T polygon1, T polygon2) {
        return true;
    }


}
