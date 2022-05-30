
/* TD2. Fruits et tables de hachage
 * Ce fichier contient 7 classes:
 * 		- Row représente une ligne de fruits,
 * 		- CountConfigurationsNaive compte naïvement les configurations stables,
 * 		- Quadruple manipule des quadruplets,
 * 		- HashTable construit une table de hachage,
 * 		- CountConfigurationsHashTable compte les configurations stables en utilisant notre table de hachage,
 * 		- Triple manipule des triplets,
 * 		- CountConfigurationsHashMap compte les configurations stables en utilisant la HashMap de java.
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

class Row { // représente une ligne de fruits
	private final int[] fruits;

	// constructeur d'une ligne vide
	Row() {
		this.fruits = new int[0];
	}

	// constructeur à partir du champ fruits
	Row(int[] fruits) {
		this.fruits = fruits;
	}

	// méthode equals pour comparer la ligne à un objet o
	@Override
	public boolean equals(Object o) {
		// on commence par transformer l'objet o en un objet de la classe Row
		// ici on suppose que o sera toujours de la classe Row
		Row that = (Row) o;
		// on vérifie que les deux lignes ont la meme longueur
		if (this.fruits.length != that.fruits.length)
			return false;
		// on vérifie que les ièmes fruits des deux lignes coincident
		for (int i = 0; i < this.fruits.length; ++i) {
			if (this.fruits[i] != that.fruits[i])
				return false;
		}
		// on a alors bien l'égalité des lignes
		return true;
	}

	// code de hachage de la ligne
	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < fruits.length; ++i) {
			hash = 2 * hash + fruits[i];
		}
		return hash;
	}

	// chaîne de caracteres représentant la ligne
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < fruits.length; ++i)
			s.append(fruits[i]);
		return s.toString();
	}

	// Question 1

	// renvoie une nouvelle ligne en ajoutant fruit à la fin de la ligne
	Row addFruit(int fruit) {
		//throw new Error("Méthode addFruit(int fruit) à compléter (Question 1)");
		int n = this.fruits.length;
		int [] rowFruit = new int [n+1];
		for(int i = 0 ; i<n; i++) {
			rowFruit[i] = this.fruits[i];
		}
		rowFruit[n] = fruit;
		return new Row(rowFruit);
	}

	// renvoie la liste de toutes les lignes stables de largeur width
	static LinkedList<Row> allStableRows(int width) {
		//throw new Error("Méthode allStableRows(int width) à compléter (Question 1)");
		LinkedList <Row> listRow = new LinkedList<Row>();
		if(width>0) {
			for(Row r : allStableRows(width-1)) {
				if(width==1 || width == 2) {
					listRow.add(r.addFruit(0));
					listRow.add(r.addFruit(1));
				}
				if(width>2) {
					int n1 = r.fruits[r.fruits.length-1];
					int n2 = r.fruits[r.fruits.length-2];
					if(n1!=n2) {
						listRow.add(r.addFruit(0));
						listRow.add(r.addFruit(1));
					}
					else {
						listRow.add((r.addFruit(1-n1)));
					}
				}
			}
		}
		else listRow.add(new Row());
		return listRow;
	}

	// vérifie si la ligne peut s'empiler avec les lignes r1 et r2
	// sans avoir trois fruits du même type adjacents
	boolean areStackable(Row r1, Row r2) {
		//throw new Error("Méthode areStackable(Row r1, Row r2) à compléter (Question 1)");
		int n = this.fruits.length;
		int n1 = r1.fruits.length;
		int n2 = r2.fruits.length;
		if(n==n1 && n==n2) {
			for(int i =0 ; i<n; i++) {
				if(this.fruits[i]==r1.fruits[i] && this.fruits[i]==r2.fruits[i]) return false;
			}
			return true;
		}
		else return false;
	}
}

// COMPTAGE NAIF
class CountConfigurationsNaive { // comptage des configurations stables

	// Question 2

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		//throw new Error("Méthode count(Row r1, Row r2, LinkedList<Row> rows, int height) de la classe CountConfigurationsHashNaive à compléter (Question 2)");
		if(height <1 || height == 1) return (long)0;
		if(height ==2) return (long)1;
		else {
			long n = 0;
			for(Row r3 : rows) {
				if(r3.areStackable(r1, r2)) n+=count(r2,r3,rows,height-1);
			}
			return n;
		}
	}

	// renvoie le nombre de grilles à n lignes et n colonnes
	static long count(int n) {
		//throw new Error("Méthode count(int n) de la classe CountConfigurationsHashNaive à compléter (Question 2)");
		LinkedList<Row> rows = Row.allStableRows(n);
		long m = 0;
		for(Row r1: rows) {
			for(Row r2: rows) {
				m+= count(r1,r2,rows,n);
			}
		}
		if(n==0) {
			m=1;
		}
		if(n==1) {
			m = rows.size();
		}
		return m;
	}
}

// CONSTRUCTION ET UTILISATION D'UNE TABLE DE HACHAGE

class Quadruple { // quadruplet (r1, r2, height, result)
	Row r1;
	Row r2;
	int height;
	long result;

	Quadruple(Row r1, Row r2, int height, long result) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
		this.result = result;
	}
}

class HashTable { // table de hachage
	final static int M = 50000;
	Vector<LinkedList<Quadruple>> buckets;

	// Question 3.1

	// constructeur
	HashTable() {
		//throw new Error("Constructeur HashTable() à compléter (Question 3.1)");
		this.buckets = new Vector<LinkedList<Quadruple>>(M);
		
		for(int i = 0; i<M; i++)
			this.buckets.add(new LinkedList<Quadruple>());
	}

	// Question 3.2

	// renvoie le code de hachage du triplet (r1, r2, height)
	static int hashCode(Row r1, Row r2, int height) {
		//throw new Error("Méthode hashCode(Row r1, Row r2, int height) à compléter (Question 3.2)");
		return (r1.hashCode()*r1.hashCode()+7)*(r2.hashCode()*r2.hashCode()+13)*height;
	}

	// renvoie le seau du triplet (r1, r2, height)
	int bucket(Row r1, Row r2, int height) {
		//throw new Error("Méthode bucket(Row r1, Row r2, int height) à compléter (Question 3.2)");
		int i = hashCode(r1, r2, height)%M;
		if(i<0) i+=M;
		return i;
	}

	// Question 3.3

	// ajoute le quadruplet (r1, r2, height, result) dans le seau indiqué par la
	// methode bucket
	void add(Row r1, Row r2, int height, long result) {
		//throw new Error("Méthode add(Row r1, Row r2, int height, long result) à compléter (Question 3.3)");
		//if(height==0) return;
		int b = bucket(r1,r2,height);
		LinkedList<Quadruple> bb = new LinkedList<Quadruple>();
		bb.add(new Quadruple(r1, r2, height, result));
		this.buckets.add(b, bb);
	}

	// Question 3.4

	// recherche dans la table une entrée pour le triplet (r1, r2, height)
	Long find(Row r1, Row r2, int height) {
		//throw new Error("Méthode Quadruple find(Row r1, Row r2, int height) à compléter (Question 3.4)");
		int h = bucket(r1, r2, height);
		LinkedList<Quadruple> LQ = this.buckets.elementAt(h);
		for(Quadruple Q : LQ) {
			if(Q.r1.equals(r1)&&Q.r2.equals(r2)&&Q.height==height) {
				return Long.valueOf(Q.result);
			}
		}
		return null;
	}

}

class CountConfigurationsHashTable { // comptage des configurations stables en utilisant notre table de hachage
	static HashTable memo = new HashTable();

	// Question 4

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	// en utilisant notre table de hachage
	
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
//		throw new Error(
//				"Méthode count(Row r1, Row r2, LinkedList<Row> rows, int height) de la classe CountConfigurationsHashTable à compléter (Question 4)");
		if(height <= 1) return 0;
		if(height == 2) return 1;
		Long r = memo.find(r1, r2, height);
		if(r!=null) return r.longValue();
		else {
			r = 0L;
			for (Row r3 : rows) {
				if (r3.areStackable(r1, r2))
					r += count(r2, r3, rows, height - 1);
			}
			memo.add(r1, r2, height, r);
			return r;
		}
	}

	// renvoie le nombre de grilles a n lignes et n colonnes
	
	static long count(int n) {
//		throw new Error("Méthode count(int n) de la classe CountConfigurationsHashTable à compléter (Question 4)");
		LinkedList<Row> rows = Row.allStableRows(n);
		long m = 0;
		for(Row r1: rows) {
			for(Row r2: rows) {
				m+= count(r1,r2,rows,n);
			}
		}
		if(n==0) {
			m=1;
		}
		if(n==1) {
			m = rows.size();
		}
		return m;
	}
	
}

//UTILISATION DE HASHMAP

class Triple { // triplet (r1, r2, height)
	// à compléter
	Row r1;
	Row r2;
	int height;
	
	Triple(Row r1, Row r2, int height) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
	}
	
	@Override
	public boolean equals(Object o) {
		Triple that = (Triple) o;
		return this.r1.equals(that.r1) && this.r2.equals(that.r2) && this.height == that.height;
	}

	@Override
	public int hashCode() {
		return HashTable.hashCode(r1, r2, height);
	}
}

class CountConfigurationsHashMap { // comptage des configurations stables en utilisant la HashMap de java
	static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

	// Question 5

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	// en utilisant la HashMap de java
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
//		throw new Error(
//				"Méthode count(Row r1, Row r2, LinkedList<Row> rows, int height) de la classe CountConfigurationsHashMap à compléter (Question 5)");
		if(height <= 1) return 0;
		if(height == 2) return 1;
		Triple t = new Triple(r1, r2, height);
		Long r = memo.get(t);
		if (r != null)
			return r;
		r = 0L;
		for (Row r3 : rows) {
			if (r3.areStackable(r1, r2))
				r += count(r2, r3, rows, height - 1);
		}
		memo.put(t, r);
		return r;
	}

	// renvoie le nombre de grilles à n lignes et n colonnes
	static long count(int n) {
		//throw new Error("Méthode count(int n) de la classe CountConfigurationsHashMap à compléter (Question 5)");
		LinkedList<Row> rows = Row.allStableRows(n);
		long m = 0;
		for(Row r1: rows) {
			for(Row r2: rows) {
				m+= count(r1,r2,rows,n);
			}
		}
		if(n==0) {
			m=1;
		}
		if(n==1) {
			m = rows.size();
		}
		return m;
	}
}
