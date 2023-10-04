package dataStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    private int size;

    public T getHead() {
        return head.data;
    }

    public T getTail() {
        return tail.data;
    }

    public int size() {
        return size;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);

        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        }

        size++;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);

        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
        }
        tail = newNode;

        size++;
    }

    public void add(T data, int index) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Invalid Index");
        }

        Node<T> newNode = new Node<>(data);

        if (index == 0) {
            addFirst(data);
        } else if (index == size) {
            addLast(data);
        } else {
            Node<T> current = getNode(index - 1);

            newNode.next = current.next;
            current.next.prev = newNode;

            newNode.prev = current;
            current.next = newNode;

            size++;
        }
    }

    public void addAll(Collection<T> collection) {
        collection.forEach(this::addLast);
    }

    public void addAll(Collection<T> collection, int index) {
        for (T data : collection.stream().toList()) {
            add(data, index++);
        }
    }

    public T removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        } else {
            T data = tail.data;

            if (head == tail) {
                head = null;
                tail = null;
            } else {
                tail = tail.prev;
                tail.next = null;
            }

            size--;
            return data;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        } else {
            T data = head.data;

            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = head.next;
                head.prev = null;
            }

            size--;
            return data;
        }
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Invalid index");
        } else {
            if (index == 0) {
                return removeFirst();
            } else if (index == size - 1) {
                return removeLast();
            } else {
                Node<T> node = getNode(index);

                node.prev.next = node.next;
                node.next.prev = node.prev;

                size--;
                return node.data;
            }
        }
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new NoSuchElementException("Invalid index");
        } else {
            Node<T> current;

            if (index <= size / 2) {
                current = head;
                for (int currentIndex = 0; currentIndex < index; currentIndex++) {
                    current = current.next;
                }
            } else {
                current = tail;
                for (int currentIndex = size - 1; currentIndex > index; currentIndex--) {
                    current = current.prev;
                }
            }

            return current;
        }
    }

    private T get(int index) {
        return getNode(index).data;
    }

    public ArrayList<T> toArrayList() {
        ArrayList<T> list = new ArrayList<>();
        Node<T> current = head;

        while (current != null) {
            list.add(current.data);
            current = current.next;
        }

        return list;
    }


    public int indexOf(T data) {
        int index = 0;
        if (data == null) {
            for (Node<T> node = head; node != null; node = node.next, index++) {
                if (node.data == null) {
                    return index;
                }
            }
        } else {
            for (Node<T> node = head; node != null; node = node.next, index++) {
                if (data.equals(node.data)) {
                    return index;
                }
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(T data) {
        return indexOf(data) != -1;
    }

    public void clear() {
        while (!isEmpty()) {
            removeLast();
        }
    }

    public void display() {
        if (size == 0) {
            System.out.println("List is empty");
        }
        toArrayList().forEach(System.out::println);
    }

    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
        }
    }

}

