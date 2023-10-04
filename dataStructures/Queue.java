package dataStructures;

public class Queue<T> {
    private final LinkedList<T> list;

    public Queue() {
        list = new LinkedList<>();
    }

    public Queue(LinkedList<T> list) {
        this.list = list;
    }

    public void enqueue(T data) {
        list.addFirst(data);
    }

    public T dequeue() {
        return list.removeLast();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
