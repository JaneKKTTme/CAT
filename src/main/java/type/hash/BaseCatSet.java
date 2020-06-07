package type.hash;

public interface BaseCatSet {

    void put(Object key, Object value);

    boolean remove(Object object);

    Object get(Object object);

    boolean contains(Object object);

    int size();
}
