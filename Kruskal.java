import java.util.*;

public class Kruskal {
    LinkedList<Tuple> A;
    LinkedHashSet<Edge> mst;
    Double cost;

    public Kruskal(EdgeWeightedGraph G) {
        PriorityQueue<Tuple> Q = new PriorityQueue<Tuple>(G.E(), new Comparator<Tuple>() {
            public int compare(Tuple t1, Tuple t2) {
                return Double.compare(t1.x, t2.x);
            }
        });
          
        DisjointStructures ds = new DisjointStructures(G.V());
        A = new LinkedList<Tuple>();
        Double weight;
        cost = 0.0;
        Tuple t;
        int u;
        int v;

        for (Edge e : G.edges())
            Q.add(new Tuple(e.weight(), e.either(), e.other(e.either())));
        
        while (!Q.isEmpty() && A.size() < G.V() -1) {
            t = Q.poll();
            weight = t.x;
            u = t.y;
            v = t.z;
            
            if (!ds.connected(u, v)) {
                A.add(new Tuple(weight, u, v));
                ds.union(u, v);
                cost = cost + weight;
            }
        }
    }
    
    public LinkedHashSet<Edge> getEdgesMST() {
        mst = new LinkedHashSet<Edge>();
        
        for (Tuple t : A) {
            mst.add(new Edge(t.y, t.z, t.x));
        }
        
	    return mst;
    }

    public double weight() {
	  return cost;
    }
    
    
    public static void main(String[] args) {
          In in = new In(args[0]);
          EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        Kruskal K = new Kruskal(G);
        LinkedHashSet<Edge> mst = new LinkedHashSet<Edge>();
        mst = K.getEdgesMST();
        
        for (Edge e : G.edges()) {
            System.out.println("u " + e.either());
            System.out.println("v " + e.other(e.either()));
            System.out.println("weigth " + e.weight());
        }
    }
}