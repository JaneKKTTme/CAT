import junit.framework.TestCase;
import types.lists.CatDoublyLinkedList;

import java.util.*;

public class CatDoublyLinkedListTest extends TestCase {
    private final List<Object> getNodes = new ArrayList<>();

    protected void setData() {
        getNodes.add(-1);
        getNodes.add(0.4);
        getNodes.add(9.9);
        getNodes.add(16);
        getNodes.add(-7.8);
    }

    public void testAddBack() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList<Object>();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        String expected = null;
        if (getNodes.size() != 0) {
            expected = "";
            for (Object i : getNodes) {
                expected += i + " ";
            }
        }
        assertEquals(expected, catDoublyLinkedList.toString());
    }

    public void testAddFront() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = getNodes.size() - 1; i >= 0 ; i--) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        String expected = null;
        if (getNodes.size() != 0) {
            expected = "";
            for (Object i : getNodes) {
                expected += i + " ";
            }
        }
        assertEquals(expected, catDoublyLinkedList.toString());
    }

    public void testContains() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        assertEquals(getNodes.contains(0), catDoublyLinkedList.contains(0));
        assertEquals(getNodes.contains(-1), catDoublyLinkedList.contains(-1));
        assertEquals(getNodes.contains(0.4), catDoublyLinkedList.contains(0.4));
    }

    public void testFind() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        assertEquals(getNodes.get(0), catDoublyLinkedList.get(0));
        assertEquals(getNodes.get(getNodes.size() - 1), catDoublyLinkedList.get(4));
        assertEquals(getNodes.get(2), catDoublyLinkedList.get(2));
    }

    public void testGet() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        assertEquals(getNodes.get(0), catDoublyLinkedList.get(0));
        assertEquals(getNodes.get(2), catDoublyLinkedList.get(2));
        assertEquals(getNodes.get(4), catDoublyLinkedList.get(4));
    }

    public void testGetHead() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        assertEquals(getNodes.get(0), catDoublyLinkedList.getHead());
    }

    public void testGetTail() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        assertEquals(getNodes.get(getNodes.size() - 1), catDoublyLinkedList.getTail());
    }

    public void testInsert() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        catDoublyLinkedList.insert(0, 0);
        getNodes.add(1, 0);
        catDoublyLinkedList.insert(1, 100);
        getNodes.add(2, 100);
        catDoublyLinkedList.insert(3, 3);
        getNodes.add(4, 3);
        String expected = "";
        if (getNodes.size() != 0) {
            expected = "";
            for (Object i : getNodes) {
                expected += i + " ";
            }
        }
        assertEquals(expected, catDoublyLinkedList.toString());
        assertEquals(expected, catDoublyLinkedList.toString());
        assertEquals(expected, catDoublyLinkedList.toString());
    }

    public void testIsEmpty() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        assertEquals(catDoublyLinkedList.isEmpty(), getNodes.isEmpty());
    }

    public void testRemoveBackNode() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        catDoublyLinkedList.removeBackNode();
        getNodes.remove(getNodes.size() - 1);
        String expected = "";
        if (getNodes.size() != 0) {
            expected = "";
            for (Object i : getNodes) {
                expected += i + " ";
            }
        }
        assertEquals(expected, catDoublyLinkedList.toString());
    }

    public void testRemoveFrontNode() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        catDoublyLinkedList.removeFrontNode();
        getNodes.remove(0);
        String expected = "";
        if (getNodes.size() != 0) {
            expected = "";
            for (Object i : getNodes) {
                expected += i + " ";
            }
        }
        assertEquals(expected, catDoublyLinkedList.toString());
    }

    public void testRemoveNodeByIndex() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        catDoublyLinkedList.removeNodeByIndex(1);
        getNodes.remove(1);
        String expected = "";
        if (getNodes.size() != 0) {
            expected = "";
            for (Object i : getNodes) {
                expected += i + " ";
            }
        }
        assertEquals(expected, catDoublyLinkedList.toString());
    }

    public void testSet() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        catDoublyLinkedList.set(0, 0);
        getNodes.set(0, 0);
        String expected = "";
        if (getNodes.size() != 0) {
            expected = "";
            for (Object i : getNodes) {
                expected += i + " ";
            }
        }
        assertEquals(expected, catDoublyLinkedList.toString());
    }

    public void testSize() {
        setData();
        CatDoublyLinkedList catDoublyLinkedList = new CatDoublyLinkedList();
        for (int i = 0; i < getNodes.size(); i++) {
            catDoublyLinkedList.add(getNodes.get(i));
        }
        assertEquals(catDoublyLinkedList.size(), getNodes.size());
    }
}
