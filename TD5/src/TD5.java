/* TD5.  Arbres k-dimensionnels */

import java.util.Vector;

class KDTree {
	int depth;
	double[] point;
	KDTree left;
	KDTree right;

	// Constructeur pour une feuille
	KDTree(double[] point, int depth) {
		this.point = point;
		this.depth = depth;
	}

	public String pointToString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (this.point.length > 0)
			sb.append(this.point[0]);
		for (int i = 1; i < this.point.length; i++)
			sb.append("," + this.point[i]);
		sb.append("]");
		return sb.toString();
	}

	// Question 1

	// <0 si a doit etre range dans le sous-arbre gauche, >=0 si a doit etre
	// range dans le sous-arbre droit
	double difference(double[] a) {
		//throw new Error("Methode double difference(double[] a) a completer (Question 2.1)");

		int d = this.depth%this.point.length; 
		return a[d]-this.point[d];
	}

	// true si a doit etre range dans le sous-arbre droit, false sinon
	boolean compare(double[] a) {
		//throw new Error("Methode boolean compare(double[] a) a completer (Question 2.1)");
	
		if(difference(a)>=0) return true;
		else return false;
	}

	// Question 2

	// insere le point a dans l'arbre tree de telle sorte que la propriete
	// d'arbre kd soit preservee, renvoie la racine du nouvel arbre
	static KDTree insert(KDTree tree, double[] a) {
		//throw new Error("Methode KDTree insert(KDTree tree, double[] a) a completer (Question 2.2)");
	
		if(tree == null)
			return new KDTree(a, 0);
		else return insert(tree, a, tree.depth);
	}
	
	static KDTree insert(KDTree tree, double[] a, int depth) {
		if(tree == null)
			return new KDTree(a, depth);
		if(tree.compare(a))
			tree.right = insert(tree.right, a, depth+1);
		else
			tree.left = insert(tree.left, a, depth+1);
		return tree;
	}

	// Question 3

	// calcule le carre de la distance euclidienne entre deux points
	static double sqDist(double[] a, double[] b) {
		//throw new Error("Methode  double sqDist(double[] a, double[] b) a completer (Question 3.1)");
	
		double d = 0;
		for(int i = 0; i<a.length; i++) {
			d+=(a[i]-b[i])*(a[i]-b[i]);
		}
		return d;
	}

	// renvoie le point le plus proche du point a parmi les points de tree
	// et champion
	static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
		//throw new Error("Methode double[] closestNaiveKDTree tree, double[] a, double[] champion) a completer (Question 3.2)");
		
		if(tree == null ) return champion;
		double d1 = sqDist(a, tree.point);
		double d2 = sqDist(a, champion);
		champion = d1<d2? tree.point:champion;
		champion = closestNaive(tree.left, a, champion);
		champion = closestNaive(tree.right, a, champion);
		return champion;
	}

	// renvoie le point de tree le plus proche du point a
	static double[] closestNaive(KDTree tree, double[] a) {
		//throw new Error("Methode double[] closestNaiveKDTree tree, double[] a) a completer (Question 3.2)");
	
		if(tree == null) return null;
		else return closestNaive(tree, a, tree.point);
	}

	// renvoie le point le plus proche du point a parmi les points de tree
	// et champion
	static double[] closest(KDTree tree, double[] a, double[] champion) {

		if (tree == null)
			return champion;

		// sert a InteractiveClosest pour afficher la progression
		InteractiveClosest.trace(tree.point, champion);

		//throw new Error("Methode double[] closest(KDTree tree, double[] a, double[] champion) a completer (Question 3.3)");
	
		double d = tree.difference(a);
		KDTree first, second;
		if(d<0) {
			first = tree.left;
			second = tree.right;
		}
		else {
			first = tree.right;
			second = tree.left;
		}
		
		champion = closest(first, a, champion);
		
		double dd = sqDist(a, champion);
		
		if(dd>=d*d) {
			if(dd>sqDist(a, tree.point)) champion = tree.point;
			champion = closest(second, a, champion);
		}
		
		return champion;
	}

	static double[] closest(KDTree tree, double[] a) {
		//throw new Error("Methode double[] closest(KDTree tree, double[] a) a completer (Question 3.3)");
	
		if(tree == null)
			return null;
		else
			return closest(tree, a, tree.point);
	}

	// Question 4

	// calcule le nombre de noeuds de l'arbre
	static int size(KDTree tree) {
		//throw new Error("Methode int size(KDTree tree) a completer (Question 4.1)");
	
		if(tree == null) return 0;
		else return 1+size(tree.left)+size(tree.right);
	}

	// calcule la somme des points de l'arbre
	static void sum(KDTree tree, double[] acc) {
		//throw new Error("Methode int sum(KDTree tree, double[] acc) a completer (Question 4.1)");
	
		if(tree == null) return;
		for(int i = 0; i<acc.length; i++) {
			acc[i] += tree.point[i];
		}
		sum(tree.left,acc);
		sum(tree.right,acc);
	}

	// calcule l'isobarycentre des points de l'arbre
	static double[] average(KDTree tree) {
		//throw new Error("Methode double[] average(KDTree tree) a completer (Question 4.1)");
	
		if(tree == null) return null;
		double [] acc = new double[tree.point.length];
		sum(tree, acc);
		int num = size(tree);
		for(int i = 0; i<acc.length; i++) {
			acc[i] = acc[i]/num;
		}
		return acc;
	}

	// elague tree de maniere a ne conserver que
	// maxpoints, renvoie le vecteur de points qui en resulte
	static Vector<double[]> palette(KDTree tree, int maxpoints) {
		//throw new Error("Methode Vector<double[]> palette(KDTree, int) a completer (Question 4.2)");
	
		Vector<double[]> acc = new Vector<double[]>();
		palette(tree, maxpoints, acc);
		return acc;
	}
	
	static int palette(KDTree tree, int maxpoints, Vector<double[]> acc) {
		if (tree == null || maxpoints <= 0)
			return 0;
		if (maxpoints == 1) {
			acc.add(average(tree));
			return 1;
		}
		int mid = (int) Math.round((double) maxpoints * size(tree.left) / size(tree));
		int total = palette(tree.left, mid, acc);
		total += palette(tree.right, maxpoints - mid, acc);
		if (total < maxpoints) {
			acc.add(average(tree));
			total++;
		}
		return total;
	}

}
