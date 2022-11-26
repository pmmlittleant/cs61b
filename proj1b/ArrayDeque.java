

/** Implement ArrayDeque API*/
public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;
    private static final int RFACTOR = 2;
    private double usageRatio;
    private static final int SIZE = 8;
    private static final int F = 3;
    private static final int L = 4;
    private static final int BIGSIZE = 16;

    /**
     * Create an empty array deque.
     */
    public ArrayDeque() {
        items = (T[]) new Object[SIZE];
        size = 0;
        nextFirst = F;
        nextLast = L;
        usageRatio = size / items.length;
    }

    /**
     * Adds an item of the type T to the back of the deque.
     */

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
        if (items.length >= BIGSIZE && usageRatio < 0.25) {
            int capacity = items.length / RFACTOR;
            T[] temp = (T[]) new Object[capacity];
            int front = front();
            int back = back();
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

    @Override
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

    /**
     * Adds an item of the type T to the front of the deque.
     */
    @Override
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

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        int first = nextFirst + 1;
        for (int i = first; i < items.length; i++) {
            if (items[i] == null) {
                return;
            }
            System.out.print(items[i] + " ");
        }
        for (int i = 0; i < nextLast; i++) {
            System.out.print(items[i] + " ");
        }
    }

    private int front() {
        if (nextFirst + 1 == items.length) {
            return 0;
        } else {
            return nextFirst + 1;
        }
    }

    private int back() {
        if (nextLast - 1 >= 0) {
            return nextLast - 1;
        } else {
            return items.length - 1;
        }
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists returns null.
     */
    @Override
    public T removeFirst() {
        int front = front();
        if (items[front] == null) {
            return null;
        }
        T first = items[front];
        items[front] = null;
        size -= 1;
        nextFirst = front;
        shrink();
        return first;
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists returns null.
     */
    @Override
    public T removeLast() {
        int back = back();
        if (items[back] == null) {
            return null;
        }
        T i = items[back];
        items[back] = null;
        size -= 1;
        nextLast = back;
        shrink();
        return i;
    }

    /**
     * Gets the item at the given index where 0 is the front, 1 is the next item and so forth,
     * if no such item exists return null.
     */
    @Override
    public T get(int index) {
        if (index < size) {
            int front = front();
            int back = back();
            if (front < back) {
                for (int i = front; i <= back; i++) {
                    if (index == 0) {
                        return items[i];
                    }
                    index -= 1;
                }
            } else {
                for (int i = front; i < items.length; i++) {
                    if (index == 0) {
                        return items[i];
                    }
                    index -= 1;
                }
                for (int i = 0; i <= back; i++) {
                    if (index == 0) {
                        return items[i];
                    }
                    index -= 1;
                }

            }
        }
        return null;
    }
}

