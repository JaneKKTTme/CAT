package types.hash;

import types.lists.CatDoublyLinkedList;

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

        public String toString() {
            return key + ": " + value;
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
        int bucket = hash(key);
        CatHashEntry list = buckets[bucket];
        while (list != null) {
            if (list.key.equals(key))
                break;
            list = list.next;
        }
        if (list != null) {
            list.value = value;
        }
        else {
            if (size >= 0.75*buckets.length) {
                resize();
            }
            CatHashEntry newNode = new CatHashEntry();
            newNode.key = key;
            newNode.value = value;
            newNode.next = buckets[bucket];
            buckets[bucket] = newNode;
            size++;
        }
    }

    private void resize() {
        CatHashEntry[] newtable = new CatHashEntry[buckets.length*2];
        for (int i = 0; i < buckets.length; i++) {
            CatHashEntry list = buckets[i];
            while (list != null) {
                CatHashEntry next = list.next;
                int hash = (Math.abs(list.key.hashCode())) % newtable.length;
                list.next = newtable[hash];
                newtable[hash] = list;
                list = next;
            }
        }
        buckets = newtable;
    }

    @Override
    public void remove(Object key) {
        int bucket = hash(key);
        if (buckets[bucket] == null) {
            return;
        }
        if (buckets[bucket].key.equals(key)) {
            buckets[bucket] = buckets[bucket].next;
            size--;
            return;
        }
        CatHashEntry prev = buckets[bucket];
        CatHashEntry curr = prev.next;
        while (curr != null && ! curr.key.equals(key)) {
            curr = curr.next;
            prev = curr;
        }
        if (curr != null) {
            prev.next = curr.next;
            size--;
        }
    }

    @Override
    public boolean contains(Object key) {
        int index = hash(key.hashCode());
        CatHashEntry current = buckets[index];
        while (current != null) {
            if (current.value.equals(key)) { return true; }
            current = current.next;
        }
        return false;
    }

    public String toString() {
        String output = "{ ";
        for (int i = 0; i < buckets.length; i++) {
            CatHashEntry list = buckets[i];
            while (list != null) {
                output += list.key + " : " + list.value;
                if (list.next == null && i < size-1) {
                    output += ", ";
                }
                list = list.next;
            }
            if (i < size && i > 0) {
                output += ", ";
            }
        }
        output += " }";
        return output;
    }

    public void clear() {
        for (int i = 0; i < BUCKET_SIZE && i < size; ++i) {
            remove(buckets[i]);
        }
        size = 0;
    }

    @Override
    public Object get(Object key) {
        int lookupPosition = hash(key);
        CatHashEntry retrievedNode = buckets[lookupPosition];
        while (retrievedNode != null) {
            if (retrievedNode.key.equals(key)) {
                return retrievedNode.value;
            } else {
                retrievedNode = retrievedNode.next;
            }
        }
        return null;
    }

    private int hash(Object object) {
        return Math.abs(object.hashCode() % BUCKET_SIZE);
    }

    public static void main(String[] args) {
        CatHashTable<Integer, Integer> hashTable = new CatHashTable<>();
        hashTable.put(1, 2);
        hashTable.put(3, 4);
        System.out.println(hashTable.toString());
        //hashTable.remove(1);
        System.out.println(hashTable.get(1));

        Map<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 2);
        hashMap.put(3, 4);
        //hashMap.remove(1);
        System.out.println("\n\n" + hashMap.get(3));
    }
}
