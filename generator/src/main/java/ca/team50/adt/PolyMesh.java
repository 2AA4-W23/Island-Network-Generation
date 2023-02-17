package ca.team50.adt;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;

import java.util.*;

// Any class which derives from the Polygons class can be used to create a mesh
public class PolyMesh<T extends Polygons> implements Collection<T> {

    // Mesh is a collection of Polygons and can operate on them
    private Polygons[] polygonsArray = new Polygons[1];
    private int arrayCurrentSize = 0;

    @Override
    public int size() {
        return this.arrayCurrentSize;
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
                if (this.polygonsArray[index] == testPolygon) {
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
            returnArray[index] = this.polygonsArray[index];
        }

        return returnArray;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {

        if (!contains(t)) {
            if (size() == this.polygonsArray.length) {
                grow();
            }

            this.polygonsArray[arrayCurrentSize] = t;
            this.arrayCurrentSize++;

            return true;
        }

        return false;

    }

    @Override
    public boolean remove(Object o) {

        for (int index = 0; index < size(); index++) {

            if (this.polygonsArray[index]==o) {
                this.polygonsArray[index] = null;

                int previousIndex = index;

                for (int forwardIndex = index+1; forwardIndex < size(); forwardIndex++) {

                    this.polygonsArray[previousIndex] = this.polygonsArray[forwardIndex];

                    previousIndex++;

                }

                this.polygonsArray[size()-1] = null;
                this.arrayCurrentSize--;

                return true;

            }


        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        for (Object currentObject : c) {

            try {

                boolean doesPolygonExist = false;

                Polygons currentPolygon = (Polygons) currentObject;

                Iterator<T> iterator = iterator();

                while (iterator.hasNext()) {
                    Polygons checkingPolygons = iterator.next();
                    if (checkingPolygons == currentPolygon) {
                        doesPolygonExist = true;
                        break;
                    }
                }

                if (!doesPolygonExist) {
                    return false;
                }

            } catch (ClassCastException e) {
                return false;
            }

        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {

        boolean collectionModified = false;

        for (T currentPolygon : c) {
            if(this.add(currentPolygon)) {
                collectionModified = true;
            }
        }

        return collectionModified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        boolean collectionModified = false;

        for (Object currentObject : c) {

            try {

                Polygons currentPolygon = (Polygons) currentObject;

                if(this.remove(currentPolygon)) {
                    collectionModified = true;
                }

            } catch (ClassCastException e) {
                return false;
            }
        }

        return collectionModified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        boolean collectionModified = false;

        while(true) {

            boolean isDone = true;

            for (int index = 0; index < size(); index++){
                if (!c.contains(this.polygonsArray[index])) {
                    this.remove(this.polygonsArray[index]);
                    collectionModified = true;
                    isDone = false;
                    break;
                }
            }

            if (isDone) {
                break;
            }

        }

        return collectionModified;
    }

    @Override
    public void clear() {
        this.polygonsArray = new Polygons[1];
        this.arrayCurrentSize = 0;
    }

    public T get(int index) {
        if (index < size()) {
            return (T) this.polygonsArray[index];
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is not within collection range");
        }
    }

    public boolean remove(int index) {

        if (index < size()) {

            this.polygonsArray[index] = null;

            int previousIndex = index;

            for (int forwardIndex = index+1; forwardIndex < size(); forwardIndex++) {

                this.polygonsArray[previousIndex] = this.polygonsArray[forwardIndex];

                previousIndex++;

            }

            this.polygonsArray[size()-1] = null;
            this.arrayCurrentSize--;

            return true;

        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is not within collection range");
        }
    }

    private void grow() {
        Polygons[] newArray = new Polygons[this.polygonsArray.length*2];

        for (int index = 0; index < size(); index++) {
            newArray[index] = this.polygonsArray[index];
        }

        this.polygonsArray = newArray;

    }

    /**
     * Comparison to check if two polygons share a segment
     * @param index1 the first polygon index position in the collection to compare
     * @param index2 the second polygon index position in the collection to compare
     * @exception IndexOutOfBoundsException if one or more of the two indices passed are out of bounds in the collection
     * @return the boolean result of the comparison (true if they share a segment, false otherwise)
     */
    public boolean isNeighbor(int index1, int index2) {

        // Check if polygons share at least one segment

        try {

            Polygons polygon1 = this.get(index1);
            Polygons polygon2 = this.get(index2);

            for (Segment polygon1Segments : polygon1.getSegmentsList()) {

                Vertex Vertex1Idx1 = polygon1.getVerticesList().get(polygon1Segments.getV1Idx());
                Vertex Vertex1Idx2 = polygon1.getVerticesList().get(polygon1Segments.getV2Idx());

                for (Segment polygon2Segments : polygon2.getSegmentsList()) {

                    Vertex Vertex2Idx1 = polygon2.getVerticesList().get(polygon2Segments.getV1Idx());
                    Vertex Vertex2Idx2 = polygon2.getVerticesList().get(polygon2Segments.getV2Idx());


                    if ((Vertex1Idx1.getX() == Vertex2Idx1.getX() && Vertex1Idx1.getY() == Vertex2Idx1.getY()) || (Vertex1Idx1.getX() == Vertex2Idx2.getX() && Vertex1Idx1.getY() == Vertex2Idx2.getY())) {
                        if ((Vertex1Idx2.getX() == Vertex2Idx1.getX() && Vertex1Idx2.getY() == Vertex2Idx1.getY()) || (Vertex1Idx2.getX() == Vertex2Idx2.getX() && Vertex1Idx2.getY() == Vertex2Idx2.getY())) {
                            return true;
                        }
                    }


                }

            }

            return false;

        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("One or more indices specified are out of range: " + e.getMessage());
        }


    }



}
