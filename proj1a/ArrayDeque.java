public class ArrayDeque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;
    private static final int RFACTOR = 2;
    private double usageRatio;

    /** Create an empty array deque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
        usageRatio = size / items.length;
    }
    /** Adds an item of the type T to the back of the deque. */
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        System.arraycopy(items, 0, temp, 0, nextLast);
        System.arraycopy(items, nextLast, temp, nextLast + size, size - nextLast);
        items = temp;
        nextFirst = nextFirst + size;
    }

    private void shrink() {
        usageRatio = (float) size / items.length;
        if (items.length > 8 && usageRatio < 0.25) {
            int capacity = items.length / RFACTOR;
            T[] temp = (T[]) new Object[capacity];
            System.arraycopy(items, 0, temp, 0, nextLast);
            System.arraycopy(items, nextFirst, temp, nextFirst - capacity, size + 1 - nextLast);
            items = temp;
            nextFirst = nextFirst -capacity;
        }
    }


    public void addLast(T i) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextLast] = i;
        size += 1;
        if (nextFirst + size + 1 >= items.length) {
            nextLast = nextFirst + size + 1 - items.length;
        } else {
            nextLast = nextFirst + size + 1;
        }
    }
    /** Adds an item of the type T to the front of the deque. */
    public void addFirst(T i) {
        items[nextFirst] = i;
        size += 1;
        if (nextLast - size - 1 >= 0) {
            nextFirst = nextLast - size - 1;
        } else {
            nextFirst = items.length + nextLast - size - 1;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int first = nextFirst + 1;
        for (int i = first; i < items.length; i++) {
            if (items[i] == null) {
                return;
            }
            System.out.print(items[i] + " ");
        }
        for (int i = 0; i < nextLast; i ++) {
            System.out.print(items[i] + " ");
        }
    }

    /** Removes and returns the item at the front of the deque. If no such item exists returns null. */
    public T removeFirst() {
        if (nextFirst + 1 < items.length) {
            nextFirst = nextFirst + 1;
        } else {
            nextFirst = 0;
        }
        T first = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        shrink();
        return first;
    }

    /** Removes and returns the item at the back of the deque. If no such item exists returns null. */
    public T removeLast() {
        if (nextLast - 1 >= 0) {
            nextLast = nextLast - 1;
        } else {
            nextLast = nextFirst + size;
        }
        T last = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        shrink();
        return last;
    }

    /** Gets the item at the given index where 0 is the front, 1 is the next item and so forth, if no such item exists return null. */
    public T get(int index) {
        int sofal = 0;
        for (int i = nextFirst + 1; i < items.length; i++) {
            if (sofal == index || items[i] == null) {
                return items[i];
            }
            sofal += 1;
        }
        for (int i = 0; i < nextLast; i++) {
            if (sofal == index || items[i] == null) {
                return items[i];
            }
            sofal += 1;
        }
        return null;
    }


    public static void main(String[] args) {
        //ArrayDeque<String> A = new ArrayDeque<>();
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addLast("a");
        deque.addLast("b");
        deque.addFirst("c");
        deque.addLast("d");
        deque.addLast("e");
        deque.addFirst("f");
        deque.addLast("g");
        deque.addLast("h");
        deque.addLast("z");
        deque.removeFirst();
        deque.removeLast();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeLast();
        deque.removeLast();
        deque.printDeque();
    }
}