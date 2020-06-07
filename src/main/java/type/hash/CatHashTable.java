package type.hash;

import type.lists.CatDoublyLinkedList;

import java.util.HashMap;
import java.util.Map;

public class CatHashTable<K, V> implements BaseCatSet{
    public class CatHashEntry<T> {
        T key;
        T value;
        CatHashEntry next;

        public CatHashEntry() {
            this.key = null;
            this.value = null;
            this.next = null;
        }

        public CatHashEntry(T key, T value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public void remove() {
            this.key = null;
            this.value = null;
            this.next = null;
        }

        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }

    private K key;
    private V value;
    private CatHashEntry[] buckets;
    private int size = 0;
    private static final int BUCKET_SIZE = 16;

    public CatHashTable() {
        this.buckets = new CatHashEntry[BUCKET_SIZE];
        this.size = 0;
    }

    public CatHashTable(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public void put(Object key, Object value) {
        int hash = hash(key);
        final CatHashEntry hashEntry = new CatHashEntry(key, value);
        if(buckets[hash] == null) {
            buckets[hash] = hashEntry;
        }
        else {
            CatHashEntry temp = buckets[hash];
            while(temp.next != null) {
                temp = temp.next;
            }
            temp.next = hashEntry;
        }
        size++;
    }

    @Override
    public boolean remove(Object object) {
        int h = object.hashCode();
        if (h < 0) { h = -h; }
        h = h % buckets.length;
        CatHashEntry current = buckets[h];
        CatHashEntry previous = null;
        while (current != null)
        {
            if (current.value.equals(object))
            {
                if (previous == null) { buckets[h] = current.next; }
                else { previous.next = current.next; }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object object) {
        int index = hash(object.hashCode());
        CatHashEntry current = buckets[index];
        while (current != null) {
            if (current.value.equals(object)) { return true; }
            current = current.next;
        }
        return false;
    }

    public String toString() {
        int bucket = 0;
        StringBuilder hashTableStr = new StringBuilder();
        for (CatHashEntry entry : buckets) {
            if(entry == null) {
                continue;
            }
            hashTableStr.append("\n bucket[")
                    .append(bucket)
                    .append("] = ")
                    .append(entry.toString());
            bucket++;
            CatHashEntry temp = entry.next;
            while(temp != null) {
                hashTableStr.append(" -> ");
                hashTableStr.append(temp.toString());
                temp = temp.next;
            }
        }
        return hashTableStr.toString();
    }

    public void clear() {
        for (int i = 0; i < BUCKET_SIZE && i < size; ++i) {
            System.out.println(buckets[i]);
            buckets[i].remove();
        }
        size = 0;
    }

    @Override
    public Object get(Object object) {
        int hash = hash(object);
        if(buckets[hash] != null) {
            CatHashEntry temp = buckets[hash];
            while( !temp.key.equals(object)
                    && temp.next != null ) {
                temp = temp.next;
            }
            return temp.value;
        }
        return null;
    }

    private int hash(Object object) {
        return Math.abs(object.hashCode() % BUCKET_SIZE);
    }

    public static void main(String[] args) {
        CatHashTable<Integer, Integer> hashTable = new CatHashTable<>();
        hashTable.put(1, 0);
        hashTable.put(32, 22);
        hashTable.put(1, 2);
        hashTable.put(2, 4);
        System.out.println(hashTable.toString());

        Map<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 0);
        hashMap.put(32, 22);
        hashMap.put(1, 2);
        hashMap.put(2, 4);
        System.out.println(hashMap.toString());
    }
}
