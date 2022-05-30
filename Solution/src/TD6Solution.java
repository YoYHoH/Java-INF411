import java.util.Arrays;

class Fenwick {
	Fenwick left;
	Fenwick right;
	final int lo;
	final int hi;
	int acc;

	// Question 2.1

	// Constructeur
	Fenwick(int lo, int hi) {
		assert lo < hi;
		this.lo = lo;
		this.hi = hi;
		this.acc = 0;
		if (hi > lo + 1) {
			int A = (hi + lo) / 2;
			this.left = new Fenwick(lo, A);
			this.right = new Fenwick(A, hi);
		}
	}

	// Question 2.2

	// renvoie la feuille associée à l'intervalle [i,i+1[ si elle existe et null
	// sinon.
	Fenwick get(int i) {
		if (i < lo || i >= hi)
			return null;
		Fenwick cur = this;
		while (cur.left != null)
			cur = (cur.right.lo <= i) ? cur.right : cur.left;
		return cur;
	}

	// Question 2.3

	// augmente la valeur stockée dans la case d'indice i du tableau
	// et actualise l'arbre pour maintenir les propriétés d'un arbre de Fenwick.
	void inc(int i) {
		if (lo <= i && i < hi) {
			acc++;
			if (hi - lo >= 2) {
				left.inc(i);
				right.inc(i);
			}
		}
	}

	// Question 2.4

	// renvoie la somme des valeurs des cases d'indice < i
	int cumulative(int i) {
		if (i > hi)
			return acc;
		if (i <= lo)
			return 0;
		int delta = (hi - lo) / 2;
		if (delta > 0) {
			return left.cumulative(i) + right.cumulative(i);
		}
		return acc;
	}

}

class CountInversions {

	// Question 3.1 :

	// implémentation naive, quadratique pour calculer le nombre d'inversions
	static int countInversionsNaive(int[] a) {
		int inv = 0;
		for (int i = 0; i < a.length; i++)
			for (int j = i + 1; j < a.length; j++)
				if (a[j] < a[i])
					inv++;
		return inv;
	}

	// Question 3.2 :

	// une implémentation moins naive, mais supposant une plage d'éléments pas trop
	// grande espace O(max-min)

	static int countInversionsFen(int[] a) {
		if (a.length == 0)
			return 0;
		int min = a[0], max = a[0];
		for (int x : a) {
			if (x > max)
				max = x;
			if (x < min)
				min = x;
		}
		Fenwick ft = new Fenwick(min, max + 1);
		int inv = 0;
		for (int i = a.length - 1; i >= 0; i--) {
			int x = a[i];
			inv += ft.cumulative(x);
			ft.inc(x);
		}
		return inv;
	}

	// Question 3.3

	// une implémentation encore meilleure, sans rien supposer sur les éléments
	// cette fois
	static int countInversionsBest(int[] a) {
		int n = a.length;
		int[] copy = Arrays.copyOf(a, n);
		Arrays.sort(copy);
		int[] b = new int[n];
		for (int i = 0; i < n; i++)
			b[i] = Arrays.binarySearch(copy, a[i]);
		return countInversionsFen(b);
	}

}
