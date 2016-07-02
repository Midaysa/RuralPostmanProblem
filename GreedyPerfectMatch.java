import java.util.*;
 
public class GreedyPerfectMatch {

    public HashSet<Edge> PerfectMatching(EdgeWeightedGraph G) {
        HashSet<Edge> M = new HashSet<Edge>(G.E());
        ArrayList<Edge> L = new ArrayList<Edge>();
        HashSet<Integer> V = new HashSet<Integer>(G.V());
        Edge e;
        int u, v;
        
        for (Edge edge : G.edges()) 
            L.add(edge);
            
        for (int i=0; i<G.V(); i++)
            V.add(i);
            
        Collections.sort(L, new Comparator<Edge>() {
            public int compare(Edge e1, Edge e2) {
                return Double.compare(e1.weight(), e2.weight());
            }
        });
        
        System.out.println("L = " + L);
        System.out.println("V = " + V);
        
        while (!V.isEmpty() && !L.isEmpty()) {
            // System.out.println("One iteration");
            e = L.get(L.size() - 1);
            L.remove(L.size() - 1);
            u = e.either();
            v = e.other(u);
            
            if (V.contains(u) && V.contains(v)) {
                M.add(e);
                V.remove(u);
                V.remove(v);
            }
        }
        return M;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        
        GreedyPerfectMatch gpm = new GreedyPerfectMatch();
        System.out.println(gpm.PerfectMatching(G));
    }
}