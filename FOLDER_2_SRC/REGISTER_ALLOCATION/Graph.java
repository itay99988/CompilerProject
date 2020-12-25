package REGISTER_ALLOCATION;

import java.util.*;

public class Graph {

    private LinkedList<Integer> adjacencyList[];
    private int[] degrees;

    private static final int UNAVAILABLE = -1;


    // constructor
    public Graph(int numOfVertices){
        this.adjacencyList = new LinkedList[numOfVertices];
        for (int i=0; i < numOfVertices; ++i)
            adjacencyList[i] = new LinkedList<>();

        degrees = new int[numOfVertices];
    }


    /**
     * Adds edge (u,v) to the graph.
     */
    public void addEdge(int u, int v) {
        // add u to v's adjacency list and vice versa
        // and also update the vertices' degrees
        if(!adjacencyList[u].contains(v)){
            adjacencyList[u].add(v);
            degrees[u]++;
        }
        // the graph is undirected - do the same to v
        if(!adjacencyList[v].contains(u)) {
            adjacencyList[v].add(u);
            degrees[v]++;
        }
    }


    /**
     * Returns vertex in graph of max degree
     */
    private int getMaxDegreeVertex() {
        int max = Integer.MIN_VALUE;
        int maxIdx = -1;
        for(int i = 0; i < degrees.length; i++){
            if(degrees[i] > max){
                max = degrees[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }


    /**
     * Assigns colors to all vertices.
     * Returns an Integer array where index i signifies the Temp_i and $ret[i] is the temp's color.
     */
    public Integer[] findColoring() {
        int numOfVertices = adjacencyList.length;
        Integer coloring[] = new Integer[numOfVertices];

        Arrays.fill(coloring, UNAVAILABLE);

        // vertex with max degree is assigned the first color
        int v = getMaxDegreeVertex();
        degrees[v] = UNAVAILABLE;
        coloring[v]  = 0; // first color

        // all colors that are available for the current vertex
        boolean isColorAvailable[] = new boolean[numOfVertices];
        Arrays.fill(isColorAvailable, true);

        // assign colors to the remaining vertices
        for(int i = 0; i < numOfVertices-1; i++) {
            v = getMaxDegreeVertex();
            // make sure this vertex will not be chosen again
            degrees[v] = UNAVAILABLE;

            // mark all colors assigned to adjacent vertices as unavailable
            for(int u : adjacencyList[v]) {
                if(coloring[u] != UNAVAILABLE) {
                    isColorAvailable[coloring[u]] = false;
                }
            }

            // find the first available color
            int color;
            for (color = 0; color < numOfVertices; color++){
                if (isColorAvailable[color])
                    break;
            }

            coloring[v] = color;

            // reset the values back to true for the next iteration
            Arrays.fill(isColorAvailable, true);
        }

        return coloring;
    }

}
