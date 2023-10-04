package dataStructures;

import java.util.List;

public class Stack<T> {
    private final LinkedList<T> list;

    public Stack() {
        list = new dataStructures.LinkedList<>();
    }

    public Stack(dataStructures.LinkedList<T> list) {
        this.list = list;
    }

    public void push(T data) {
        list.addFirst(data);
    }

    public T pop() {
        return list.removeFirst();
    }

    public T peek() {
        return list.getHead();
    }

    public int search(T data) {
        return list.indexOf(data);
    }

    void clear() {
        list.clear();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<T> toArray() {
        List<T> array = list.toArrayList();
        list.clear();
        return array;
    }

    public int size() {
        return list.size();
    }

}
