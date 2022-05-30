
/* TD1. Bataille
 * Ce fichier contient deux classes :
 * 		- Deck représente un paquet de cartes,
 * 		- Battle représente un jeu de bataille.
 */

import java.util.LinkedList;

class Deck { // représente un paquet de cartes

	LinkedList<Integer> cards;

	// constructeur d'un paquet vide
	Deck() {
		cards = new LinkedList<Integer>();
	}

	// constructeur à partir du champ
	Deck(LinkedList<Integer> cards) {
		this.cards = cards;
	}

	// constructeur d'un paquet de cartes complet trié avec nbVals valeurs
	Deck(int nbVals) {
		cards = new LinkedList<Integer>();
		for (int j = 1; j <= nbVals; j++)
			for (int i = 0; i < 4; i++)
				cards.add(j);
	}

	// copie le paquet de cartes
	Deck copy() {
		Deck d = new Deck();
		for (Integer card : cards)
			d.cards.addLast(card);
		return d;
	}

	// chaîne de caractères représentant le paquet
	@Override
	public String toString() {
		return cards.toString();
	}

	// Question 1

	// prend une carte du paquet d pour la mettre à la fin du paquet courant
	int pick(Deck d) {
		// throw new Error("Méthode pick(Deck d) à compléter (Question 1)");
		if (!d.cards.isEmpty()) {
			int x = d.cards.removeFirst();
			cards.addLast(x);
			return x;
		} else {
			return -1;
		}
	}

	// prend toutes les cartes du paquet d pour les mettre à la fin du paquet courant
	void pickAll(Deck d) {
		// throw new Error("Méthode pickAll(Deck d) à compléter (Question 1)");
		while (!d.cards.isEmpty())
			pick(d);
	}

	// vérifie si le paquet courant est valide
	boolean isValid(int nbVals) {
		// throw new Error("Méthode isValid(int nbVals) à compléter (Question 1)");
		int[] numbers = new int[nbVals];
		for (Integer x : cards) {
			if (x < 1 || x > nbVals || numbers[x - 1] > 3)
				return false;
			numbers[x - 1]++;
		}
		return true;
	}

	// Question 2.1

	// choisit une position pour la coupe
	int cut() {
		// throw new Error("Méthode cut() à compléter (Question 2.1)");
		int cut = 0;
		for (int i = 0; i < cards.size(); i++) {
			cut += (Math.random() < .5 ? 1 : 0);
		}
		return cut;
	}

	// coupe le paquet courant en deux au niveau de la position donnée par cut()
	Deck split() {
		// throw new Error("Méthode split() à compléter (Question 2.1)");
		int c = cut();
		Deck res = new Deck();
		for (int i = 0; i < c; i++)
			res.pick(this);
		return res;
	}

	// Question 2.2

	// mélange le paquet courant et le paquet d
	void riffleWith(Deck d) {
		// throw new Error("Méthode riffleWith(Deck d) à compléter (Question 2.2)");
		Deck f = new Deck();
		while (!this.cards.isEmpty() || !d.cards.isEmpty()) {
			int a = this.cards.size();
			int b = d.cards.size();
			if (Math.random() * (a + b) < a)
				f.pick(this);
			else
				f.pick(d);
		}
		this.cards = f.cards;
	}

	// Question 2.3

	// mélange le paquet courant au moyen du riffle shuffle
	void riffleShuffle(int m) {
		// throw new Error("Méthode riffleShuffle(int m) à compléter (Question 2.3)");
		for (int i = 0; i < m; i++) {
			riffleWith(split());
		}	
	}
}

class Battle { // représente un jeu de bataille

	Deck player1;
	Deck player2;
	Deck trick;
	boolean turn;

	// constructeur d'une bataille sans cartes
	Battle() {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
		turn = true;
	}
	
	// constructeur à partir des champs
	Battle(Deck player1, Deck player2, Deck trick) {
		this.player1 = player1;
		this.player2 = player2;
		this.trick = trick;
		turn = true;
	}

	// copie la bataille
	Battle copy() {
		Battle r = new Battle();
		r.player1 = this.player1.copy();
		r.player2 = this.player2.copy();
		r.trick = this.trick.copy();
		r.turn = this.turn;
		return r;
	}

	// chaîne de caractères représentant la bataille
	@Override
	public String toString() {
		return "Joueur 1 : " + player1.toString() + "\n" + "Joueur 2 : " + player2.toString() + "\nPli " + trick.toString();
	}

	// Question 3.1

	// constructeur d'une bataille avec un jeu de cartes de nbVals valeurs
	Battle(int nbVals) {
		// throw new Error("Constructeur Battle() à compléter (Question 3.1)");
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
		Deck d = new Deck(nbVals);
		d.riffleShuffle(7);
		for (int i = 0; i < 2*nbVals; i++) {
			player1.pick(d);
			player2.pick(d);
		}
		turn = true; //(Math.random() < .5);
	}

	// Question 3.2

	// teste si la partie est finie
	boolean isOver() {
		// throw new Error("Méthode isOver() à compléter (Question 3.2)");
		return player1.cards.isEmpty() || player2.cards.isEmpty();
	}

	// effectue un tour de jeu
	boolean oneRound() {
		// throw new Error("Méthode oneRound() à compléter (Question 3.2)");
		if (isOver()) return false;
		int card1, card2;
		if (turn) {
			card1 = trick.pick(player1);
			card2 = trick.pick(player2);
		} else {
			card2 = trick.pick(player2);
			card1 = trick.pick(player1);			
		}
		turn = !turn;
		while (card1 == card2)
			for (int i = 0; i < 2; i++)
				if (isOver()) return false;
				else {
					if (turn) {
						card1 = trick.pick(player1);
						card2 = trick.pick(player2);
					} else {
						card2 = trick.pick(player2);
						card1 = trick.pick(player1);			
					}
					turn = !turn;
				}
		if (card1 > card2)
			player1.pickAll(trick);
		else
			player2.pickAll(trick);
		return true;
	}

	// Question 3.3

	// renvoie le gagnant
	int winner() {
		// throw new Error("Méthode winner() à compléter (Question 3.3)");
		int c1 = player1.cards.size();
		int c2 = player2.cards.size();
		if (c1 == c2) return 0;
		if (c1 > c2) return 1;
		else return 2;
	}

	// effectue une partie avec un nombre maximum de coups fixé
	int game(int turns) {
		// throw new Error("Méthode game(int turns) à compléter (Question 3.3)");
		while (turns > 0 && oneRound())
			turns--;
		return winner();
	}

	// Question 4.1

	// effectue une partie sans limite de coups, mais avec détection des parties infinies
	int game() {
		// throw new Error("Méthode game() à compléter (Question 4.1)");
		// on utilise l'algorithme du lièvre et de la tortue pour repérer les parties infinies
		Battle turtle = this.copy();
		Battle hare = this;
		do {
			// Le lièvre joue deux tours ...
			if(!hare.oneRound()) return hare.winner();
			if(!hare.oneRound()) return hare.winner();
			// ... alors que la tortue joue un tour.
			turtle.oneRound();
		} while(!hare.toString().equals(turtle.toString()));
		return 3;
	}

	// Question 4.2

	// effectue des statistiques sur le nombre de parties infinies
	static void stats(int nbVals, int nbGames) {
		// throw new Error("Méthode stats(int bvVals, int nb_of_games) à compléter (Question 4.2)");
		int[] results = new int[4];
		for (int i = 0; i < nbGames; i++)
			results[new Battle(nbVals).game()]++;
		System.out.println("Statistiques sur les " + nbGames + " parties jouées :");
		System.out.println("   " + results[0] + " parties se sont terminées sur une égalité,");
		System.out.println("   " + results[1] + " parties ont été gagnées par le joueur 1,");
		System.out.println("   " + results[2] + " parties ont été gagnées par le joueur 2,");
		System.out.println("   " + results[3] + " parties sont infinies.");
	}
}
