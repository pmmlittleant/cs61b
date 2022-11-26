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
        nextFirst = 3;
        nextLast = 4;
        usageRatio = size / items.length;
    }
    /** Adds an item of the type T to the back of the deque. */

    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        System.arraycopy(items, 0, temp, 0, nextLast);
        System.arraycopy(items, nextLast, temp, nextLast + size, size - nextLast);
        if (nextLast != 0) {
            nextFirst = nextFirst + size;
        }
        items = temp;
    }

    /*
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
    */

    public void addLast(T i) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextLast] = i;
        if (nextLast + 1 > items.length - 1) {
            nextLast = 0;
        } else {
            nextLast += 1;
        }
        size += 1;
    }
    /** Adds an item of the type T to the front of the deque. */
    public void addFirst(T i) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextFirst] = i;
        if (nextFirst - 1 < 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst -= 1;
        }
        size += 1;
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
        int front;
        if (nextFirst + 1 == items.length) {
            front = 0;
        } else {
            front = nextFirst + 1;
        }
        if (items[front] == null) {
            return null;
        }
        T first = items[front];
        items[front] = null;
        size -= 1;
        nextFirst = front;
        return first;
    }
    /** Removes and returns the item at the back of the deque. If no such item exists returns null. */
    public T removeLast() {
        int back;
        if (nextLast - 1 >= 0) {
            back = nextLast - 1;
        } else {
            back = items.length - 1;
        }
        if (items[back] == null) {
            return null;
        }
        T i = items[back];
        items[back] = null;
        size -= 1;
        nextLast = back;
        return i;
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