/*
 *  Copyright 2012 Peter Karich 
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.storage;

import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.RawEdgeIterator;
import com.graphhopper.util.shapes.BBox;

/**
 * An interface to represent a (geo) graph - suited for efficient storage as it
 * can be requested via ids. Querying via lat,lon can be done via with a
 * Location2IDIndex implementation.
 *
 * @author Peter Karich,
 */
public interface Graph {

    /**
     * @return the number of created locations - via setNode() or edge()
     */
    int getNodes();

    /**
     * This method ensures that the node with the specified index exists and
     * sets the lat+lon to the specified values. The index goes from 0
     * (inclusive) to getNodes() (exclusive)
     */
    void setNode(int index, double lat, double lon);

    double getLatitude(int index);

    double getLongitude(int index);

    /**
     * Returns the implicit bounds of this graph calculated from the lat,lon
     * input of setNode
     */
    BBox getBounds();

    /**
     * Creates an edge between the nodes a and b.
     *
     * @param a the index of the starting (tower) node of the edge
     * @param b the index of the ending (tower) node of the edge
     * @param distance between a and b. Often setNode is not called - if it is
     * not a geo-graph - and we need the distance parameter here.
     * @param flags see EdgeFlags - involves velocity and direction
     * @return the created edge
     */
    EdgeIterator edge(int a, int b, double distance, int flags);

    EdgeIterator edge(int a, int b, double distance, boolean bothDirections);

    /**
     * The returned EdgeIterator will return endNode for node() or if endNode is
     * negative node() will return the bigger node (nodeB).
     *
     * @return an edge iterator over one element where the method next() has no
     * meaning and will return false.
     * @throws IllegalStateException if edgeId is not valid
     */
    EdgeIterator getEdgeProps(int edgeId, int endNode);

    /**
     * @return all edges in this graph
     */
    RawEdgeIterator getAllEdges();

    /**
     * Returns an iterator which makes it possible to traverse all edges of the
     * specified node index. Hint: use GraphUtility to go straight to certain
     * neighbor nodes. Hint: edges with both directions will returned only once!
     */
    EdgeIterator getEdges(int index);

    EdgeIterator getIncoming(int index);

    EdgeIterator getOutgoing(int index);

    /**
     * @return the specified graph g
     */
    Graph copyTo(Graph g);

    /**
     * Schedule the deletion of the specified node until an optimize() call
     * happens
     */
    void markNodeDeleted(int index);

    boolean isNodeDeleted(int index);

    /**
     * Performs optimization routines like deletion or node rearrangements.
     */
    void optimize();
}
