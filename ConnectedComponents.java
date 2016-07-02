import java.io.*;
import java.util.*;
import java.util.LinkedList;
 
 
public class ConnectedComponents {
    
    public ArrayList<ArrayList<Integer>> findComponents(EdgeWeightedGraph G) {
        ArrayList<ArrayList<Integer>> finalComponents = new ArrayList<ArrayList<Integer>>();
        Integer[] components = new Integer[G.V()];
        ArrayList<Integer> subArray;
        int ccNumber = 0;
        
        for (int i=0; i<G.V(); i++)
            components[i] = -1;
        
        for (int i = 0; i < G.V(); i++) {
            if (components[i] == -1) {
                dfs(G, i, ccNumber, components);
                subArray = new ArrayList<Integer>();
                
                for (int j = 0; j < G.V(); j++) {
                    //System.out.println(components[j]);
                    if (components[j] == ccNumber)
                        subArray.add(j);
                }
                
                finalComponents.add(subArray);
                ccNumber++;
            } 
        }
        return finalComponents;
    }



    /**
     * Aplica el algortimo de dfs para hallar el camino desde s. 
     * 
     * @param  G grafo
     * @param s vertice donde se comienza aplicar el dfs
     * @param ccNumber es el numero de componentes que van a recibir los nodos que visita el dfs
     * @param commpenents es un arreglo que indica si el nodo esta asignado a una componente correspondiente
     */
    public void dfs(EdgeWeightedGraph G, int s, int ccNumber, Integer[] components) {
        Stack<Integer> S = new Stack<Integer>();
        int v, w;
        
        S.push(s);

        while (!S.isEmpty()) {
            v = S.pop();
            
            if (components[v] == -1) {
                components[v] = ccNumber;
                
                for (Edge e : G.adj(v)) {
                    w = e.other(v);
                    if (components[w] == -1) {
                        S.push(w);
                    }
                } 
            }
        }
    }
    
    
    
    /**
     * Pruebas unitarias de el tipo de dato <tt>ConnectedComponents/tt>.
     */
    public static void main(String args[]) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        ConnectedComponents CC = new ConnectedComponents();
        ArrayList<ArrayList<Integer>> ccArray = CC.findComponents(G);
        
        for (ArrayList e : ccArray) 
            System.out.println(e);
        System.out.println("Following are strongly connected components "+
                           "in given graph ");
    
    }
}