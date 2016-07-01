import java.util.*;
import java.lang.Math.*;

public class SolverRPP {
    private EdgeWeightedGraph G;
    private HashSet<Edge> R;
    private String algorithm;


    public SolverRPP(In in, String used_algorithm) {
        int a, u, v, V, requiredE, notRequiredE, useless_int;
        double weigth;
        char useless_char;
        String useless_string, str;
        
        algorithm = used_algorithm;  // -g or -s
        useless_string = in.readLine();
        //System.out.println("useless: " + useless_string);
        useless_string = in.readLine();
        //System.out.println("useless: " + useless_string);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        V = in.readInt();
        //System.out.println("V = " + V);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        requiredE = in.readInt();
        //System.out.println("requiredE " + requiredE);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        notRequiredE = in.readInt();
        //System.out.println("notRequiredE" + notRequiredE);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        
        G = new EdgeWeightedGraph(V+1);
        R = new HashSet<Edge>();

        for (int i = 0; i < requiredE; i++) {
            useless_string = in.readString();
            //System.out.println("useless_string " + useless_string);
            
            str = in.readString();  // number with a comma at the end
            u = Integer.parseInt(str.substring(0, str.length() - 1));
            //System.out.println(u);
            
            str = in.readString();  // number with a comma at the end
            v = Integer.parseInt(str.substring(0, str.length() - 1));
            //System.out.println(v);
            
            useless_string = in.readString();
            //System.out.println("useless_string " + useless_string);
            
            weigth = in.readInt();
            //System.out.println("weigth " + weigth);
            useless_int = in.readInt();
            //System.out.println("useless_int " + useless_int);
            
            Edge e = new Edge(u, v, weigth);
            G.addEdge(e);
            R.add(e);
        }
        
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);
        useless_string = in.readString();
        //System.out.println("useless: " + useless_string);

        for (int i = 0; i < requiredE; i++) {
            useless_string = in.readString();
            //System.out.println("useless_string " + useless_string);
            
            str = in.readString();  // number with a comma at the end
            u = Integer.parseInt(str.substring(0, str.length() - 1));
            //System.out.println(u);
            
            str = in.readString();  // number with a comma at the end
            v = Integer.parseInt(str.substring(0, str.length() - 1));
            //System.out.println(v);
            
            useless_string = in.readString();
            //System.out.println("useless_string " + useless_string);
            
            weigth = in.readInt();
            //System.out.println("weigth " + weigth);
            useless_int = in.readInt();
            //System.out.println("useless_int " + useless_int);
            
            Edge e = new Edge(u, v, weigth);
            G.addEdge(e);
        }
    }


    public ArrayList<Integer> GetSolution() {
        EdgeWeightedGraph Gr = new EdgeWeightedGraph(this.G.V());
        EulerianCycle EC;
        
        for (Edge edge : this.R)
            Gr.addEdge(edge);
        
        Floyd CCM;
        
        EC = new EulerianCycle();

        if (EC.isConnected(Gr)) {
            if (EC.isPair(Gr)) {
                EC = new EulerianCycle(G);
                return getEulerianCycle(EC);       // lines 23-24
            }
            else {
                CCM = new Floyd(G);
                applyPerfectMatching(Gr, CCM);     // lines 16-22
                EC = new EulerianCycle(Gr);
                return getEulerianCycle(EC);       // lines 23-24
            }
        }
        else {
            CCM = new Floyd(G);
            System.out.println("marico el que lo lea :3");
            applyConnectionMethod(Gr, G, CCM);     // lines 9-15
            applyPerfectMatching(Gr, CCM);         // lines 16-22
            EC = new EulerianCycle(Gr);
            return getEulerianCycle(EC);           // lines 23-24
        }
    }
    

    private boolean contains(Iterable<Edge> edges_iterable, int node) {
        // returns whether node is inside edges_iterable or not
        
        for (Edge edge : edges_iterable) {
            if (edge.either() == node || edge.other(edge.either()) == node)
                return true;
        }
        return false;
    }


    public void applyConnectionMethod(EdgeWeightedGraph Gr, EdgeWeightedGraph G, Floyd CCM) {
        ConnectedComponents CC = new ConnectedComponents();
        ArrayList<ArrayList<Integer>> CC_list = CC.findComponents(Gr);
        EdgeWeightedGraph Gt = new EdgeWeightedGraph(Gr.V());
        ArrayList<Double> CCM_dist_list;
        int node_u, node_v;
        
        System.out.println(CCM.dist(297, 357));
        
        // build complete graph Gt using Gr's connected components as big nodes 
        // and costs taken from G's paths
        for (int i=0; i<CC_list.size(); i++) { // Connected Component i
            for (int j=0; j<CC_list.size(); j++) { // Connected Component j
            
                if (i != j) { // make sure CC[i] and CC[j] are different Connected Components
                    CCM_dist_list = new ArrayList<Double>();
                    
                    for (int u=0; u<CC_list.get(i).size(); u++) { // nodes u from CC[i]
                        for (int v=0; v<CC_list.get(j).size(); v++) { // nodes v from CC[j]
                            node_u = CC_list.get(i).get(u);
                            node_v = CC_list.get(j).get(v);
                            
                            if (contains(G.adj(node_u), node_v) || 
                                contains(G.adj(node_v), node_u))
                                // if there's a path between CC[i] and CC[j]
                                // using nodes u and v in the original graph G,
                                // then add the CCM.dist(u, v) to CCM_dist_list
                                CCM_dist_list.add(CCM.dist(u, v));
                        }
                    }
                    // add an edge from CC[i] to CC[j] with a cost of
                    // min(CCM_dist_list) to the new graph Gt
                   
                    Gt.addEdge(new Edge(i, j, Collections.min(CCM_dist_list)));
                }
            }
        }
        
        // get MST from Gt
        Kruskal kruskal = new Kruskal(Gt);
        LinkedHashSet<Edge> mst = kruskal.getEdgesMST();
        
        // remove duplicates from mst (is this neccessary?)
        // -DELETED-
        
        // add MST edges to Gr
        for (Edge edge : mst)
            Gr.addEdge(edge);
    }


    public void applyPerfectMatching (EdgeWeightedGraph Gr, Floyd CCM ) {
        HashSet<Integer> Vo = new HashSet<Integer>();
        Floyd F = new Floyd(Gr);
        
        for(int i =0 ; i<Gr.V(); i++) {
            if (Gr.degree(i)%2 != 0) {
                Vo.add(i);
            }
        }
        
        EdgeWeightedGraph Go = new EdgeWeightedGraph(Gr.V());
        
        for (int i = 0; i < Vo.size(); i++) {
            for (int j = 0; j < Vo.size(); j++) {
                Edge e = new Edge(i, j, CCM.dist(i, j));
                Go.addEdge(e);
            }
        }
        
        if (this.algorithm.equals("g")) {
            HashSet<Edge> MG = new HashSet<Edge>();
            GreedyPerfectMatch gpm = new GreedyPerfectMatch();
            MG = gpm.PerfectMatching(Go);
            
            for (Edge e : MG) {
                for (Edge s: Go.edges()) {
                    Gr.addEdge(s);
                }
            }
        }
        else if (this.algorithm.equals("s")) {
            HashSet<Edge> MV = new HashSet<Edge>();
            VertexScanPerfectMatch vspm = new VertexScanPerfectMatch();
            MV = vspm.PerfectMatching(Go);
            
            for (Edge e : MV) {
                for (Edge s: Go.edges()) {
                    Gr.addEdge(s);
                }
            }
        }
    }


    public ArrayList<Integer> getEulerianCycle(EulerianCycle EC) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        
        for (Integer i : EC.cycle())
            array.add(i);
            
        return array;
    }
    
    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: \nSolverRPP [-g] [-s] <problem instance>");
            System.exit(1);
        }
        
        In in = new In(args[1]);
        long initial_time = System.nanoTime();
        SolverRPP rpp = new SolverRPP(in, args[0]);
        System.out.println(rpp.GetSolution());
        System.out.println(System.nanoTime() - initial_time);
    }
}