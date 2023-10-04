package dataStructures;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HashMap<K, V> {
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private Entry<K, V>[] buckets;
    private int capacity = 16;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public HashMap() {
        this.buckets = (Entry<K, V>[]) new Entry[capacity];
    }

    public V get(K key) {
        int bucketIndex = (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
        if (bucketIndex >= buckets.length) {
            return null;
        }
        Entry<K, V> currentEntry = buckets[bucketIndex];

        while (currentEntry != null) {
            if (currentEntry.key.equals(key)) {
                return currentEntry.value;
            }
            currentEntry = currentEntry.next;
        }

        return null;
    }

    public void put(K key, V value) {
        resizeIfNeeded();

        Entry<K, V> newEntry = new Entry<>(key, value);
        int bucketIndex = Math.abs(key.hashCode()) % capacity;
        Entry<K, V> currentEntry = (bucketIndex >= buckets.length) ? null : buckets[bucketIndex];

        if (currentEntry == null) {
            buckets[bucketIndex] = newEntry;
        } else {
            while (currentEntry.next != null) {
                if (currentEntry.key.equals(key)) {
                    currentEntry.value = value;
                    return;
                }
                currentEntry = currentEntry.next;
            }
            currentEntry.next = newEntry;
        }

        size++;
    }

    public boolean putIfAbsent(K key, V value) {
        if (containsKey(key)) {
            return false;
        }

        resizeIfNeeded();

        Entry<K, V> newEntry = new Entry<>(key, value);
        int bucketIndex = Math.abs(key.hashCode()) % capacity;
        Entry<K, V> currentEntry = buckets[bucketIndex];

        if (currentEntry == null) {
            buckets[bucketIndex] = newEntry;
        } else {
            while (currentEntry.next != null) {
                currentEntry = currentEntry.next;
            }
            currentEntry.next = newEntry;
        }

        size++;
        return true;
    }

    public final void putAll(Entry<K, V>[] entries) {
        for (Entry<K, V> entry : entries) {
            put(entry.key, entry.value);
        }
    }

    public V compute(K key, BiFunction<K, V, V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = get(key);
        V newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            // delete mapping
            if (oldValue != null || containsKey(key)) {
                remove(key);
            }
            return null;
        } else {
            put(key, newValue);
            return newValue;
        }
    }

    public V computeIfPresent(K key, BiFunction<K, V, V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = get(key);
        if (oldValue != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                put(key, newValue);
                return newValue;
            } else {
                remove(key);
            }
        }
        return null;
    }

    public V computeIfAbsent(K key, Function<K, V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        if (containsKey(key)) {
            return null;
        }

        resizeIfNeeded();

        V newValue = mappingFunction.apply(key);
        put(key, newValue);
        return newValue;
    }


    public V replace(K key, V newValue) {
        V oldValue;
        for (Entry<K, V> entry : buckets) {
            while (entry != null) {
                if (entry.key.equals(key)) {
                    oldValue = entry.value;
                    entry.value = newValue;
                    return oldValue;
                }
                entry = entry.next;
            }
        }

        return null;
    }

    public V remove(K key) {
        int bucketIndex = (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
        Entry<K, V> currentEntry = buckets[bucketIndex];

        if (currentEntry.key.equals(key)) {
            V value = buckets[0].value;
            buckets[bucketIndex] = null;
            size--;
            return value;
        } else {
            while (currentEntry.next != null) {
                if (currentEntry.next.key.equals(key)) {
                    V value = currentEntry.next.value;
                    if (currentEntry.next.next == null) {
                        currentEntry.next = null;
                    } else {
                        currentEntry.next = currentEntry.next.next;
                    }
                    size--;
                    return value;
                }
                currentEntry = currentEntry.next;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public void removeAll() {
        size = 0;
        capacity = 16;
        buckets = (Entry<K, V>[]) new Entry[16];
    }

    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (Entry<K, V> entry : buckets) {
            Entry<K, V> currentEntry = entry;
            while (currentEntry != null) {
                set.add(currentEntry.key);
                currentEntry = currentEntry.next;
            }
        }

        return set;
    }

    public boolean containsKey(K key) {
        int bucketIndex = (key == null) ? 0 : Math.abs(key.hashCode()) % buckets.length;
        Entry<K, V> currentEntry = buckets[bucketIndex];

        while (currentEntry != null) {
            if (currentEntry.key.equals(key)) {
                return true;
            }
            currentEntry = currentEntry.next;
        }

        return false;
    }


    public boolean containsValue(V value) {
        for (Entry<K, V> entry : buckets) {
            Entry<K, V> currentEntry = entry;
            while (currentEntry != null) {
                if (currentEntry.value.equals(value)) {
                    return true;
                }
                currentEntry = currentEntry.next;
            }
        }

        return false;
    }

    public void display() {
        for (Entry<K, V> entry : buckets) {
            Entry<K, V> currentEntry = entry;
            while (currentEntry != null) {
                System.out.print(currentEntry.key + " -> " + currentEntry.value + " , ");
                currentEntry = currentEntry.next;
            }
            System.out.println();
        }
    }

    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        double loadFactor = (double) size / capacity;
        if (loadFactor > LOAD_FACTOR_THRESHOLD) {
            capacity *= 2;
            Entry<K, V>[] oldBuckets = buckets;
            buckets = (Entry<K, V>[]) new Entry[capacity];

            putAll(oldBuckets);
        }
    }

    public static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }


        @Override
        public boolean equals(Object o) {
            if (o == this) return true;

            return o instanceof Entry<?, ?> e && Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}

