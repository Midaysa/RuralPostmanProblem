import java.util.*;
 
public class VertexScanPerfectMatch {
    
    public HashSet<Edge> PerfectMatching(EdgeWeightedGraph G) {
        HashSet<Edge> M = new HashSet<Edge>();
        ArrayList<Edge> E = new ArrayList<Edge>();
        HashSet<Integer> V = new HashSet<Integer>();
        Edge edge = null;
        int i=-1, i1, i2, j1, j2;
        
        for (Edge e : G.edges())
            E.add(e);
            
        for (int v=0; v<G.V(); v++)
            V.add(v);
        
        Collections.sort(E, new Comparator<Edge>() {
            public int compare(Edge e1, Edge e2) {
                return Double.compare(e1.weight(), e2.weight());
            }
        });
        
        while (!V.isEmpty()) {
            i = V.iterator().next();
            
            for (Edge e : E) {
                if (e.either() == i || e.other(e.either()) == i) {
                    // E is sorted by cost, so the first edge (i,j) we find is
                    // the one which costs less of all (i,j) edges
                    edge = e;
                    break;
                }
            }
            
            M.add(edge);
            i1 = edge.either();
            j1 = edge.other(i1);
            V.remove(i1);
            V.remove(j1);
            
            Iterator<Edge> it = E.iterator();
            while (it.hasNext()) {
                Edge e = it.next();
                i2 = e.either();
                j2 = e.other(i2);
                if (i1 == i2 || i1 == j2 || j1 == i2 || j1 == j2)
                    it.remove();
            }
        }
        return M;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        
        VertexScanPerfectMatch vspm = new VertexScanPerfectMatch();
        System.out.println(vspm.PerfectMatching(G));
    }
}