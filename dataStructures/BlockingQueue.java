package dataStructures;

public class BlockingQueue<T> {
    private final Queue<T> queue = new Queue<>();
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue(T data) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // Block if the queue is full
        }
        queue.enqueue(data);
        notifyAll(); // Wake up any waiting consumers
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // Block if the queue is empty
        }
        T item = queue.dequeue();
        notifyAll(); // Wake up any waiting producers
        return item;
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
