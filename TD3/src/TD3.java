
/* TD3. Classes disjointes (Union-Find) et connectivité d'un réseau 
 * Ce fichier contient 4 classes :
 * 	- PRNG : générateur de nombres pseudo-aléatoires,
 * 	- UnionFind : classes disjointes (vues en cours),
 * 	- Network : un réseau avec une méthode pour effectuer des appels 
 * 	aléatoires, et une autre qui renvoie le nombre d'éléments de 
 * 	la composante connexe d'un membre du réseau,
 * 	- NetworkSimulation : on observe l'évolution de la connectivité 
 * 	du réseau à mesure que l'on ajoute des liens (appels) aléatoirement. 
 */

class PRNG { // Générateur de nombres pseudo-aléatoires (Lagged Fibonacci Generator)
	
	private int[] state; // état des derniers termes
	private final int stateSize = 55; // taille de l'état
	private final int modulus; // base du calcul
	// Dans le cas où le générateur est utilisé pour générer un réseau,
	// modulus est le nombre d'utilisateurs i.e. la taille de la population.
	private int index = 0; // indice de la case à modifier au prochain appel à getNext
	private boolean warmUpDone = false; // y a-t-il déjà eu 55 appels (au moins) à getNext ?
	// non (false) / oui (true)

	// constructeur à partir du champ modulus
	PRNG(int modulus) {
		this.modulus = modulus;
		state = new int[stateSize];
	}

	// Question 1
	// calcule le terme suivant, met a jour l'état interne, met a jour l'indice, et
	// renvoie ce terme
	int getNext() {
		//throw new Error("Méthode getNext() à compléter (Question 1)");
		if(!warmUpDone) {
			this.state[this.index] = (((((300007 * this.index + 900021) % this.modulus) * this.index 
					+ 700018) % this.modulus) * this.index + 200007) % this.modulus;
			if(this.state[this.index]<0) this.state[this.index]+=this.modulus;
			if(this.index==54) {
				this.warmUpDone = true; 
				this.index = 0;
				return this.state[54];
			}
			return this.state[this.index++];
		}
		else {
			this.state[this.index] = (this.state[this.index] %this.modulus + this.state[(this.index+31)%55]%this.modulus)%this.modulus;
			if(this.state[this.index]<0) this.state[this.index]+=this.modulus;
			if(this.index==54) {
				this.index=0;
				return this.state[54];
			}
			return state[this.index++];
		}
	}
}

class UnionFind { // classes disjointes (vues en cours, amphi 3)

	// Question 3.1 (le champ size doit être ajouté)
	private int[] link; // le tableau des liens
	private int[] rank; // le tableau des rangs
	private int numClasses; // nombre de classes
	private int[] size;

	// Question 3.1 (le constructeur doit-être modifié pour initialiser size)
	// constructeur
	UnionFind(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		link = new int[n];
		rank = new int[n];
		for (int i = 0; i < n; i++) {
			link[i] = i;
			rank[i] = 0;
		}
		numClasses = n;
		size = new int[n];
		for(int i = 0; i<n; i++) {
			size[i] = 1;
		}
	}

	// renvoie le nombre de classes
	int numClasses() {
		return numClasses;
	}

	// renvoie le représentant d'un élément i
	int find(int i) {
		if (i < 0 || i >= link.length)
			throw new ArrayIndexOutOfBoundsException(i);
		int p = link[i];
		if (p == i)
			return i;
		int r = find(p);
		link[i] = r; // compression de chemin
		return r;
	}

	// teste si deux éléments i et j sont dans la même classe
	boolean sameClass(int i, int j) {
		return find(i) == find(j);
	}

	// renvoie le cardinal de l'ensemble
	int cardinal() {
		return link.length;
	}
	
	// Question 3.1 (la méthode union doit être modifiée pour mettre à jour size)
	// fait l'union des classes des deux éléments i et j
	void union(int i, int j) {
		int ri = find(i);
		int rj = find(j);
		if (ri != rj) {
			numClasses--;
			if (rank[ri] < rank[rj]) { // rj devient le représentant
				link[ri] = rj;
				size[rj] = size[rj]+size[ri];
			} else {
				link[rj] = ri; // ri devient le représentant
				size[ri] = size[rj]+size[ri];
				if (rank[ri] == rank[rj])
					rank[ri]++;
			}
		}
	}

	// Question 3.1
	// renvoie le nombre d'éléments de la classe de l'élément i
	int getSize(int i) {
		//throw new Error("Méthode getSize() à compléter (Question 3.1)");
		return size[find(i)];
	}
}

class Network { // représente un réseau téléphonique

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
	// utilise le PRNG pour simuler un appel en établissant le lien 
	// entre les deux numeros générés.
	void nextCall() {
		//throw new Error("Méthode nextCall() à compléter (Question 2.1)");
		this.caller = this.prng.getNext();
		this.callee = this.prng.getNext();
		this.nbCalls+=1;
		if(this.callee!=this.caller) this.nbDistinctCalls+=1;
		this.relations.union(this.caller, this.callee);
	}

	// Question 3.2
	// renvoie la taille de la composante connexe (autrement dit la classe) de i
	int getSize(int i) {
		//throw new Error("Méthode getSize() à compléter (Question 3.2)");
		return this.relations.getSize(i);
	}

}

class NetworkSimulation { // simulations sur le réseau

	static Network network;
	
	// Question 2.2
	// renvoie le nombre d'appels effectués jusqu'à un appel entrant ou sortant du président
	static int simulation22(int president, int population) {
		// initialisation du réseau
		network = new Network(population);
		// remplissons le réseau de relations jusqu'a obtenir un appel entrant
		// ou sortant du président.
		//throw new Error("Méthode simulation22() à compléter (Question 2.2)");
		while(network.callee != president&&network.caller != president) {
			network.nextCall();
		}
		return network.nbDistinctCalls;
	}

	// Question 3.3
	// fait des appels jusqu'à un appel entrant ou sortant du président 
	// et renvoie la taille de la composante connexe du président 
	static int simulation33(int president, int population) {
		// initialisation du réseau
		network = new Network(population);
		// remplissons le réseau de relations jusqu'a obtenir un appel entrant
		// ou sortant du président.
		//throw new Error("Méthode simulation33() à compléter (Question 3.3)");
		while(network.callee != president&&network.caller != president) {
			network.nextCall();
		}
		return network.relations.getSize(president);
	}

	// Question 4
	// renvoie le nombre d'appels effectués (on ignore les appels au répondeur) jusqu'à ce que le président soit connecté à 99% du réseau
	static int simulation4(int president, int population) {
		// initialisation du réseau
		network = new Network(population);
		// remplissons le réseau de relations jusqu'à ce que la classe
		// du président contienne 99% de la population.
		//throw new Error("Méthode simulation4() à compléter (Question 4)");
		while(network.callee != president&&network.caller != president) {
			network.nextCall();
		}
		while(network.relations.getSize(president)<0.99*population) {
			network.nextCall();
		}
		return network.nbDistinctCalls;
	}
}

// Question 5 (optionnelle)
// une variante de PRNG permettant d'aller jusqu'à modulus = 100000000

class PRNGLarge {
	// à compléter
	
	private int[] state;
	private final int stateSize = 55;
	private final int modulus;
	private int index = 0; 
	private boolean warmUpDone = false;
	
	PRNGLarge(int modulus) {
		//throw new Error("Constructeur à compléter (Question 5)");
		this.modulus = modulus;
		state = new int[stateSize];
	}
	
	PRNGLarge() {
		this(100000000); 
	}
	
	// note : bien que les calculs nécessitent maintenant le type long en interne
	// la méthode getNext continue de renvoyer une valeur de type int
	int getNext() {
		//throw new Error("Méthode getNext() à compléter (Question 5)");
		long i = this.index;
		long m = this.modulus;
		if(!warmUpDone) {
			this.state[this.index] = (int) ((((((300007 * i + 900021) % m) * i 
					+ 700018) % m) * i + 200007) % m);
			if(this.state[this.index]<0) this.state[this.index]+=this.modulus;
			if(this.index==54) {
				this.warmUpDone = true; 
				this.index = 0;
				i = 0;
				return this.state[54];
			}
			i++;
			return this.state[this.index++];
		}
		else {
			this.state[this.index] = (int)((this.state[this.index] %this.modulus 
					+ this.state[(this.index+31)%55]%this.modulus)%this.modulus);
			if(this.state[this.index]<0) this.state[this.index]+=this.modulus;
			if(this.index==54) {
				this.index=0;
				return this.state[54];
			}
			return state[this.index++];
		}
	}
	
	void printState() {
		System.out.print("state = [");
		for (int i = 0; i < this.stateSize - 1; i++) {
			System.out.print(this.state[i] + ", ");
		}
		System.out.print(this.state[this.stateSize - 1] + "];\n");
	}
	void printIndex() {
		System.out.println("index = " + this.index);
	}
	
}

class NetworkLarge{
	
    private UnionFind relations;
    private PRNGLarge prng;
    private long nbCalls = 0; 
    private int caller = 0;
    private int callee = 0;
    private int nbDistinctCalls = 0;
    
    
    NetworkLarge(int size){
		this.relations = new UnionFind(size);
		this.prng = new PRNGLarge(size);
    }
    
    NetworkLarge(){
	this(100000000);
    }

    void nextCall(){
    	this.caller = this.prng.getNext();
		this.callee = this.prng.getNext();
		this.nbCalls+=1;
		if(this.callee!=this.caller) this.nbDistinctCalls+=1;
		this.relations.union(this.caller, this.callee);
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
    	return this.nbCalls;
    }
    int getVoicemailCalls(){
    	return this.nbDistinctCalls;
    }
    
    int getSize(int N){
    	return this.relations.getSize(N);
    }
}

// this following part is form TD3Solution
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
