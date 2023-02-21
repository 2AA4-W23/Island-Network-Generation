package ca.team50.adt;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;

import java.util.*;

// Any class which derives from the Polygons class can be used to create a mesh

/**
 * A collection to store and refer to Polygons that make up a mesh.
 * While this collection realizes the collection interface, it also calculates all drawable segments as two Vertices which can be referenced for drawing.
 * If a method does not specify if either Polygon or Segment collection is used, it is assumed to be Polygon
 * @return A PolyMesh object in which only specified Polygon class types are allowed to be operated on
 */
public class PolyMesh<T extends Polygons> implements Collection<T> {

    // Mesh is a collection of Polygons and can operate on them

    public Polygons[] polygonsArray = new Polygons[1];
    public Vertex[][] drawableSegmentsArray = new Vertex[1][2];
    public int arrayCurrentSize = 0;
    public int segmentsArrayCurrentSize = 0;


    // ----------All methods for Polygons----------

    /**
     * Get the size of the Polygon collection
     * @return an integer value representing the current size of the Polygon collection
     */
    @Override
    public int size() {
        return this.arrayCurrentSize;
    }

    /**
     * Check if the size of the Polygon and Segment collection are both empty
     * @return true if empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (size()==0);
    }

    /**
     * Check if a Polygon exists within the Polygon collection
     * @param o the Polygon object to check for existence
     * @return true if the Polygon exists within the segment collection, false otherwise
     */
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

    /**
     * Add a Polygon Object to the collection. Note that this subsequently updates the drawable segments collection to account for the added polygon
     * @param t the Polygon object to add
     * @return true if the collection was modified as a result of the addition
     */
    @Override
    public boolean add(T t) {

        if (!contains(t)) {
            if (size() == this.polygonsArray.length) {
                grow();
            }

            this.polygonsArray[arrayCurrentSize] = t;
            this.arrayCurrentSize++;

            calculateDrawableSegments();

            return true;
        }

        return false;

    }

    /**
     * Removes the corresponding Polygons object (or subclass derivative) from the polygon collection. Note that this subsequently updates the drawable segments collection to account for the removed polygon
     * @param o the Polygon object to delete
     * @exception IndexOutOfBoundsException if the index passed is out of bounds in the collection
     * @return true if the collection was modified as a result of the removal
     */
    @Override
    public boolean remove(Object o) {

        for (int index = 0; index < size(); index++) {

            if (this.polygonsArray[index]==o) {

                removeSegments(this.polygonsArray[index],index);

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

                for (int index = 0; index < size(); index++) {

                    if (currentPolygon == this.polygonsArray[index]) {
                        removeSegments(this.polygonsArray[index],index);
                        break;
                    }
                }

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
                    removeSegments(this.polygonsArray[index],index);
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

        this.drawableSegmentsArray = new Vertex[1][2];
        this.segmentsArrayCurrentSize = 0;

    }

    public T get(int index) {
        if (index < size()) {
            return (T) this.polygonsArray[index];
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is not within collection range");
        }
    }

    /**
     * Removes the corresponding index of a polygon from the polygon collection. Note that this subsequently updates the drawable segments collection to account for the removed polygon
     * @param index the index position to delete
     * @exception IndexOutOfBoundsException if the index passed is out of bounds in the collection
     * @return true if the collection was modified as a result of the removal
     */
    public boolean remove(int index) {

        if (index < size()) {

            removeSegments(this.polygonsArray[index],index);

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


                    if (areSegmentsEqual(Vertex1Idx1,Vertex1Idx2,Vertex2Idx1,Vertex2Idx2)) {
                        return true;
                    }


                }

            }

            return false;

        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("One or more indices specified are out of range: " + e.getMessage());
        }


    }

    /**
     * Comparison to check if two polygons share segments and if so specify which ones
     * @param polygon1 the first polygon object
     * @param polygon2 the second polygon object
     * @return an array of type Segment which specifies which segments are being shared between polygon1 and polygon2. The vertex indices of each shared segment is relative to polygon1
     */
    private Segment[] isNeighborSpecific(Polygons polygon1, Polygons polygon2) {

        Segment[] itemsToReturn = new Segment[polygon1.getSegmentsList().size()];

        int index = 0;

        for (Segment polygon1Segments : polygon1.getSegmentsList()) {

            Vertex Vertex1Idx1 = polygon1.getVerticesList().get(polygon1Segments.getV1Idx());
            Vertex Vertex1Idx2 = polygon1.getVerticesList().get(polygon1Segments.getV2Idx());

            for (Segment polygon2Segments : polygon2.getSegmentsList()) {

                Vertex Vertex2Idx1 = polygon2.getVerticesList().get(polygon2Segments.getV1Idx());
                Vertex Vertex2Idx2 = polygon2.getVerticesList().get(polygon2Segments.getV2Idx());

                if (areSegmentsEqual(Vertex1Idx1,Vertex1Idx2,Vertex2Idx1,Vertex2Idx2)) {
                    itemsToReturn[index] = polygon1Segments;
                    index++;
                }

            }

        }

        Segment[] returnArray = new Segment[index];

        for (int checkIndex = 0; checkIndex < index; checkIndex++) {

            returnArray[checkIndex] = itemsToReturn[checkIndex];

        }

        return returnArray;

    }

    // ----------All methods for segments----------

    private boolean areSegmentsEqual(Vertex seg1Vertex1, Vertex seg1Vertex2, Vertex seg2Vertex1, Vertex seg2Vertex2) {

        if ((seg1Vertex1.getX() == seg2Vertex1.getX() || seg1Vertex1.getX() == seg2Vertex2.getX()) && (seg1Vertex1.getY() == seg2Vertex1.getY() || seg1Vertex1.getY() == seg2Vertex2.getY())) {
            if ((seg1Vertex2.getX() == seg2Vertex1.getX() || seg1Vertex2.getX() == seg2Vertex2.getX()) && (seg1Vertex2.getY() == seg2Vertex1.getY() || seg1Vertex2.getY() == seg2Vertex2.getY())) {
                return true;
            }
        }

        return false;

    }

    /**
     * Get the size of the segment collection
     * @return an integer value representing the current size of the segment collection
     */
    public int segmentSize() {
        return this.segmentsArrayCurrentSize;
    }

    // Entrance method to check if the segments collection needs to be updated (typically if a polygon is added)
    private void calculateDrawableSegments() {

        Polygons currentPolygon = this.polygonsArray[size()-1];

        List<Vertex> vertexList = currentPolygon.getVerticesList();

        for (Segment currentSegment : currentPolygon.getSegmentsList()) {

            Vertex Idx1 = vertexList.get(currentSegment.getV1Idx());
            Vertex Idx2 = vertexList.get(currentSegment.getV2Idx());

            this.addSegment(Idx1,Idx2);

        }

    }

    // Method to grow segments array if it fills up
    private void growSegments() {

        Vertex[][] newArray = new Vertex[this.drawableSegmentsArray.length*2][2];

        for (int index = 0; index < segmentSize(); index++) {
            newArray[index][0] = this.drawableSegmentsArray[index][0];
            newArray[index][1] = this.drawableSegmentsArray[index][1];
        }

        this.drawableSegmentsArray = newArray;

    }

    /**
     * Gets a corresponding Vertex array composed of a start Vertex and an end Vertex
     * @param index the index to retrieve in the Segment collection (NOT polygon collection)
     * @exception IndexOutOfBoundsException if one or more of the two indices passed are out of bounds in the collection
     * @return an array of two Vertex objects, a starting Vertex and an ending Vertex that make up one Segment
     */
    public Vertex[] getSegment(int index) {

        if (index < segmentSize()) {
            return new Vertex[]{this.drawableSegmentsArray[index][0], this.drawableSegmentsArray[index][1]};
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is not within segment collection range");
        }

    }

    // Batch remove all segments that are not shared by the given Polygon to be removed
    private void removeSegments(Polygons polygonToRemove, int indexOfRemoval) {

        // Create a boolean array of the same size as the Segments list in the polygon
        // These will be set to true or false depending on if they are shared by another polygon
        boolean[] segmentsToKeepBool = new boolean[polygonToRemove.getSegmentsList().size()];

        // Loop through all polygons in the polygon collection
        for (int checkingPolygonIndex = 0; checkingPolygonIndex < size(); checkingPolygonIndex++) {

            // Get the next polygon to check
            Polygons checkingPolygon = this.polygonsArray[checkingPolygonIndex];

            // Check if it is a neighbour of the polygon to remove (and not the same polygon)
            if (isNeighbor(indexOfRemoval,checkingPolygonIndex) && checkingPolygon != polygonToRemove) {

                // Calculate all segments that are shared by the two polygons
                Segment[] segmentsToKeep = isNeighborSpecific(polygonToRemove,checkingPolygon);

                int boolindex = 0;

                // For each segment that is shared, set the corresponding position in the index of the boolean array to true
                for (Segment polygonToRemoveSegment : polygonToRemove.getSegmentsList()) {

                    segmentsToKeepBool[boolindex] = false;

                    Vertex Vertex1Idx1 = polygonToRemove.getVerticesList().get(polygonToRemoveSegment.getV1Idx());
                    Vertex Vertex1Idx2 = polygonToRemove.getVerticesList().get(polygonToRemoveSegment.getV2Idx());

                    for (Segment segmentKeeping : segmentsToKeep) {

                        Vertex Vertex2Idx1 = polygonToRemove.getVerticesList().get(segmentKeeping.getV1Idx());
                        Vertex Vertex2Idx2 = polygonToRemove.getVerticesList().get(segmentKeeping.getV2Idx());

                        if (areSegmentsEqual(Vertex1Idx1,Vertex1Idx2,Vertex2Idx1,Vertex2Idx2)) {

                            segmentsToKeepBool[boolindex] = true;
                            break;

                        }

                    }

                    boolindex++;

                }

            }

        }

        int boolIndex = 0;

        // Remove all corresponding segments from the segments array given the boolean list
        for (Segment currentSegment : polygonToRemove.getSegmentsList()) {

            if (!segmentsToKeepBool[boolIndex]) {

                Vertex idx1 = polygonToRemove.getVerticesList().get(currentSegment.getV1Idx());
                Vertex idx2 = polygonToRemove.getVerticesList().get(currentSegment.getV2Idx());

                removeSegment(idx1,idx2);
            }

            boolIndex++;

        }


    }

    // Method to remove a specific segment from the segments array
    // Note this method does as it's told and removes segments without checking neighbours
    private void removeSegment(Vertex idx1, Vertex idx2) {

        int removalIndex = 0;

        for (int index = 0; index < segmentsArrayCurrentSize; index++) {

            Vertex[] testVertex = this.drawableSegmentsArray[index];

            Vertex testVertex1 = testVertex[0];
            Vertex testVertex2 = testVertex[1];

            if (areSegmentsEqual(idx1,idx2,testVertex1,testVertex2)) {

                break;

            }

            removalIndex++;

        }

        // Once the segment is removed, move everything in array 1 over
        this.drawableSegmentsArray[removalIndex][0] = null;
        this.drawableSegmentsArray[removalIndex][1] = null;

        int previousIndex = removalIndex;

        for (int forwardIndex = removalIndex+1; forwardIndex < segmentSize(); forwardIndex++) {

            this.drawableSegmentsArray[previousIndex][0] = this.drawableSegmentsArray[forwardIndex][0];
            this.drawableSegmentsArray[previousIndex][1] = this.drawableSegmentsArray[forwardIndex][1];

            previousIndex++;

        }

        this.drawableSegmentsArray[segmentSize()-1][0] = null;
        this.drawableSegmentsArray[segmentSize()-1][1] = null;
        this.segmentsArrayCurrentSize--;


    }

    private void addSegment(Vertex Idx1, Vertex Idx2) {

        if (!containsSegment(Idx1,Idx2)) {

            if (segmentSize() == this.drawableSegmentsArray.length) {
                growSegments();
            }

            this.drawableSegmentsArray[segmentsArrayCurrentSize][0] = Idx1;
            this.drawableSegmentsArray[segmentsArrayCurrentSize][1] = Idx2;

            this.segmentsArrayCurrentSize++;

        }

    }

    /**
     * Check if a Segment exists within the segment collection
     * @param idx1 the starting Vertex of the Segment
     * @param idx2 the end Vertex of the Segment
     * @return true if the Segment exists within the segment collection, false otherwise
     */
    public boolean containsSegment(Vertex idx1, Vertex idx2) {

        for (int index = 0; index < segmentsArrayCurrentSize; index++) {

            Vertex[] segment = this.drawableSegmentsArray[index];

            if ((segment[0].getX() == idx1.getX() && segment[0].getY() == idx1.getY()) || (segment[0].getX() == idx2.getX() && segment[0].getY() == idx2.getY())) {
                if ((segment[1].getX() == idx1.getX() && segment[1].getY() == idx1.getY()) || (segment[1].getX() == idx2.getX() && segment[1].getY() == idx2.getY())) {
                    return true;
                }

            }

        }

        return false;

    }



}