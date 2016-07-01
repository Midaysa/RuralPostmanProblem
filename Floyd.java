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
                arcofinal[e.either()][e.other(e.either())] = e; // Actualizacion de arco final del camino
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

            for (int i =0; i < V; i++) {  

            	// Salida del ciclo for actual en caso de que el arco final sea null
            	// para evitar recorrido innecesario en el siguiente ciclo for
                if (arcofinal[i][v] == null) continue;  

                for (int j = 0; j < V; j++) {  

                    if (distancia[i][j] > distancia[i][v] + distancia[v][j]) { // Verifica distancia mas corta

                        distancia[i][j] = distancia[i][v] + distancia[v][j]; // Actualizacion de distancia mas corta
                        arcofinal[i][j] = arcofinal[v][j];  // Actualicacion de arco final del camino mas corto
                    }
                }
            }
        }
    }
     

    /**
	* Distancia del camino
     */
    public double dist(int s, int t) {
        return distancia[s][t];
    }

    /**
	* Verifica existencia de camino
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
          System.out.println(G);
          Floyd caminosFloyd = new Floyd(G);

          double  t = caminosFloyd.dist(0,1);
          System.out.println("Distancia: "+t);
          System.out.println("Path: "+caminosFloyd.path(0,1));


          double  f = caminosFloyd.dist(0,4);
          System.out.println("Distancia: "+f);
          System.out.println("Path: "+caminosFloyd.path(0,4));
    }    
    
}
