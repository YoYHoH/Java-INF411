
/* TD3. Classes disjointes (Union-Find) et connectivité d'un réseau 
 * Ce fichier contient 4 classes :
 * 	- PRNG : générateur de nombres pseudo-aléatoires,
 *  - UnionFind : classes disjointes (vues en cours),
 * 	- Network : un réseau avec une méthode pour effectuer des appels 
 *  	aléatoires, et une autre qui renvoie le nombre d'éléments de 
 *  	la composante connexe d'un membre du réseau,
 * 	- NetworkSimulation : on observe l'évolution de la connectivité 
 * 		du réseau à mesure que l'on ajoute des liens (appels) aléatoirement. 
 */

class PRNG {
	private int[] state;
	private final int stateSize = 55;
	private final int modulus;
	private int index = 0;
	private boolean warm_up_done = false;

	PRNG(int modulus) {
		this.modulus = modulus;
		this.state = new int[this.stateSize];
	}

	// calcule le terme suivant, met a jour l'etat interne, met a jour l'indice, et
	// renvoie ce terme
	int getNext() {
		// Question 1
		int i = this.index;
		this.index ++;
		int s = this.stateSize;
		if(!warm_up_done){
			// attention aux parentheses !
		    this.state[i] = (((((300007 * i + 900021) % modulus) * i + 700018) % modulus) * i + 200007) % modulus;
		    if(index==55) warm_up_done = true;
		}
		else {
			this.state[i] = (this.state[(i + 31) % s] + this.state[i]) % modulus;
		}
		if(index==55) index=0;
		return this.state[i % s];
	}

	// affichage de l'etat interne
	void PrintState() {
		System.out.print("state = [");
		for (int i = 0; i < this.stateSize - 1; i++) {
			System.out.print(this.state[i] + ", ");
		}
		System.out.print(this.state[this.stateSize - 1] + "];\n");
	}
	
	public boolean equals(Object o) {
		PRNG prng = (PRNG) o;
		if (!((this.stateSize==prng.stateSize) && (this.modulus==prng.modulus))) return false;
		for(int i=0 ; i < stateSize ; i++) {
			if (state[i]!=prng.state[i]) return false;
		}
		return true;
	}
}

/**
 * 
 * UnionFind
 * La classe vue en cours
 *
 */

class UnionFind {

	private int[] link;
	private int[] rank;
	private int numClasses;
	private int[] size; // Question 3.1

	UnionFind(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		this.link = new int[n];
		for (int i = 0; i < n; i++)
			this.link[i] = i;
		this.rank = new int[n];
		this.numClasses = n;
		// Question 3.1
		this.size = new int[n];
		for (int i = 0; i < n; i++)
			this.size[i] = 1; // one single element at start-up
	}

	int numClasses() {
		return this.numClasses;
	}

	int find(int i) {
		if (i < 0 || i >= this.link.length)
			throw new ArrayIndexOutOfBoundsException(i);
		int p = this.link[i];
		if (p == i)
			return i;
		int r = this.find(p);
		this.link[i] = r; // compression de chemin
		return r;
	}

	void union(int i, int j) {
		int ri = this.find(i);
		int rj = this.find(j);
		if (ri == rj)
			return; // déjà dans la même classe
		this.numClasses--;
		if (this.rank[ri] < this.rank[rj]) {// rj devient le representant
			this.link[ri] = rj;
			// Question 3.1
			this.size[rj] = this.size[ri] + this.size[rj];
		} else {
			this.link[rj] = ri;// ri devient le representant
			if (this.rank[ri] == this.rank[rj])
				this.rank[ri]++;
			// Question 3.1
			this.size[ri] = this.size[ri] + this.size[rj];
		}
	}

	boolean sameClass(int i, int j) {
		return this.find(i) == this.find(j);
	}

	// renvoie le cardinal de l'ensemble
	int cardinal() {
		return link.length;
	}

	int getSize(int i) {
		// Question 3.1
		return this.size[this.find(i)];
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.numClasses);
		for (int i = 0; i < this.link.length; i++)
			sb.append("[" + i + "->" + this.link[i] + "]");
		return sb.toString();
	}

}

class Network {

	UnionFind relations; // structure de classes disjointes (UnionFind)
	PRNG prng; // generateur de nombres pseudo-aleatoires (PRNG)
	int nbCalls = 0; // nombre d'appels
	int nbDistinctCalls = 0; // nombre d'appels vers un numero distinct
	int caller = 0; // appelant
	int callee = 0; // appele

	Network(int size) {
		relations = new UnionFind(size);
		prng = new PRNG(size);
	}

	// Question 2.1
	// utilise le PRNG pour simuler un appel.
	// etablit le lien entre les deux numeros
	void nextCall() {
		caller = prng.getNext();
		callee = prng.getNext();
		relations.union(caller, callee);
		nbCalls++;
		if (caller != callee)
			nbDistinctCalls ++;
	}

	// Question 3.2
	// Taille de la composante connexe de N
	int getSize(int N) {
		return relations.getSize(N);
	}

}

/**
 * NetworkSimulation
 *
 * Cette classe contient trois méthodes (simulation22, simulation33, et simulation4) 
 * qu'il faut compléter pour répondre aux questions 2.2, 3.3, et 4. Pour 
 * l'essentiel, dans chacune, on fait tourner une boucle qui effectue des appels 
 * sur le réseau «network» jusqu'à ce qu'un certain crière soit satisfait 
 * (atteindre le Président pour les questions 2.2 et 3.3, que la classe du 
 * président contienne au moins 99% de la population pour la question 4). 
 * 
 */

class NetworkSimulation {

	static Network network;
	
	//Question 2.2 : après exécution de la méthode simulation22, les variables
	//first_call_involving_president, caller, callee, et voice_call_counter 
	// doivent contenir la réponse à la question 2.2.
	static int simulation22 (int president, int population) {
		// initialisation du réseau
		network = new Network(population);
		// boucle génératrice
		// remplissons le reseau de relations jusqu'a obtenir un appel entrant
		// ou sortant du président.
		do {
			network.nextCall();
		} while (network.caller != president && network.callee != president);
		return network.nbDistinctCalls;
	}

	//Question 3.3 : après exécution de la méthode simulation33, la variable
	//size_of_President_class doit contenir la réponse à la question 3.3.
	//Pour l'essentiel, on reprend simulation22

	static int simulation33 (int president, int population) {
		// initialisation du réseau
		network = new Network(population);
		// boucle génératrice
		// remplissons le reseau de relations jusqu'a obtenir un appel entrant
		// ou sortant du président.
		do {
			network.nextCall();
		} while (network.caller != president && network.callee != president);
		return network.relations.getSize(president);
	}

	//Question 4 : après exécution de la méthode simulation4, la variable
	//size_of_President_class doit contenir la réponse à la question 4.
	//Pour l'essentiel, on change le critère d'arrêt de la boucle de 
	//simulation22 et simulation33.
	static int simulation4 (int president, int population) {
		// initialisation du réseau
		network = new Network(population);
		// boucle génératrice
		// remplissons le reseau de relations jusqu'a obtenir un appel entrant
		// ou sortant du président.
		do {
			network.nextCall();
		} while (network.getSize(president) < Math.floor(0.99 * population));
		return network.nbDistinctCalls;
	}
	
}

/**
 * classe pour simuler un reseau d'utilisateurs avec 10^8 numeros possibles
 */

class NetworkSimulationLarge {

	public static void main(String[] args) {

		NetworkLarge network = new NetworkLarge(100000000);

		int primeMinisterNumber = 64403411;
		int secretaryNumber = 25086123;
		// remplissons le reseau de relations jusqu'a obtenir un appel entrant
		// vers le numero de la Premiere Ministre, ou un appel sortant depuis son
		// numero.
		// de meme pour le secretaire d'Etat.

		int caller, callee;
		System.out.println(
				"Partie 5. Simulation d'appels pour obtenir le numéro de la Première Ministre ou de son secrétaire d'État.\n"
						+ "Ceci prend quelques secondes, veuillez patienter...");
		do {
			network.nextCall();
			caller = network.getCaller();
			callee = network.getCallee();
		} while (caller != secretaryNumber && callee != secretaryNumber && caller != primeMinisterNumber
				&& callee != primeMinisterNumber);

		// Questions 5.
		System.out.println("Question 5");
		long s_voicemail_calls, s_n, r_voicemail_calls, pm_voicemail_calls, pm_n, r_n;
		int pm_caller, pm_callee, s_caller, s_callee, pm_sizeClique, s_sizeClique, r_sizeClique;

		if (caller == secretaryNumber || callee == secretaryNumber) {
			// the Secretary was involved, now looking for the Prime Minister number
			s_voicemail_calls = network.getVoicemailCalls();
			s_n = network.getCalls() - s_voicemail_calls;
			s_caller = caller;
			s_callee = callee;
			s_sizeClique = network.getSize(secretaryNumber);
			// then try to obtain the number of the other one:
			do {
				network.nextCall();
				caller = network.getCaller();
				callee = network.getCallee();
			} while (caller != primeMinisterNumber && callee != primeMinisterNumber);
			pm_voicemail_calls = network.getVoicemailCalls();
			pm_n = network.getCalls() - pm_voicemail_calls;
			pm_caller = caller;
			pm_callee = callee;
			pm_sizeClique = network.getSize(primeMinisterNumber);

		} else { // the Prime Minister was involved, now looking for the second number
			pm_voicemail_calls = network.getVoicemailCalls();
			pm_n = network.getCalls() - pm_voicemail_calls;
			pm_caller = caller;
			pm_callee = callee;
			pm_sizeClique = network.getSize(primeMinisterNumber);
			// then try to obtain the number of the other one:
			do {
				network.nextCall();
				caller = network.getCaller();
				callee = network.getCallee();
			} while (caller != secretaryNumber && callee != secretaryNumber);
			s_caller = caller;
			s_callee = callee;
			s_voicemail_calls = network.getVoicemailCalls();
			s_n = network.getCalls() - s_voicemail_calls;
			s_sizeClique = network.getSize(secretaryNumber);
		}

		System.out.println("il a fallu " + pm_n + " appels, et " + pm_voicemail_calls
				+ " appels aux répondeurs téléphoniques (" + (pm_n + pm_voicemail_calls)
				+ " appels au total)\n pour obtenir un appel avec la Première Ministre (numéro " + primeMinisterNumber
				+ "),\n appelant = " + pm_caller + ", appelé = " + pm_callee);
		if (pm_caller == primeMinisterNumber)
			System.out.println("La Première Ministre appelle le numéro " + pm_callee);
		else
			System.out.println("La Première Ministre reçoit un appel du numéro " + pm_caller);
		// il y a 10^8 utilisateurs inscrits
		// combien y a-t-il d'utilisateurs dans la classe disjointe de la première
		// ministre?
		System.out.println("à ce stade il y a " + pm_sizeClique
				+ " numéros en relation avec la Première Ministre, la Première Ministre y compris, soit "
				+ ((double) pm_sizeClique / 1000000.0) + " % des utilisateurs du réseau.");

		// secretaire
		System.out.println("il a fallu " + s_n + " appels, et " + s_voicemail_calls
				+ " appels aux répondeurs téléphoniques (" + (s_n + s_voicemail_calls)
				+ " appels au total)\n pour obtenir un appel avec le secrétaire d'État (numéro " + secretaryNumber
				+ "),\n appelant = " + s_caller + ", appelé = " + s_callee);
		if (s_caller == secretaryNumber)
			System.out.println("Le secrétaire d'État appelle le numéro " + s_callee);
		else
			System.out.println("Le secrétaire d'État reçoit un appel du numéro " + s_caller);

		System.out.println("à ce stade il y a " + s_sizeClique
				+ " numéros en relation avec le secrétaire d'État, le secrétaire d'État y compris, soit "
				+ ((double) s_sizeClique / 1000000.0) + " % des utilisateurs du réseau.");

		// Est-ce que la Première Ministre est en relation avec son secrétaire d'État ?
		if (network.areFriends(primeMinisterNumber, secretaryNumber)) {
			System.out.println("La Première Ministre et son secrétaire sont dans la même classe disjointe");
		} else {
			System.out.println("La Première Ministre et son secrétaire ne sont pas dans la même classe disjointe");
		}

		// obtenir la Premiere Ministre en relation avec son secrétaire d'État
		while (!network.areFriends(primeMinisterNumber, secretaryNumber)) {
			network.nextCall();
		}
		r_sizeClique = network.getSize(primeMinisterNumber);
		r_n = network.getCalls();
		r_voicemail_calls = network.getVoicemailCalls();
		System.out.println("La Première Ministre et son secrétaire sont dans la même classe disjointe après " + r_n
				+ " appels dont " + r_voicemail_calls + " appels à la messagerie,\n" + "la classe contient "
				+ r_sizeClique + " numéros distincts, soit " + (r_sizeClique / 1000000.0)
				+ " % des utilisateurs du réseau.");

		// obtenir plus de 95% des utilisateurs en relation avec la Première Ministre
		while (network.getSize(primeMinisterNumber) < 95000000) {
			network.nextCall();
			caller = network.getCaller();
			callee = network.getCallee();
		}
		pm_sizeClique = network.getSize(primeMinisterNumber);
		pm_n = network.getCalls();
		pm_voicemail_calls = network.getVoicemailCalls();

		System.out.println("Après " + pm_n + " appels dont " + pm_voicemail_calls + " appels aux repondeurs, ");
		System.out.println("il y a " + pm_sizeClique
				+ " numéros en relation avec la Première Ministre, la Première Ministre y compris, soit "
				+ (pm_sizeClique / 1000000.0) + " % des utilisateurs du réseau.");

		// Est-ce que la Première Ministre est en relation avec son secrétaire d'État ?
		if (network.areFriends(primeMinisterNumber, secretaryNumber)) {
			System.out.println("La Première Ministre et son secrétaire sont dans la même classe disjointe");
		} else {
			System.out.println("La Première Ministre et son secrétaire ne sont pas dans la même classe disjointe");
		}
	}
}


/**
 * Generateur de nombres pseudo-aleatoires Lagged Fibonacci Generator
 */

class PRNGLarge {
	private int[] state;
	private final int stateSize = 55;
	private final int modulus;

	// indice courant dans l'etat interne
	private int index_mod_55 = 0;// 0 <= index_mod_55 < 55
	private boolean init = true;

	// constructeur
	PRNGLarge(int modulus) {
		this.modulus = modulus;
		this.state = new int[this.stateSize];
	}

	PRNGLarge() {
		this(100000000); // valeur par defaut: 10^8
	}

	// calcule le terme suivant, met a jour l'etat interne, met a jour l'indice, et
	// renvoie ce terme

	int getNext() {
		int Si;
		if (this.init) {
			// initialisation des valeurs jusqu'à l'indice 55
			long SiL;
			long iL = this.index_mod_55, mL = this.modulus;
			SiL = (((((300007 * iL + 900021) % mL) * iL + 700018) % mL) * iL + 200007) % mL;
			Si = (int) SiL;
			this.state[this.index_mod_55] = Si;
		} else {
			int i = this.index_mod_55;
			Si = (this.state[(this.stateSize + i - 24) % this.stateSize] + this.state[i]) % this.modulus;
			// (attention au -24 et au %55, il ne faut pas se retrouver avec une valeur
			// negative car -24 % 55 = -24)
			this.state[i] = Si;
		}
		this.index_mod_55 = (this.index_mod_55 + 1) % this.stateSize;
		if (this.index_mod_55 == 0) {
			this.init = false;
		}
		return Si;
	}

	// affichage de l'etat interne
	void printState() {
		System.out.print("state = [");
		for (int i = 0; i < this.stateSize - 1; i++) {
			System.out.print(this.state[i] + ", ");
		}
		System.out.print(this.state[this.stateSize - 1] + "];\n");
	}
	void printIndex() {
		System.out.println("index_mod_55 = " + this.index_mod_55);
	}
}

class NetworkLarge{

    // on utilise une structure de classes disjointes (UnionFind)
    private UnionFind relations;
    // generateur de nombres pseudo aleatoires (PRNG)
    private PRNGLarge prng;
    // nombre d'appels
    private long calls = 0; // ici on prend un long pour avoir de la marge
    // appelant (celui qui compose le numéro et appelle)
    private int caller = 0;
    // appelé (celui qui répond à l'appel)
    private int callee = 0;
    private int voicemailCalls = 0; // appels au repondeur
    
    
    NetworkLarge(int size){
	this.relations = new UnionFind(size);
	this.prng = new PRNGLarge(size);
    }
    // constructeur par defaut: avec 10^8 utilisateurs
    NetworkLarge(){
	this(100000000);
    }
    // utilise le PRNG pour simuler un appel.
    // etablit le lien entre les deux numeros: methode 'union'
    void nextCall(){
		this.caller = this.prng.getNext();
		this.callee = this.prng.getNext();
	this.calls ++;
	this.relations.union(this.caller, this.callee);
	if(this.caller == this.callee)
	    voicemailCalls++;
    }

    boolean areFriends(int a, int b){
	return this.relations.sameClass(a, b);
    }
    
    int getCaller(){
	return this.caller;
    }
    int getCallee(){
	return this.callee;
    }

    long getCalls(){
	return this.calls;
    }
    int getVoicemailCalls(){
	return this.voicemailCalls;
    }

    // Section 3.
    // On a modifie la classe UnionFind pour calculer le nombre d'elements par classe disjointe
    int getSize(int N){
	return this.relations.getSize(N);
    }
    
}
