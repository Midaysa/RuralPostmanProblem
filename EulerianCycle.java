/**
 *  The <tt>EulerianCycle</tt> class represents a data type
 *  for finding an Eulerian cycle or path in a graph.
 *  An <em>Eulerian cycle</em> is a cycle (not necessarily simple) that
 *  uses every edge in the graph exactly once.
 *  <p>
 *  This implementation uses a nonrecursive depth-first search.
 *  The constructor runs in O(<Em>E</em> + <em>V</em>) time,
 *  and uses O(<em>E</em> + <em>V</em>) extra space, where <em>E</em> is the
 *  number of edges and <em>V</em> the number of vertices
 *  All other methods take O(1) time.
 *  <p>
 *  To compute Eulerian paths in graphs, see {@link EulerianPath}.
 *  To compute Eulerian cycles and paths in digraphs, see
 *  {@link DirectedEulerianCycle} and {@link DirectedEulerianPath}.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/41graph">Section 4.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * 
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Nate Liu
 */

import java.util.ArrayList;

public class EulerianCycle {
    public Stack<Integer> cycle = new Stack<Integer>();  // Eulerian cycle; null if no such cycle
    
    // empty constructor to allow using public functions on other graphs without
    // applying all the cycle calculations
    public EulerianCycle() {}
    

    /**
     * Computes an Eulerian cycle in the specified graph, if one exists.
     * 
     * @param G the graph
     */
    public EulerianCycle(EdgeWeightedGraph G) {

        // must have at least one edge
        if (G.E() == 0) return;

        // necessary condition: all vertices have even degree
        // (this test is needed or it might find an Eulerian path instead of cycle)
        for (int v = 0; v < G.V(); v++) 
            if (G.degree(v) % 2 != 0)
                return;

        // create local view of adjacency lists, to iterate one vertex at a time
        // the helper Edge data type is used to avoid exploring both copies of an edge v-w
        Queue<Edge>[] adj = (Queue<Edge>[]) new Queue[G.V()];
        for (int v = 0; v < G.V(); v++)
            adj[v] = new Queue<Edge>();

        for (int v = 0; v < G.V(); v++) {
            int selfLoops = 0;
            int w;
            for (Edge edge : G.adj(v)) {
                w = edge.other(v);
                // careful with self loops
                if (v == w) {
                    if (selfLoops % 2 == 0) {
                        Edge e = new Edge(v, w, 0.0);
                        adj[v].enqueue(e);
                        adj[w].enqueue(e);
                    }
                    selfLoops++;
                }
                else if (v < w) {
                    Edge e = new Edge(v, w, 0.0);
                    adj[v].enqueue(e);
                    adj[w].enqueue(e);
                }
            }
        }

        // initialize stack with any non-isolated vertex
        int s = nonIsolatedVertex(G);
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(s);

        // greedily search through edges in iterative DFS style
        cycle = new Stack<Integer>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            while (!adj[v].isEmpty()) {
                Edge edge = adj[v].dequeue();
                if (edge.isUsed) continue;
                edge.isUsed = true;
                stack.push(v);
                v = edge.other(v);
            }
            // push vertex with no more leaving edges to cycle
            cycle.push(v);
        }

        // check if all edges are used
        if (cycle.size() != G.E() + 1)
            cycle = null;

        assert certifySolution(G);
    }


    /**
     * Returns the sequence of vertices on an Eulerian cycle.
     * 
     * @return the sequence of vertices on an Eulerian cycle;
     *         <tt>null</tt> if no such cycle
     */
     public Iterable<Integer> cycle() {
        return cycle;
     }
    //  public ArrayList<Integer> cycle() {
    //     ArrayList<Integer> road = new ArrayList<Integer>();
        
    //      for(Integer i: cycle){
    //          road.add(cycle.pop());
    //      }
         
    //      return road;
    //  }
    /**
     * Returns true if the graph has an Eulerian cycle.
     * 
     * @return <tt>true</tt> if the graph has an Eulerian cycle;
     *         <tt>false</tt> otherwise
     */
    public boolean hasEulerianCycle() {
        return cycle != null;
    }


    // returns any non-isolated vertex; -1 if no such vertex
    private static int nonIsolatedVertex(EdgeWeightedGraph G) {
        for (int v = 0; v < G.V(); v++)
            if (G.degree(v) > 0)
                return v;
        return -1;
    }

    /**************************************************************************
     *
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

    // Determines whether a graph has an Eulerian cycle using necessary
    // and sufficient conditions (without computing the cycle itself):
    //    - at least one edge
    //    - degree(v) is even for every vertex v
    //    - the graph is connected (ignoring isolated vertices)
    private static boolean hasEulerianCycle(EdgeWeightedGraph G) {

        // Condition 0: at least 1 edge
        if (G.E() == 0) return false;

        // Condition 1: degree(v) is even for every vertex
        for (int v = 0; v < G.V(); v++) {
            if (G.degree(v) % 2 != 0)
                return false;
        }

        // Condition 2: graph is connected, ignoring isolated vertices
        int s = nonIsolatedVertex(G);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);
        for (int v = 0; v < G.V(); v++)
            if (G.degree(v) > 0 && !bfs.hasPathTo(v))
                return false;

        return true;
    }
    
    
    /**
     * Verifica si el grafo es conexo..
     * 
     * @param   G   grafo
     * @return <tt>true</tt>  Si el grafo es conexo
     *         <tt>false</tt> En caso contrario
     */
    public boolean isConnected(EdgeWeightedGraph G){
        
        int s = nonIsolatedVertex(G);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);
        for (int v = 0; v < G.V(); v++)
            if (G.degree(v) > 0 && !bfs.hasPathTo(v))
                return false;

        return true;
    }
    
    
        
    /**
     * Verifica si el grafo es par
     * 
     * @param   G   grafo
     * @return <tt>true</tt>  Si el grafo es par
     *         <tt>false</tt> En caso contrario
     */
    public boolean isPair(EdgeWeightedGraph G){
        for (int v = 0; v < G.V(); v++) 
            if (G.degree(v) % 2 != 0)
                return false;
        return true;
    }


    private boolean certifySolution(EdgeWeightedGraph G) {

        // internal consistency check
        if (hasEulerianCycle() == (cycle() == null)) return false;

        // hashEulerianCycle() returns correct value
        if (hasEulerianCycle() != hasEulerianCycle(G)) return false;

        // nothing else to check if no Eulerian cycle
        if (cycle == null) return true;

        // check that cycle() uses correct number of edges
        if (cycle.size() != G.E() + 1) return false;

        // check that cycle() is a cycle of G
        // TODO

        // check that first and last vertices in cycle() are the same
        int first = -1, last = -1;
        for (int v : cycle()) {
            if (first == -1) first = v;
            last = v;
        }
        if (first != last) return false;

        return true;
    }

    /**
     * Unit tests the <tt>EulerianCycle</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        EulerianCycle euler = new EulerianCycle(G);
        
        //euler.isConnected(G);
        //StdOut.println(euler.isConnected(G));
        //ArrayList<Integer> bla = new ArrayList<Integer>();
        //bla = euler.cycle();
        
        //for (Integer e : bla)
        //    System.out.println(" " + e);
            
        if (euler.hasEulerianCycle(G)) {
            
            //for (int i = 0; i < G.V(); i++)
            //    StdOut.print(G.degree(i) + " ");
            
            for (int v : euler.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
        else {
            StdOut.println("none");
        }
    }
}