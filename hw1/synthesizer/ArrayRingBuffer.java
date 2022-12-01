package synthesizer;
import java.util.Iterator;


public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        //       first, last, and fillCount should all be set to 0.
        first = 0;
        last = 0;
        fillCount = 0;
        //       this.capacity should be set appropriately. Note that the local variable
        this.capacity = capacity;
    }

    private int plusone(int index) {
        return Math.floorMod(index + 1, capacity);
    }
    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        }
        rb[last] = x;
        last = plusone(last);
        fillCount += 1;
    }


    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        T item = peek();
        rb[first] = null;
        first = plusone(first);
        fillCount -= 1;
        return item;
    }
    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        return rb[first];
    }


    @Override
    public Iterator<T> iterator() {
        return new ArrayRingIter();
    }

    private class ArrayRingIter implements Iterator<T> {
        private int pos = first;
        @Override
        public boolean hasNext() {
            return first != last;
        }

        @Override
        public T next() {
            T item = rb[first];
            pos = plusone(pos);
            return item;
        }
    }
}
