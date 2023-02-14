package ca.team50.adt;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PolyMesh<Polygons>  {

    List<Polygons> polygonsList;

    // Mesh is a collection of Polygons and can operate on them
    boolean isNeighbor(Polygons polygon1, Polygons polygon2) {

        boolean polygon1ExistsInList = polygonsList.contains(polygon1);
        boolean polygon2ExistsInList = polygonsList.contains(polygon2);

        if (polygon1ExistsInList && polygon2ExistsInList) {

            Iterator<Polygons> listCheck1 = polygonsList.iterator();
            Iterator<Polygons> listCheck2 = polygonsList.iterator();
            listCheck2.next();

            while (listCheck2.hasNext()) {

                Polygons check1 = listCheck1.next();
                Polygons check2 = listCheck2.next();

                if (check1 == polygon1 && check2 == polygon2) {
                    return true;
                }

            }

            return false;

        } else {
            throw new NoSuchElementException("One or more polygon objects do not exist in the list");
        }
    }


}
