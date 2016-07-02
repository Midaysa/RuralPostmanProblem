

import java.util.*;
import java.lang.Math.*;

public class SolverRPP {
    private EdgeWeightedGraph G;
    private HashSet<Edge> R;
    private String algorithm;


    public SolverRPP(In in, String used_algorithm) {
        int a, u, v, V, requiredE, notRequiredE, uselessInt;
        double weigth;
        char useless_char;
        String uselessString, str;
        
        algorithm = used_algorithm;  // -g or -s
        uselessString = in.readLine();
        uselessString = in.readLine();
        uselessString = in.readString();
        uselessString = in.readString();
        V = in.readInt();
        uselessString = in.readString();
        uselessString = in.readString();
        requiredE = in.readInt();
        uselessString = in.readString();
        uselessString = in.readString();
        notRequiredE = in.readInt();
        uselessString = in.readString();
        uselessString = in.readString();
        
        G = new EdgeWeightedGraph(V);
        R = new HashSet<Edge>();

        for (int i = 0; i < requiredE; i++) {
            uselessString = in.readString();

            str = in.readString();  // number with a comma at the end
            u = Integer.parseInt(str.substring(0, str.length() - 1));
            
            str = in.readString();  // number with a comma at the end
            v = Integer.parseInt(str.substring(0, str.length() - 1));
            
            uselessString = in.readString();
            weigth = in.readInt();
            uselessInt = in.readInt();
            
            Edge e = new Edge(u-1, v-1, weigth);
            G.addEdge(e);
            R.add(e);
        }
        
        uselessString = in.readString();
        uselessString = in.readString();

        for (int i = 0; i < notRequiredE; i++) {
            uselessString = in.readString();

            str = in.readString();  // number with a comma at the end
            u = Integer.parseInt(str.substring(0, str.length() - 1));
            
            str = in.readString();  // number with a comma at the end
            v = Integer.parseInt(str.substring(0, str.length() - 1));
            
            uselessString = in.readString();
            weigth = in.readInt();
            uselessInt = in.readInt();
            
            Edge e = new Edge(u-1, v-1, weigth);
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
            System.out.println("Case 1");
            if (EC.isPair(Gr)) {
                System.out.println("Case 1.1");
                EC = new EulerianCycle(G);
                System.out.println(EC.cycle());
                return getEulerianCycle(EC);       // lines 23-24
            }
            else {
                System.out.println("Case 1.2");
                CCM = new Floyd(G);
                applyPerfectMatching(Gr, CCM);     // lines 16-22
                EC = new EulerianCycle(Gr);
                
                for (Integer e : EC.cycle()) {
                    System.out.print(" " + e);              
                    System.out.println("Eurelian Cycle  "); 
                }
                    
                return getEulerianCycle(EC);       // lines 23-24
            }
        }
        else {
            System.out.println("Case 2");
            CCM = new Floyd(G);
            
            /*System.out.println("Edges in G:");
            for (Edge e : G.edges())
                System.out.println("  " + e);
            
            System.out.println("Edges in old Gr:");
            for (Edge e : Gr.edges())
                System.out.println("  " + e);*/
                
            applyConnectionMethod(Gr, G, CCM);     // lines 9-15
            
             System.out.println("Edges in new Gr:");
            for (Edge e : Gr.edges())
                System.out.println("  " + e);
            
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
        ArrayList<Edge> CCM_dist_list;
        int node_u, node_v;
        
        // System.out.println("CC_list = " + CC_list);
        
        // build complete graph Gt using Gr's connected components as big nodes 
        // and costs taken from G's paths
        for (int i=0; i<CC_list.size(); i++) { // Connected Component i
            for (int j=i+1; j<CC_list.size(); j++) { // Connected Component j
                CCM_dist_list = new ArrayList<Edge>();
                /*System.out.println("CC["+i+"] = " + CC_list.get(i) +
                                   " | CC["+j+"] = "  + CC_list.get(j));*/
                
                for (int u=0; u<CC_list.get(i).size(); u++) { // nodes u from CC[i]
                    for (int v=0; v<CC_list.get(j).size(); v++) { // nodes v from CC[j]
                        node_u = CC_list.get(i).get(u);
                        node_v = CC_list.get(j).get(v);
                        
                        if (contains(G.adj(node_u), node_v) || 
                            contains(G.adj(node_v), node_u)) {
                            // if there's a path between CC[i] and CC[j]
                            // using nodes u and v in the original graph G,
                            // then add the CCM.dist(u, v) to CCM_dist_list
                            /*System.out.println("node_u = " + node_u + 
                                               " | node_v = " + node_v +
                                               " | CCM = " + CCM.dist(node_u, node_v));*/
                            CCM_dist_list.add(
                                new Edge(node_u, node_v, CCM.dist(node_u, node_v)));
                        }
                    }
                }
                // add an edge from CC[i] to CC[j] with a cost of
                // min(CCM_dist_list) to the new graph Gt
                
                if (CCM_dist_list.size() == 0)
                    continue;
                
                /*System.out.println("    CCM_dist_list = " + CCM_dist_list + 
                                   " | min = " + Collections.min(CCM_dist_list));*/
                Gt.addEdge(Collections.min(CCM_dist_list));
            }
        }
        
        // get MST from Gt
        // System.out.println("Edges in Gt = " + Gt.E());
        Kruskal kruskal = new Kruskal(Gt);
        LinkedHashSet<Edge> mst = kruskal.getEdgesMST();
        
        // remove duplicates from mst (is this neccessary?)
        // -DELETED-
        
        // add MST edges to Gr
        /*System.out.println("Edges in MST:");
        for (Edge edge : mst) {
            Gr.addEdge(edge);
            System.out.println("  " + edge);
        }*/
    }


    public void applyPerfectMatching (EdgeWeightedGraph Gr, Floyd CCM ) {
        ArrayList<Integer> Vo = new ArrayList<Integer>();
        
        for(int i =0 ; i<Gr.V(); i++) {
            if (Gr.degree(i)%2 != 0) {
                Vo.add(i);
            }
        }
        
        System.out.println("V0 = " + Vo);
        
        EdgeWeightedGraph Go = new EdgeWeightedGraph(Gr.V());
        
        for (int i=0; i<Vo.size(); i++) {
            for (int j=i+1; j<Vo.size(); j++) {
                int u = Vo.get(i);
                int v = Vo.get(j);
                Go.addEdge(new Edge(u, v, CCM.dist(u, v)));
            }
        }
        
        System.out.println("Edges in Go:");
        for (Edge e : Go.edges())
            System.out.println("  " + e);
        
        System.out.println(this.algorithm);
        
        if (this.algorithm.equals("-g")) {
            HashSet<Edge> MG = new HashSet<Edge>();
            GreedyPerfectMatch gpm = new GreedyPerfectMatch();
            MG = gpm.PerfectMatching(Go);
            
            for (Edge e : MG) {
                for (Edge s: Go.edges()) {
                    Gr.addEdge(s);
                }
            }
            
            System.out.println("MG = " + MG);
        }
        else if (this.algorithm.equals("-s")) {
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
        System.out.println("Estoy en getEulerianCycle");

        
        for (Integer i : EC.cycle()) {
            System.out.println("elemento i " + i);
            array.add(i);
            System.out.println("Arra " + array.get(i));
        }
        
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
        System.out.println("Lista " + rpp.GetSolution());
    
        long elapsedTime = System.nanoTime() - initial_time;
        double  seconds = (double)elapsedTime/1000000000.0;
        System.out.println(seconds);
        //System.out.println(System.nanoTime() - initial_time);
        
    }
}