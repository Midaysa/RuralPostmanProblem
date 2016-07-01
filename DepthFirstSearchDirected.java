import java.util.*;

/**
* En la clase <tt>DepthFirstSearchDirected</tt> se implemente el algoritmo de busquedad en
* profundidad de grafos DFS. Para ello recibe el grafo en el cual realizara la busqueda y
* el vertice fuente.
* <p>
* Ademas se proveen algunos metodos para obtener los vertices visitados o los caminos 
* requeridos desde el vertice s.
* <p>
*/

public class DepthFirstSearchDirected {

	private static final String NEWLINE = System.getProperty("line.separator");

	public Integer[] antes_visit;
	public int[] color;
	public int num_v;

	/**
	* Hace llamado al algoritmo dfs dado un vertice <tt>s</tt>.
	*/
	public DepthFirstSearchDirected(Digraph G, int s){
		antes_visit = new Integer[G.V()];
		color = new int[G.V()];
		num_v = G.V();
		for(int i=0; i<G.V(); i++){
			antes_visit[i] = null;
			color[i] = 0;
		}

		dfs(G,s);
	}

	public void dfsAlternativo(Digraph G, int v){

		this.color[v] = 1;

		for( int i : G.adj(v) ){

			if(this.color[i] == 0){

				this.antes_visit[i] = v;
				this.dfsAlternativo( G, i);

			}

		}

		this.color[v] = 2;
		
	}

	/**
	* Algoritmo de busquedad dfs.
	*
	* @param G digrafo
	* @param s vertice fuente
	*/
	public void dfs(Digraph G, int s){

		for(int j=0; j<G.V(); j++){
			this.color[j] = 0;
		}

		this.color[s] = 2;

		for( int i: G.adj(s) ){
			if(G.containsEdge(s,i) && s!=i){
				this.antes_visit[i] = s;
				this.dfsAlternativo( G, i);
			}
		}
	}

	/**
	*  
	*
	* @return Integer[], arreglo que indica para cada vertice, el vertice anteriormente visitado
	*/
	public Integer[] arcsVisited(){	return this.antes_visit; }

	/**
	* 
	* @param vertice v
	*
	* @return lista enlazada que contiene los vertices del camino calculado con dfs
	*/
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
		LinkedList<LinkedList<Integer>> gip = new LinkedList<LinkedList<Integer>>();
		
		for(i=0; i<this.num_v; i++){
			if (this.color[i]==2){
				gip.add(this.getDirectedPathTo(i));
			}
		}
		return gip; 
	}

	public static void  main(String args[]){
		In in = new In(args[0]);
		Digraph D = new Digraph(in);
		StdOut.println(D);
		DepthFirstSearchDirected bbfs = new DepthFirstSearchDirected(D,0);
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