/**
 * FileName: ImplementDijkstraAlgo.java
 * Introduction:
 * Implement Dijkstra's algorithm.
 *
 * Your graph must have at least 10 vertices and 20 edges.
 * Print out the graph - list of vertices and edges(pair of vertices)
 * Run dijkstra's algorithm.
 * Print the tree that results - list of vertices in the tree (same as above) and list of edges that make up the tree.
 * You may use heap library. That is the only library you can use.
 * Submit the code and screen shots of execution results
 *
 * Thought:
 * 1. I will design a graph class called MyGraph with randomInit function.
 * 2. Then I will write a print function for MyGraph to show vertices and edges.
 * 3. In the end, I will design a dijkstra's function to get the shortest path graph and then print it.
 * 4. I will provide a start point. If some vertice is seperated, I will set the vertice at heap top as the next start point.
 *
 * @author Chaoran Li
 * @Date 11/03/2019
 * @version 1.0
 */
package implementDijkstraAlgo;

import java.util.Random;
import java.util.Comparator;// self design compare for queue
import java.util.PriorityQueue;// java's heap library

public class ImplementDijkstraAlgo {
    /**
     * Vertice designed for MyGraph
     */
    private static class MyVertice{
        public int index = 0;
        public int key = Integer.MAX_VALUE;// positive infinite for int
        public MyVertice parent = null;
    }

    /**
     * Edge designed for MyGraph
     */
    private static class MyEdge{
        public MyVertice origin = null;
        public int weight = 0;
        public MyEdge next = null;
    }

    /**
     * Directed graph, use adjacency list to represent the edges
     */
    public static class MyGraph {
        public static final double RANDOMFACTOR = 0.99;// the probability to end randomInit after getting enough edges.
        public static final int RANDOMRANGE = 100;// edge weight from 0 to RANDOMRANGE
        public static final int MINVERTICENUM = 10;// minimum number of vertices
        public static final int MINEDGENUM = 20;// minimum number of edges
        public MyVertice vertices[] = new MyVertice[0];
        public MyEdge edges[] = new MyEdge[0];
        public int edgeNum = 0;

        /**
         * default
         */
        public MyGraph(){}

        /**
         * generate a graph with certain vertices and random edges
         * @param verticeNum vertice numbers
         */
        public MyGraph(int verticeNum){
            // set vertices and edges
            vertices = new MyVertice[verticeNum];
            edges = new MyEdge[verticeNum];
            for (int i = 0; i < verticeNum; i++){
                vertices[i] = new MyVertice();
                vertices[i].index = i;
                edges[i] = new MyEdge();
                edges[i].origin = vertices[i];
            }
            randomInit();
        }

        /**
         * generate a random graph
         */
        private void randomInit() {
            Random rand = new Random();
            int count = 0;
            int maxEdgeNum = vertices.length * (vertices.length - 1);
            while (count <= maxEdgeNum){
                if ((count > MINEDGENUM) && (rand.nextDouble() > RANDOMFACTOR)) break;// when edge count more than require, I set a possibility to end up generation
                int start = rand.nextInt(vertices.length);
                int end = rand.nextInt(vertices.length);
                if (start == end) continue;
                MyEdge edge = edges[start];
                while ((edge.next != null) && (edge.next.origin.index != end)){
                    edge = edge.next;
                }
                if (edge.next == null){
                    MyEdge newEdge = new MyEdge();
                    newEdge.origin = vertices[end];
                    newEdge.weight = rand.nextInt(RANDOMRANGE) + 1;
                    edge.next = newEdge;
                    count++;
                }
                edgeNum = count;
            }
        }

        /**
         * print vertices and edges
         */
        public void print(){
            System.out.printf("Vertices(%d):\n[", vertices.length);
            for (int i = 0; i < vertices.length; i++){
                System.out.printf("%d", vertices[i].index);
                if (i == vertices.length - 1) continue;
                System.out.print(", ");
            }
            System.out.printf("]\nEdges(%d):\n", edgeNum);
            for (int i = 0; i < edges.length; i++){
                MyEdge tmp = edges[i];
                while (tmp.next != null){
                    System.out.printf("%d -(%d)-> ", tmp.origin.index, tmp.next.weight);
                    tmp = tmp.next;
                }
                System.out.printf("%d -> null\n", tmp.origin.index);
            }
        }
    }

    public static void main(String[] args) {
        int verticeNum = 16;// number of vertices
        if ((verticeNum < MyGraph.MINVERTICENUM) || (verticeNum <= 0)){
            System.out.printf("Unvalid vertice number!");
            return;
        }
        MyGraph original = new MyGraph(verticeNum);
        System.out.printf("\nOriginal graph:\n");
        original.print();
        MyGraph shortestPath = dijkstraAlog(original, 0);
        System.out.printf("\nShortest path:\n");
        shortestPath.print();
    }

    private static MyGraph dijkstraAlog(MyGraph originalGraph, int startIndex) {
        // init shortestP
        MyGraph shortestP = new MyGraph();
        MyVertice[] spvs = shortestP.vertices;
        MyVertice[] ogvs = originalGraph.vertices;
        MyEdge[] spes = shortestP.edges;
        MyEdge[] oges = originalGraph.edges;
        // queue
        PriorityQueue<MyVertice> queue = new PriorityQueue<MyVertice>(ogvs.length, new Comparator<MyVertice>(){// java's heap
            public int compare(MyVertice v1, MyVertice v2){
                return v1.key - v2.key;
            }// minimun heap
        });
        // shortest path vertices init
        spvs = new MyVertice[ogvs.length];
        for (int i = 0; i < ogvs.length; i++){
            MyVertice tmpVert = new MyVertice();
            tmpVert.index = ogvs[i].index;
            tmpVert.key = ogvs[i].key;
            spvs[i] = tmpVert;
            queue.add(tmpVert);
        }
        // shortest path edges init
        spes = new MyEdge[ogvs.length];
        for (int i = 0; i < ogvs.length; i++){
            spes[i] = new MyEdge();
            spes[i].origin = spvs[i];
        }
        // Dijkstra's Algorithm
        MyVertice[] cloud = new MyVertice[spvs.length];
        int cloudCount = 0;
        spvs[startIndex].key = 0;
        cloud[cloudCount] = queue.poll();
        cloudCount++;
        //System.out.printf("New start: %d", startIndex);
        MyEdge edge = oges[startIndex].next;
        while (edge != null){
            if (spvs[edge.origin.index].key <= edge.weight) continue;;
            spvs[edge.origin.index].key = edge.weight;// set distance
            spvs[edge.origin.index].parent = spvs[startIndex];// set parent
            edge = edge.next;
        }
        while (queue.size() > 0){
            MyVertice vertice = queue.poll();
            cloud[cloudCount] = vertice;
            cloudCount++;
            if (vertice.key != Integer.MAX_VALUE){// infinite -> new startpoint
                MyEdge tmpEdge = spes[vertice.parent.index];
                while (tmpEdge.next != null){
                    tmpEdge = tmpEdge.next;
                }
                MyEdge newEdge = new MyEdge();
                newEdge.origin = spvs[vertice.index];
                newEdge.weight = vertice.key;
                tmpEdge.next = newEdge;
                shortestP.edgeNum++;
            }
            //else{
                //System.out.printf(" %d", vertice.index);
            //}
            edge = oges[vertice.index].next;
            while (edge != null){
                if (spvs[edge.origin.index].key >= edge.weight){
                    spvs[edge.origin.index].key = edge.weight;// set distance
                    spvs[edge.origin.index].parent = spvs[vertice.index];// set parent
                }
                edge = edge.next;
            }
        }
        shortestP.vertices = spvs;
        shortestP.edges = spes;
        return shortestP;
    }
}
