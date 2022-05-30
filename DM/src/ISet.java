class AVL {
    int key;
    boolean onleft;

    AVL left, right;

    static AVL getPrev(AVL t, int i) {
        if(t==null) 
        	return null;
        if(t.key>i) 
        	return getPrev(t.left, i);
        AVL a = getPrev(t.right, i);
        if(a != null)
        	return a;
        else
        	return t;
    }

    // IMPLEMENTATION DES ARBRES AVL VUE EN COURS


    int height;

    AVL(int point, boolean type, AVL left, AVL right) {
        onleft = type;
        this.key = point;
        this.left = left;
        this.right = right;
        this.height = 1 + Math.max(height(left), height(right));
    }

    static AVL put(AVL b, int i, boolean onleft) {
        if (b == null)
            return new AVL(i, onleft, null, null);

        if (i < b.key)
            b.left = put(b.left, i, onleft);
        else if (i > b.key)
            b.right = put(b.right, i, onleft);
        else {
            b.onleft = onleft;
        }
        return balance(b);
    }

    static AVL removeRoot(AVL b) {

        if (b.right == null)
            return b.left;

        b.right = removeMin(b.right, b);

        return balance(b);
    }

    static void copynode(AVL dst, AVL src) {
        dst.key = src.key;
        dst.onleft = src.onleft;
    }

    static AVL remove(AVL b, int x) {
        if (b == null)
            return null;
        if (x < b.key)
            b.left = remove(b.left, x);
        else if (x > b.key)
            b.right = remove(b.right, x);
        else
            return removeRoot(b);
        return balance(b);
    }

    static AVL removeMin(AVL b, AVL dst) {
        if (b.left == null) {
            copynode(dst, b);
            return b.right;
        }
        b.left = removeMin(b.left, dst);
        return balance(b);
    }

    static int height(AVL a) {
        return (a == null) ? 0 : a.height;
    }

    private static AVL rotateRight(AVL t) {
        assert t != null && t.left != null;
        AVL l = t.left;
        t.left = l.right;
        l.right = t;
        t.height = 1 + Math.max(height(t.left), height(t.right));
        l.height = 1 + Math.max(height(l.left), height(l.right));
        return l;
    }

    private static AVL rotateLeft(AVL t) {
        assert t != null && t.right != null;
        AVL r = t.right;
        t.right = r.left;
        r.left = t;
        t.height = 1 + Math.max(height(t.left), height(t.right));
        r.height = 1 + Math.max(height(r.left), height(r.right));
        return r;
    }

    private static AVL balance(AVL t) {
        assert t != null;
        AVL l = t.left, r = t.right;
        int hl = height(l), hr = height(r);
        if (hl > hr + 1) {
            AVL ll = l.left, lr = l.right;
            if (height(ll) >= height(lr))
                return rotateRight(t);
            else {
                t.left = rotateLeft(t.left);
                return rotateRight(t);
            }
        } else if (hr > hl + 1) {
            AVL rl = r.left, rr = r.right;
            if (height(rr) >= height(rl))
                return rotateLeft(t);
            else {
                t.right = rotateRight(t.right);
                return rotateLeft(t);
            }
        } else {
            t.height = 1 + Math.max(hl, hr);
            return t;
        }
    }

    static boolean isNormal(AVL b, boolean left, boolean right) {
        if (b == null)
            return left != right;
        return isNormal(b.left, left, !b.onleft) && isNormal(b.right, !b.onleft, right);
    }

    static boolean isNormal(AVL b) {
        return isNormal(b, true, false);
    }
}

public class ISet {
    private AVL root;

    ISet() {}

    // Représente l'intervalle [x, y[
    ISet(int x, int y) {
        root = null;
        root = AVL.put(root, x, true);
        root = AVL.put(root, y, false);
    }

    boolean contains(int i) {
        AVL a = AVL.getPrev(this.root, i);
        return (a.onleft && a!=null);
    }

    void add(int x) {
        if(contains(x)) return; // x is in the set, no need to add
        // no element less or equal to x in AVL, or such element on the right
        boolean bef = contains(x-1);
        boolean aft = contains(x+1);
        if(bef)
        	this.root = AVL.remove(this.root, x);
        else
        	this.root = AVL.put(this.root, x, true);
        if(aft)
        	this.root = AVL.remove(this.root, x+1);
        else
        	this.root = AVL.put(this.root, x+1, false);
    }


    void union(ISet a) {
        // Le code n'est pas demandé mais vous pouvez essayer vos idées ici
    }



}
