package com.aaroncarsonart.imbroglio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Used as a general-purpose graph data structure.  This can be directed or undirected,
 * and allows self-edges.
 */
public class Graph<E> {
    private HashMap<E, HashSet<E>> edgeLists;

    /**
     * Create a new Graph with the specified number of vertices, accessible by index.
     */
    public Graph(){
        edgeLists = new HashMap<E, HashSet<E>>();
    }

    /**
     * Get the number of Vertices.
     * @return The number of vertices
     */
    public int vertices(){
        return edgeLists.size();
    }

    /**
     * Get the number of directed edges.
     * @return The number of edges
     */
    public int edges(){
        int value = 0;
        for(HashSet<E> list : edgeLists.values()){
            for(E e : list){
                value++;
            }
        }
        return value;
    }

    /**
     * Add a vertex to this graph.
     * @param vertex The vertex to add.
     */
    public void addVertex(E vertex){
        edgeLists.put(vertex, new HashSet<E>());
    }

    /**
     * Get all Vertices as a set.
     * @return All Vertices
     */
    public Set<E> getVertices(){
        return edgeLists.keySet();
    }

    /**
     * Get the List of adjacent vertices for the given vertex.
     * @param vertex
     * @return A set of the vertices this vertex has an edge to.
     */
    public HashSet<E> getEdges(E vertex){
        return edgeLists.get(vertex);
    }

    /**
     * Add a directed edge to this graph from v1 to v2.
     * @param v1 The tail of the edge.
     * @param v2 The head of the edge.
     */
    public void addDirectedEdge(E v1, E v2){
        edgeLists.get(v1).add(v2);
    }

    /**
     * Add a an undirected edge to this graph for v1 and v2.
     * @param v1 The first vertex of the edge.
     * @param v2 The second vertex of the edge.
     */
    public void addUndirectedEdge(E v1, E v2){
        addDirectedEdge(v1,v2);
        addDirectedEdge(v2,v1);
    }

    /**
     * Add a an undirected edge to this graph for v1 and v2.
     * @param v1 The first vertex of the edge.
     * @param v2 The second vertex of the edge.
     */
    public void addEdge(E v1, E v2){
        edgeLists.get(v1).add(v2);
        edgeLists.get(v2).add(v1);
    }

    /**
     * Test the implementation.
     * @param args Does nothing
     */
    public static void main(String[] args){
        Graph<String> g = new Graph<String>();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");

        g.addEdge("A","B");
        g.addEdge("B","C");
        g.addEdge("C","D");
        g.addEdge("D","E");
        g.addEdge("E","F");
        g.addEdge("F","G");
        g.addEdge("G","A");
        g.addEdge("G","B");
        g.addEdge("G","C");
        g.addEdge("G","D");
        g.addEdge("G","E");
        g.addEdge("G","F");

        for(String vertex: g.getVertices()){
            System.out.printf("Edges from %s: ", vertex);
            for(String edge : g.getEdges(vertex)){
                System.out.printf("%s ", edge);
            }
            System.out.println();


        }

    }
}
