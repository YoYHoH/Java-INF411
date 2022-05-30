/* TD10. Plus courts chemins */

import java.lang.reflect.Field;
import java.util.PriorityQueue;

// Algorithme de Dijkstra
class Dijkstra {
	final Graph g; // le graphe de travail
	final int source; // source du plus court chemin recherche
	final int dest; // destination du plus court chemin recherche
	private Fenetre f; // fenetre pour la visualisation

	// Questions 1.1, 1.2 et 4
	
	/* Méthodes à compléter */
	
	// Questions 1.1 et 1.2
	
	int[] dist;
	int[] pred;
	boolean[] settled;
	PriorityQueue<Node> unsettled;
	
	// constructeur
	Dijkstra(Graph g, int source, int dest) {
		this.g = g;
		this.source = source;
		this.dest = dest;
		this.dist = new int [g.nbVertices];
		this.pred = new int [g.nbVertices];
		this.settled = new boolean [g.nbVertices];
		this.unsettled = new PriorityQueue<Node>();
		
		for(int i=0; i<g.nbVertices; i++) {
			if(i == source) {
				dist[i] = 0;
				pred[i] = i; 
			}
			else {
				dist[i] = Integer.MAX_VALUE;
				pred[i] = -1;
			}
			settled[i] = false;
		}
		
		this.unsettled.add(new Node(this.source, this.dist[this.source]));		
		
	}
	
	// Question 2.1 et 2.2

	// mise a jour de la distance, de la priorite, et du predecesseur d'un sommet
	void update(int succ, int current) {
		if(!this.settled[current] && current!=this.dest) {
			this.pred[succ] = this.dist[succ]>this.dist[current]+this.g.weight(succ, current)?
					current : this.pred[succ];
			for(int j : this.g.successors(succ)) {
				this.unsettled.add(j, )
			}
			
		}
	}
	
	// Question 2.1

	// trouve le prochain sommet de unsettled non traite
	int nextNode() {
		
	}
	
	// Questions 2.1, 2.2 et 4

	// une etape de l'algorithme Dijkstra
	int oneStep() {
		throw new Error("Méthode Dijkstra.oneStep() à implémenter (Question 2.1)");		
	}
	
	// Question 2.1

	// algorithme de Dijkstra complet
	int compute() {
		throw new Error("Méthode Dijkstra.compute() à implémenter (Question 2.1)");				
	}
	
	// Question 4
	
	public int getSteps() {
		throw new Error("Méthode Dijkstra.getSteps() à implémenter (Question 4)");
				
	}
	
	/* Méthodes à ne pas changer */
	
	// ralentisseur visualisation
	void slow(){
		if(f == null) return;
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {}
	}
	
	void setFenetre (Fenetre f) { this.f = f; }

	// Cette fonction vérifie si le vecteur 'int[] name' est 
	// présent dans la classe et le renvoie. Sinon, renvoie null 
	private int[] getIntArray(String name) {
		Field field = null;
		for (Field f : getClass().getDeclaredFields()) {
			if (f.getName().equals(name)) {
				field = f;
				break;
			}
		}
		if (field == null)
			return null;
		
		int[] result = null;
		try {
			result = (int[]) field.get(this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int[] getPred() {
		return getIntArray("pred");
	}
	
	public int[] getDist() {
		return getIntArray("dist");
	}	
		
	public void draw () {
		g.drawSourceDestination(f, source, dest);
		g.drawPath(f, getPred(), dest);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Dijkstra))
			return false;
		
		Dijkstra that = (Dijkstra) obj;
		return g.equals(that.g) && source == that.source && dest == that.dest;
	}
}
