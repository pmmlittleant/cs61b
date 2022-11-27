/** Implement ArrayDeque API*/
public class ArrayDeque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;
    private static final int RFACTOR = 2;
    private double usageRatio;
    private final int CAPACITY = 8;

    /**
     * Create an empty array deque.
     */
    public ArrayDeque() {
        items = (T[]) new Object[CAPACITY];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
        usageRatio = size / items.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }

    /**
     * Adds an item of the type T to the back of the deque.
     */

    /** Get the last index. */
    private int minusOne(int index) {
        return Math.floorMod(index - 1, items.length);
    }

    private int plusOne(int index) {
        return Math.floorMod(index + 1, items.length);
    }


    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        System.arraycopy(items, 0, temp, 0, nextLast);
        System.arraycopy(items, nextLast, temp, nextLast + size, size - nextLast);
        if (nextLast != 0) {
            nextFirst = nextFirst + size;
        }
        items = temp;
    }

    private void shrink() {
        usageRatio = (float) size / items.length;
        if (items.length >= 16 && usageRatio < 0.25) {
            int capacity = items.length / RFACTOR;
            T[] temp = (T[]) new Object[capacity];
            int front = plusOne(nextFirst);
            int back = minusOne(nextLast);
            if (front < back) {
                System.arraycopy(items, front, temp, 0, size);
            } else {
                System.arraycopy(items, front, temp, 0, items.length - front);
                System.arraycopy(items, 0, temp, items.length - front, back + 1);
            }
            items = temp;
            nextFirst = items.length - 1;
            nextLast = size;
        }
    }
    /**
     * Adds an item of the type T to the front of the deque.
     */
    public void addFirst(T i) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextFirst] = i;
        nextFirst = minusOne(nextFirst);
        size += 1;
    }

    public void addLast(T i) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextLast] = i;
        nextLast = plusOne(nextLast);
        size += 1;
    }

    public void printDeque() {
        int first = plusOne(nextFirst);
        for (int i = first; i != nextLast; i = plusOne(i)) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists returns null.
     */
    private T getFirst() {
        return items[plusOne(nextFirst)];
    }
    public T removeFirst() {
        T first = getFirst();
        if (first == null) {
            return null;
        }
        nextFirst = plusOne(nextFirst);
        items[nextFirst] = null;
        size -= 1;
        shrink();
        return first;
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists returns null.
     */
    private T getLast() {
        return items[minusOne(nextLast)];
    }
    public T removeLast() {
        T back = getLast();
        if (back == null) {
            return null;
        }
        nextLast = minusOne(nextLast);
        items[nextLast] = null;
        size -= 1;
        shrink();
        return back;
    }

    /**
     * Gets the item at the given index where 0 is the front, 1 is the next item and so forth,
     * if no such item exists return null.
     */
    public T get(int index) {
        if (index < 0 || index >= size || isEmpty()) {
            return null;
        }
        index = Math.floorMod(plusOne(nextFirst) + index, items.length);
        return items[index];
    }
}
