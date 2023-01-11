package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (key == null || p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            return p.value;
        }
        if (cmp > 0) {
            return getHelper(key, p.right);
        }
        return getHelper(key, p.left);
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, value);
        }
        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            p.value = value;
        } else if (cmp > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.left = putHelper(key, value, p.left);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keySetHelper(new HashSet<>(), root);
    }

    private Set<K> keySetHelper(Set<K> ks, Node p) {
        if (p == null) {
            return ks;
        }
        ks.add(p.key);
        ks = keySetHelper(ks, p.left);
        ks = keySetHelper(ks, p.right);
        return ks;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
       V v = get(key);
       if (v == null) {
           return null;
       }
       root = removeHelper(key, root);
       size -= 1;
       return v;
    }

    private Node removeHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp > 0) {
            p.right = removeHelper(key, p.right);
        }
        else if (cmp < 0) {
            p.left = removeHelper(key, p.left);
        }
        else {
            if (p.left == null) {
                return p.right;
            }
            if (p.right == null) {
                return p.left;
            }
            Node pred = findPredecessor(p);
            K newk = pred.key;
            V newV = pred.value;
            remove(newk);
            p.key = newk;
            p.value = newV;
        }
        return p;
    }

    /** Return Node P's predecessor (most biggest Node left to p)*/
    private Node findPredecessor(Node p) {
        Node pred = p.left;
        while (pred.right != null) {
            pred = pred.right;
        }
        return pred;
    }
    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V v = get(key);
        if (v == null || !v.equals(value)) {
            return null;
        }
        remove(key);
        return v;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator<>();
    }

    private class BSTMapIterator<K> implements Iterator<K> {
        private Iterator<K> keysIter;
        public BSTMapIterator(){
            keysIter = (Iterator<K>) keySet().iterator();
        }
        @Override
        public boolean hasNext() {
            return keysIter.hasNext();
        }

        @Override
        public K next() {
            return keysIter.next();
        }
    }
}
