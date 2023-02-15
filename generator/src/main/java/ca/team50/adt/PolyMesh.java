package ca.team50.adt;

import java.util.*;

// Any class which derives from the Polygons class can be used to create a mesh
public class PolyMesh<T extends Polygons> implements Collection<T>{

    // Mesh is a collection of Polygons and can operate on them
    Polygons[] polygonsArray = new Polygons[1];
    int arrayCurrentSize = 0;

    boolean isNeighbor(T polygon1, T polygon2) {
        return true;
    }


    @Override
    public int size() {
        return arrayCurrentSize;
    }

    @Override
    public boolean isEmpty() {
        return (size()==0);
    }

    @Override
    public boolean contains(Object o) {

        try {
            Polygons testPolygon = (Polygons) o;

            for (int index = 0; index < size(); index++) {
                if (polygonsArray[index] == testPolygon) {
                    return true;
                }
            }

        } catch (ClassCastException e) {
            return false;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int pos = 0;
            @Override
            public boolean hasNext() {
                return (size()>pos);
            }

            @Override
            public T next() {
                return (T) polygonsArray[pos++];
            }
        };
    }

    @Override
    public Object[] toArray() {

        Polygons[] returnArray = new Polygons[size()];

        for (int index = 0; index < size(); index++) {
            returnArray[index] = polygonsArray[index];
        }

        return returnArray;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {

        if (size() == polygonsArray.length) {
            allocateNewSpace();
        }

        polygonsArray[arrayCurrentSize] = t;
        arrayCurrentSize++;

        return true;
    }

    @Override
    public boolean remove(Object o) {

        for (int index = 0; index < size(); index++) {

            if (polygonsArray[index]==o) {
                polygonsArray[index] = null;

                int previousIndex = index;

                for (int forwardIndex = index+1; forwardIndex < size(); forwardIndex++) {

                    polygonsArray[previousIndex] = polygonsArray[forwardIndex];

                    previousIndex++;

                }

                polygonsArray[size()-1] = null;
                arrayCurrentSize--;

                return true;

            }


        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        polygonsArray = new Polygons[1];
        arrayCurrentSize = 0;
    }

    private void allocateNewSpace() {
        Polygons[] newArray = new Polygons[polygonsArray.length*2];

        for (int index = 0; index < size(); index++) {
            newArray[index] = polygonsArray[index];
        }

        this.polygonsArray = newArray;

    }


}
