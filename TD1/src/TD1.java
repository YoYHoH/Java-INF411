
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
		for (Integer card : this.cards)
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
		if (d!=null && !d.cards.isEmpty()) {
			int card = d.cards.getFirst();
			this.cards.addLast(card);
			d.cards.remove();
			return card;
		}
		else return -1;
	}

	// prend toutes les cartes du paquet d pour les mettre à la fin du paquet courant
	void pickAll(Deck d) {
		for(Integer card : d.cards) 
			this.cards.addLast(card);
		d.cards.clear();
	}

	// vérifie si le paquet courant est valide
	boolean isValid(int nbVals) {
		int nCard [] = new int [nbVals];
		boolean b = true;
		for(Integer card : this.cards) {
			b = b && card >=1 && card <=nbVals;
			if(!b) return b;
			nCard[card-1]++;
		}
		for(Integer i : nCard) {
			if(i>4) return false;
		}
		return true;
	}

	// Question 2.1

	// choisit une position pour la coupe
	int cut() {
		int n = 0;
		double r;
		for(int i = 0 ; i<this.cards.size() ; i++) {
			r = Math.random() ;
			n = r<0.5? n+1:n; 
		}
		return n;
	}

	// coupe le paquet courant en deux au niveau de la position donnée par cut()
	Deck split() {
		Deck d = new Deck();
		int c = this.cut();
		for(int i = 0 ; i < c ; i++)
			d.pick(this);
		return d;	
	}

	// Question 2.2

	// mélange le paquet courant et le paquet d
	void riffleWith(Deck d) {
		Deck f = new Deck();
		while(!this.cards.isEmpty() || !d.cards.isEmpty()) {
			int a = this.cards.size();
			int b = d.cards.size();
			f.pick((Math.random()*(a+b)<a)? this:d);
		}
		this.cards = f.cards;
	}

	// Question 2.3

	// mélange le paquet courant au moyen du riffle shuffle
	void riffleShuffle(int m) {
		for(int i = 0 ; i < m; i++) {
			Deck d = new Deck(); 
			d =	this.split();
			this.riffleWith(d);
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
		this.turn = true;
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
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
		Deck d = new Deck(nbVals);
		d.riffleShuffle(7);
		for(int i = 0; i < 2*nbVals; i++) {
			player1.pick(d);
			player2.pick(d);
		}
	}

	// Question 3.2

	// teste si la partie est terminée
	boolean isOver() {
		if(player1.cards.isEmpty() || player2.cards.isEmpty()) return true;
		else return false;
	}

	// effectue un tour de jeu
	boolean oneRound() {
		if(isOver()) return false;
		int card1, card2;
		if (turn) {
			card1 = trick.pick(player1);
			card2 = trick.pick(player2);
		}
		else {
			card2 = trick.pick(player2);
			card1 = trick.pick(player1);
		}
		turn = !turn;
		while(card1==card2) {
			if(isOver()) return false;
			else {
				if (turn) {
					card1 = trick.pick(player1);
					card2 = trick.pick(player2);
				}
				else {
					card2 = trick.pick(player2);
					card1 = trick.pick(player1);
				}
				turn = !turn;
			}
			if(isOver()) return false;
			else {
				if (turn) {
					card1 = trick.pick(player1);
					card2 = trick.pick(player2);
				}
				else {
					card2 = trick.pick(player2);
					card1 = trick.pick(player1);
				}
				turn = !turn;
			}
		}
		if(card1>card2) player1.pickAll(trick);
		else player2.pickAll(trick);
		return true;
	}

	// Question 3.3

	// renvoie le gagnant
	int winner() {
		int a = player1.cards.size();
		int b = player2.cards.size();
		if(a>b) return 1;
		if(a<b) return 2;
		else return 0;
	}

	// effectue une partie avec un nombre maximum de coups fixé
	int game(int turns) {
		for(int i = 0; i < turns; i++) {
			if(!oneRound()) break;
		}
		return winner();
	}

	// Question 4.1

	// effectue une partie sans limite de coups, mais avec détection des parties infinies
	int game() {
		Battle tortue = this.copy();
		Battle lievre = this;
		do {
			if(!lievre.oneRound()) return lievre.winner();
			if(!lievre.oneRound()) return lievre.winner();
			tortue.oneRound();
		}while(!lievre.toString().equals(tortue.toString()));
		return 3;
	}

	// Question 4.2

	// effectue des statistiques sur le nombre de parties infinies
	static void stats(int nbVals, int nbGames) {
		int[] results = new int[4];
		for (int i = 0; i < nbGames; i++) {
			Battle bbb = new Battle(nbVals);
			results[bbb.game()]++;
		}
		System.out.println("Statistiques sur les " + nbGames + " parties jouées :");
		System.out.println("   " + results[0] + " parties se sont terminées sur une égalité,");
		System.out.println("   " + results[1] + " parties ont été gagnées par le joueur 1,");
		System.out.println("   " + results[2] + " parties ont été gagnées par le joueur 2,");
		System.out.println("   " + results[3] + " parties sont infinies.");
	}
}
