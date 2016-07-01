import java.util.*;

/**
* En la clase <tt>BreadthFirstSearchDirected</tt> se implemente el algoritmo de busquedad en
* amplitud de grafos BFS. Para ello recibe el grafo en el cual realizara la busqueda y
* el vertice fuente.
* <p>
* Ademas se proveen algunos metodos para obtener los vertices visitados o los caminos 
* requeridos desde el vertice s.
* <p>
*/
public class BreadthFirstSearchDirected{

	private static final String NEWLINE = System.getProperty("line.separator");

	public Integer[] antes_visit;
	public int[] color;
	public int num_v;

	/**
	* Hace llamado al algoritmo bfs dado un vertice <tt>s</tt>.
	*/
	public BreadthFirstSearchDirected( Digraph G, int s){

		antes_visit = new Integer[G.V()];
		color = new int[G.V()];
		num_v = G.V();
		

		for(int i=0; i<G.V(); i++){
			antes_visit[i] = null;
			color[i] = 0;
		}

		bfs(G,s); 
	}

	/**
	* Algoritmo de busquedad bfs.
	*
	* @param G digrafo
	* @param s vertice fuente
	*/
	public void bfs(Digraph G, int s){

		LinkedList<Integer> cola = new LinkedList<Integer>();
		int u;

		this.color[s] = 1;

		cola.add(s);

		while( !(cola.isEmpty()) ){

			u = cola.removeFirst();

			for( int i : G.adj(u) ){

				if( this.color[i] == 0 ){

					this.color[i] = 1;
					this.antes_visit[i] = u;
					cola.addLast(i);

				}

			}

			this.color[u] = 2;

		}

	}

	/**
	*  
	*
	* @return Integer[], arreglo que indica para cada vertice, el vertice anteriormente visitado
	*/
	public Integer[] arcsVisited(){	return this.antes_visit; }

	
	public LinkedList<Integer> getDirectedPathTo(int v){ 
		Integer w = v;
		LinkedList<Integer> dp = new LinkedList<Integer>();
		dp.add(w);
		while( this.antes_visit[w] != null ){
			dp.addFirst( this.antes_visit[w] );
			w = this.antes_visit[w];	
		}

		return dp;
	}

	/**
	*
	* @return lista enlazada con todos los caminos desde el vertice s hasta los vertices alcanzables desde s.
	*/
	public LinkedList<LinkedList<Integer>> getAllDirectedPath(){ 
		int i;
		LinkedList<LinkedList<Integer>> gap = new LinkedList<LinkedList<Integer>>();
		
		for(i=0; i<this.num_v; i++){
			if (this.color[i]==2){
				gap.add(this.getDirectedPathTo(i));
			}
		}
		return gap; 
	}

	public static void  main(String args[]){
		In in = new In(args[0]);
		Digraph D = new Digraph(in);
		StdOut.println(D);
		BreadthFirstSearchDirected bbfs = new BreadthFirstSearchDirected(D,0);
		for(int i=0; i<D.V();i++){
			StdOut.print(bbfs.color[i] + " ");	
		}
		StdOut.println("");
		for(int i=0; i<D.V(); i++){
			StdOut.print(bbfs.antes_visit[i] + " ");
		}
		StdOut.println("");
		StdOut.println(bbfs.getDirectedPathTo(4));
		StdOut.println(bbfs.getAllDirectedPath());
	}
}
//http://stackoverflow.com/questions/713508/find-the-paths-between-two-given-nodes
//http://stackoverflow.com/questions/13179057/logic-for-finding-all-paths-from-one-graph-node-to-another
//http://stackoverflow.com/questions/9803143/find-all-paths-in-a-graph-with-dfs