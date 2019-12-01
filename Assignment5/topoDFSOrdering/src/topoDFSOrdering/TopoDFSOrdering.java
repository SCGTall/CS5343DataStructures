/**
 * FileName: TopoDFSOrdering.java
 * Introduction:
 * Implement the DFS topological ordering.
 * Your directed graph must have at least 10 vertices and 15 edges.
 * You must run the algorithm on two sets of graphs.
 * 1. The graph does not have a cycle.  It generates a correct topological ordering.
 * 2. The graph has a cycle. It attempts to generate an order. But discovers the cycle and exits (some nodes will already be ordered.
 *
 * Submit the code.
 * Submit screen shots of each execution.
 * You can print each of the graphs as adjacency matrix or other representation.
 *
 * Thought:
 * 1. I choose adjacency matrix to represent graph. Use lexicographical order to choose in multiple choices.
 * 2. I will add one random edge once a time. Topological DFS sort and print when edge number is larger than 15. Terminate when get a circle.
 * 3. Print matrix and topological DPS ordering.
 *
 * @author Chaoran Li
 * @Date 11/15/2019
 * @version 1.0
 */

package topoDFSOrdering;

import java.util.Random;

public class TopoDFSOrdering {
    /**
     * class MyVertice for MyGraph.
     */
    private static class MyVertice{
        public int index;
        public int inDegree;
        public boolean visited;
        public boolean done;
        public MyVertice(int inIndex){
            index = inIndex;
            inDegree = 0;
            visited = false;
            done = false;
        }
    }

    /**
     * use a adjacency matrix to represent graph. Aij = 1 iff there is an edge from vi to vj.
     */
    private static class MyGraph{
        public static final int MINVERTICENUM = 10;
        public static final int MINEDGENUM = 15;
        public MyVertice[] vertices;
        public int[][] matrix;
        public int edgeNum;
        private int verticeNum;
        private Random rand;
        /**
         * init a directed graph with num vertices
         * @param n number of vertices
         */
        public MyGraph(int n){
            verticeNum = n;
            if (verticeNum < MINVERTICENUM) return;
            vertices = new MyVertice[verticeNum];
            matrix = new int[verticeNum][verticeNum];
            for (int i = 0; i < verticeNum; i++){
                vertices[i] = new MyVertice(i);
                matrix[i] = new int[verticeNum];// in java, integers initialized to 0
            }
            edgeNum = 0;
            rand = new Random();
        }

        /**
         * add an random edge
         */
        public void randomAddEdge(){
            int i = rand.nextInt(verticeNum);
            int j = rand.nextInt(verticeNum);
            while (0 != matrix[i][j]){
                i = rand.nextInt(verticeNum);
                j = rand.nextInt(verticeNum);
            }
            matrix[i][j] = 1;
            edgeNum++;
            vertices[j].inDegree++;
        }

        /**
         * print graph as adjacency matrix
         */
        public void print(){
            System.out.print("\nAdjacency Matrix:\n[");
            for (int i = 0; i < verticeNum; i++){
                System.out.print("[");
                for (int j = 0; j < verticeNum; j++){
                    System.out.print(matrix[i][j]);
                    if (1 >= verticeNum - j) continue;;
                    System.out.print(", ");
                }
                System.out.print("]");
                if (1 >= verticeNum - i) continue;
                System.out.print(",\n");
            }
            System.out.print("]\n");
            /*
            System.out.print("\nIndegree:\n");
            for (int i = 0; i < verticeNum; i++){
                System.out.printf("%d\t", vertices[i].index);
            }
            System.out.print("\n");
            for (int i = 0; i < verticeNum; i++){
                System.out.printf("%d\t", vertices[i].inDegree);
            }
            System.out.print("\n");
             */
        }
    }

    public static void main(String[] args) {
        // init
        int verticeNum = 15;
        MyGraph g = new MyGraph(verticeNum);
        if (null == g.vertices){
            System.out.printf("Your directed graph have (%d/10) vertices!", verticeNum);
            return;
        }
        while (g.edgeNum < g.verticeNum * g.verticeNum){// break condition
            g.randomAddEdge();
            if (g.edgeNum < g.MINEDGENUM) continue;// edge less than 15
            g.print();
            if (topologicalDFS(g)) break;// break if there is a circle in the graph
        }
    }

    /**
     * topological DFS ordering
     * @param g graph
     * @return have circle
     */
    private static boolean topologicalDFS(MyGraph g) {
        // init visited and done
        for (int i = 0; i < g.verticeNum; i++){
            g.vertices[i].visited = false;
            g.vertices[i].done = false;
        }
        MyVertice v = getStartVertice(g);
        System.out.print("Ordering:");
        boolean flag = false;
        while (null != v){
            flag = topologicalDFS(g, v);
            if (flag){
                System.out.print("\nThe graph has a cycle!\n");
                return flag;
            }
            v = getStartVertice(g);
        }
        for (int i = 0; i < g.verticeNum; i++){
            if (g.vertices[i].visited) continue;
            System.out.print("\nThe graph has a cycle!\n");
            return true;
        }
        return flag;
    }

    /**
     * recursion function for topological DFS ordering
     * @param g graph
     * @param v vertice
     * @return return have circle
     */
    private static boolean topologicalDFS(MyGraph g, MyVertice v) {
        boolean flag = false;
        v.visited = true;
        for (int i = 0; i < g.verticeNum; i++){// DFS
            if (0 == g.matrix[v.index][i]) continue;// 1 if connected
            if (g.vertices[i].visited) {
                if (!g.vertices[i].done){// visited = T, done = F
                    return true;
                }
            }
            else{
                flag = topologicalDFS(g, g.vertices[i]);
            }
        }
        v.done = true;
        System.out.printf("->%d", v.index);
        return flag;
    }

    /**
     * the indegree of start vertice is 0. Return null if no zero-indegree vertice is found.
     * @param g graph
     * @return start vertice
     */
    private static MyVertice getStartVertice(MyGraph g) {
        //System.out.print("\nFIndStart:\n");
        for (int i = 0; i < g.verticeNum; i++){
            //System.out.printf("%d  ", g.vertices[i].index);
            if (g.vertices[i].inDegree != 0) continue;// indegree should be 0
            if (g.vertices[i].visited) continue;// should not visited
            return g.vertices[i];
        }
        return null;
    }

}
