import java.util.HashSet;

class USet {

    private boolean finite;
    private HashSet<Integer> set;

    USet() {
        finite = true;
        set = new HashSet<Integer>();
    }

    boolean contains(int i) {
    	// if x is finite, set is of x (set contains i, x contains i)
    	// if not, set is complement of x (set contains i, x does not contains i).
        if(this.finite) return this.set.contains(i);
        else return !this.contains(i);
    }

    void add(int i) {
        // if x is finite, add i to set
    	// if not, remove i from set
    	if(this.contains(i)) return;
    	else {
    		if(this.finite) this.set.add(i);
    		else this.set.remove(i);
    	}
    }

    void union(USet a) {
        if(this.finite==true && a.finite==true) {
        	this.set.addAll(a.set);
        }
        else if(this.finite==true && a.finite==false) {
        	this.finite = false;
        	a.set.removeAll(this.set);
        	this.set = a.set;
        }
        else if(this.finite==false && a.finite==true) {
        	this.set.removeAll(a.set);
        }
        else {
        	this.set.retainAll(a.set);
        }
    }

    void complement() {
        this.finite = !this.finite;
    }

}
