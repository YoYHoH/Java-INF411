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
		//throw new Error("Méthode Fenwick(int lo, int hi) à completer (Question 2.1)");
		
		this.lo = lo;
		this.hi = hi;
		int mid = (lo+hi)/2;
		this.acc = 0;
		if(hi-lo>1) {
			this.left = new Fenwick(lo,mid);
			this.right = new Fenwick(mid, hi);
		}
	}

	// Question 2.2

	// renvoie la feuille associée à l'intervalle [i,i+1[ si elle existe et null
	// sinon.
	Fenwick get(int i) {
		//throw new Error("Méthode Fenwick get(int i) à compléter (Question 2.2)");
	
		if(i<lo || i>=hi) return null;
		Fenwick f = this;
		while(f.left!=null) {
			f = (f.right.lo <= i)? f.right:f.left;
		}
		return f;
		
	}

	// Question 2.3

	// augmente la valeur stockée dans la case d'indice i du tableau
	// et actualise l'arbre pour maintenir les propriétés d'un arbre de Fenwick.
	void inc(int i) {
		//throw new Error("Méthode inc(int i) à compléter (Question 2.3)");

		if(i<lo || i>=hi) return;
		this.acc+=1;
		if(i<(lo+hi)/2) {
			if(this.left==null) return;
			this.left.inc(i);
		}
		else {
			if(this.right == null) return;
			this.right.inc(i);
		}
	}

	// Question 2.4

	// renvoie la somme des valeurs des cases d'indice < i
	int cumulative(int i) {
		//throw new Error("Méthode cumulative(int i) à compléter (Question 2.4)");

		if(i<=lo) return 0;
		if(i>=hi) return this.acc;
		if((hi-lo)/2>0)
			return this.left.cumulative(i)+this.right.cumulative(i);
		return this.acc;
	}

}

class CountInversions {

	// Question 3.1 :

	// implémentation naive, quadratique pour calculer le nombre d'inversions
	static int countInversionsNaive(int[] a) {
		//throw new Error("Méthode countInversionsNaive(int[] a) à compléter (Question 3.1)");
	
		int i = 0 ;
		for(int j=0; j<a.length; j++) {
			for(int k = j; k<a.length; k++) {
				i += (a[j]>a[k])? 1:0; 
			}
		}
		return i;
	}

	// Question 3.2 :

	// une implémentation moins naive, mais supposant une plage d'éléments pas trop
	// grande espace O(max-min)

	static int countInversionsFen(int[] a) {
		//throw new Error("Méthode countInversions(int[] a) à completer (Question 3.2)");
		int n = 0;
		if(a==null||a.length==0) return 0;
		int min = a[0] , max = a[0];
		for(Integer i : a) {
			min = (i<min)? i:min;
			max = (i>max)? i:max;
		}
		Fenwick f = new Fenwick(min, max+1);
		for(int j = a.length -1 ; j >=0; j--) {
			f.inc(a[j]);
			n += f.cumulative(a[j]);
		}
		return n;
	}

	// Question 3.3

	// une implémentation encore meilleure, sans rien supposer sur les éléments
	// cette fois
	static int countInversionsBest(int[] a) {
		//throw new Error("Méthode countInversionsBest(int[] a) à completer (Question 3.3)");
		int n = a.length;
		int[] copy = Arrays.copyOf(a, n);
		Arrays.sort(copy);
		int[] b = new int[n];
		for (int i = 0; i < n; i++)
			b[i] = Arrays.binarySearch(copy, a[i]);
		return countInversionsFen(b);
		
	}
	
//	static int[] copyOf(int[] a, int n) {
//		int [] b = new int[n];
//		for(int i = 0; i<n; i++) {
//			b[i]=a[i];
//		}
//		return b;
//	}
//	
//	static void sort(int[] a) {
//		int [] t = new int[a.length];
//		interMerge(a, t, 0, a.length-1);
//	}
//	
//	static void interMerge(int[] a, int[] t, int left, int right) {
//		if(left<right) {
//			int mid = (left+right)/2;
//			interMerge(a, t, left, mid);
//			interMerge(a, t, mid+1, right);
//			mergeSort(a, t, left, mid, right);
//		}
//	}
//	
//	static void mergeSort(int[] a, int[] t, int left, int mid, int right) {
//		int i = left;
//		int j = mid+1;
//		int k = 0;
//		while(i<=mid && j<=right) {
//			t[k++] = a[i]<=a[j]? a[i++]:a[j++];
//		}
//		while(i<=mid) {
//			t[k++] = a[i++];
//		}
//		while(j<=right) {
//			t[k++] = a[i++];
//		}
//		
//		for(i=0; i<k; i++) {
//			a[left+i] = t[i];
//		}
//	}
	

	
}
