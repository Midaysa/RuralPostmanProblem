import java.util.*;

public class Floyd {


    private double[][] distancia;
    private Edge[][] arcofinal; 


    public Floyd(EdgeWeightedGraph G) {
        
        int V = G.V();
        distancia = new double[V][V];
        arcofinal = new Edge[V][V]; 

        for (int v = 0; v < V; v++) {
            for (int w = 0; w < V; w++) {
                distancia[v][w] = Double.POSITIVE_INFINITY;
            }
        }

        /* Actualizacion de la distancia de los caminos  (no necesariamente mas cortos)
        * existentes en el digrafo de entrada segun el costo de cada uno de los arcos.
        *  
		*  Inicializacion de los ultimos arcos de los caminos existentes en el digrafo
        */
        for (int i=0; i < V; i++) {
            
            for (Edge e : G.adj(i)) {  
                distancia[e.either()][e.other(e.either())] = e.weight(); // Actualizacion de la distancia del camino
                distancia[e.other(e.either())][e.either()] = e.weight(); 
                arcofinal[e.either()][e.other(e.either())] = e; // Actualizacion de arco final del camino
                arcofinal[e.other(e.either())][e.either()] = e;
            }
            
            // Actualizacion de valores de distancia y arco final de camino
            // para el caso de los bucles existentes en el digrafo de entrada
            if (distancia[i][i] >= 0.0) {  
                distancia[i][i] = 0.0;
                arcofinal[i][i] = null;
            }
        }


        for (int v = 0; v < V; v++) {
            // Para cada vertice, se realiza busqueda del camino con menor distancia

            for (int i = 0; i < V; i++) {  
            	// Salida del ciclo for actual en caso de que el arco final sea null
            	// para evitar recorrido innecesario en el siguiente ciclo for
                if (arcofinal[i][v] == null) continue;  

                for (int j = 0; j < V; j++) {  
                    
                    if (distancia[i][j] > distancia[i][v] + distancia[v][j]) { // Verifica distancia mas corta
                        distancia[i][j] = distancia[i][v] + distancia[v][j]; // Actualizacion de distancia mas corta
                        arcofinal[i][j] = arcofinal[v][j];  // Actualicacion de arco final del camino mas corto
                        //System.out.println(arcofinal[i][j]);
                        //arcofinal[i][j] = arcofinal[i][v]; 
                    }
                }
            }
        }
        
        // for (int i = 0; i < V; i++) {
        //     for (int j = 0; j < V; j++) {
        //         System.out.println(i + " " + j);
        //         System.out.println("dist["+i+"]["+j+"] = " + distancia[i][j]);
        //     }
        // }
        System.out.println();
    }
    
     
    
    /**
     * Calcula la distancia del camino entre los vertices <tt>s</tt> y <tt>t</tt>.
     * 
     * @param   s   primer vertice
     * @param   t   segundo vertice
     * @return Double  Distancia previamente calculada.
     */
     
    public double dist(int s, int t) {
        return distancia[s][t];
        
        
    }
    

    /**
     * Varifica si hay un camino entre los vertices <tt>s</tt> y <tt>t</tt>.
     * 
     * @param   G   grafo
     * @param   s   primer vertice
     * @param   t   segundo vertice
     * @return <tt>true</tt>  Si el camino existe.
     *         <tt>false</tt> En caso contrario.
     */     
    public boolean hayCamino(int s, int t) {
    	if (distancia[s][t] < Double.POSITIVE_INFINITY) { //Verifica su valor actual sea menor que inicializacion
    		return true;
    	}
    	else {
    		return false;
    	}
    }


    /**
	* Camino mas corto
     */
    public ArrayList<Edge> path(int s, int t) {

        if (!hayCamino(s, t)) return null; // En caso de no haber camino entre el par de vertices

    	ArrayList<Edge> camino = new ArrayList<Edge>();
        
        for (Edge e = arcofinal[s][t]; e != null; e = arcofinal[s][e.either()]) {
            /* Construccion del camino
            * Agrega los arcos pertenecientes al camino, partiendo del arco final
            * hasta llegar al vertice de partida
            */
            camino.add(e); 
           
        }

		// Invierte ArrayList para obtener el camino mas corto en orden correcto
        Collections.reverse(camino); 

		return camino;
    }
    
    public static void main(String[] args) {
	
          In in = new In(args[0]);
          EdgeWeightedGraph G = new EdgeWeightedGraph(in);
          //System.out.println(G);
          Floyd caminosFloyd = new Floyd(G);
          
        
          double  t = caminosFloyd.dist(2,3);
          System.out.println("Distancia: "+t);
         // System.out.println("Path: "+caminosFloyd.path(2,3));
        
        double r = caminosFloyd.dist(1,2);
        System.out.println("Distancia: "+r);
        //System.out.println("Path: "+caminosFloyd.path(1,2));

          

          double  p = caminosFloyd.dist(3,1);
          System.out.println("Distancia: "+p);
          //System.out.println("Path: "+caminosFloyd.path(3,1));


          double  f = caminosFloyd.dist(3,2);
          System.out.println("Distancia: "+f);
          //System.out.println("Path: "+caminosFloyd.path(3,2));
    }    
    
}
