public class LinkedListDeque<T> {
    private class Node<T> {
        public T item;
        public Node prev;
        public Node next;
        /** Node constructor */
        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    /** The first item (if it exists) is at sentinel.next.
     *  The last item is at sentinel.prev.*/
    private Node sentinel;
    private int size;

    /** Create an empty linked list deque*/
    public LinkedListDeque() {
        sentinel = new Node(null,null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;

        size = 0;
    }
    public LinkedListDeque(T item) {
        sentinel = new Node(null, null, null);
        sentinel.next = new Node(item, sentinel, sentinel);
        sentinel.prev = sentinel.next;
        size += 1;
    }
    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        sentinel.next = new Node(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        sentinel.prev = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }
    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }
    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }
    /** Prints the items in the deque from first to last, separated by a space*/

    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
    }
    /** Removes and returns the item at the front of the deque. If no such item exists return null. */
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        T first = (T) sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return first;
    }

    /** Removes and returns the item at the back of the deque. if no such item exists, returns null. */
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }
        T last = (T) sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return last;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item and so forth. If no such item exists return null. */
    public T get(int index) {
        Node p = sentinel;
        for (int i = 0; i <= index; i++) {
            p = p.next;
            if (p == sentinel) {
                break;
            }
        }
        return (T) p.item;
    }

    /** get item using recursion. */
    public T getRecursive(int index) {
        return (T) getRecursive(index, sentinel.next);
    }

    private T getRecursive(int index, Node L) {
        if (index == 0 || L == sentinel) {
            return (T) L.item;
        }
        return (T) getRecursive(index - 1, L.next);
    }

}