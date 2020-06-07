package type.lists;

public class CatDoublyLinkedList<T> implements BaseCatList{
    public class CatNode<T> {
        T value;
        CatNode previous;
        CatNode next;

        public CatNode() {
            this.value = null;
            this.previous = null;
            this.next = null;
        }

        public CatNode(T value) {
            this.value = value;
            this.previous = null;
            this.next = null;
        }

        public CatNode(T value, CatNode previous, CatNode next) {
            this.value = value;
            this.previous = previous;
            this.next = next;
        }
    }

    private  CatNode head;
    private CatNode tail;
    private int totalQuantity;

    public CatDoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.totalQuantity = 0;
    }

    public void addBack(T value) {
        CatNode catNode = new CatNode(value);
        if(head == null) {
            head = tail = catNode;
            head.previous = null;
            tail.next = null;
        }
        else {
            tail.next = catNode;
            catNode.previous = tail;
            tail = catNode;
            tail.next = null;
        }
        totalQuantity++;
    }

    public void addFront(T value) {
        CatNode catNode = new CatNode(value, null, head);
        head = catNode;
        if (tail == null) {
            tail = head;
        }
        totalQuantity++;
    }

    public boolean contains(T value) {
        CatNode catNode = head;
        while (catNode != null) {
            if (catNode.value.equals(value)) {
                return true;
            }
            catNode = catNode.next;
        }

        return false;
    }

    public CatNode find(int index) {
        CatNode catNode = null;
        if (index < totalQuantity / 2) {
            catNode = head;
            for (int i = 0; i < index; i++) {
                catNode = catNode.next;
            }
        }
        else {
            catNode = head;
            for (int i = 0; i < index; i++) {
                catNode = catNode.next;
            }
        }

        return catNode;
    }

    @Override
    public Object get(int index) {
        if (index < 0 || index > totalQuantity - 1) {
            throw new IndexOutOfBoundsException();
        }

        return find(index).value;
    }

    @Override
    public Object getHead() {
        if (head == null) {
            return null;
        }

        return head.value;
    }

    @Override
    public Object getTail() {
        if (tail == null) {
            return null;
        }

        System.out.println(tail.previous.value);
        return tail.value;
    }

    public void insert(int index, T value) {
        CatNode previousNode = find(index);
        if (previousNode == null) {
            return;
        }
        CatNode newNode = new CatNode(value);
        newNode.next = previousNode.next;
        previousNode.next = newNode;
        newNode.previous = previousNode;
        if (newNode.next != null)
            newNode.next.previous = newNode;
        else {
            tail = newNode;
        }
        totalQuantity++;
    }

    public boolean isEmpty() {
        return totalQuantity == 0;
    }

    public String toString() {
        String result = "";
        CatNode catNode = head;
        if(head == null) {
            System.out.println("List is empty!");
            return null;
        }
        while(catNode != null) {
            result += catNode.value + " ";
            catNode = catNode.next;
        }
        return result;
    }

    public void remove() {
        totalQuantity -= totalQuantity;
        head = null;
        tail = null;
    }

    public void removeBackNode() {
        removeNode(tail);
    }

    public void removeFrontNode() {
        removeNode(head);
    }

    public void removeNode(CatNode catNode) {
        if (head == null || catNode == null) {
            return;
        }
        if (head == catNode) {
            head = catNode.next;
        }
        else if (tail == catNode) {
            tail = catNode.previous;
        }
        if (catNode.next != null) {
            catNode.next.previous = catNode.previous;
        }
        if (catNode.previous != null) {
            catNode.previous.next = catNode.next;
        }
        totalQuantity--;
    }

    public void removeNodeByIndex(int index) {
        if (index >= totalQuantity || index < 0) {
            return;
        }
        removeNode(find(index));
    }

    public void set(int index, T value) {
        if (find(index) != null) {
            find(index).value = value;
        }
        else {
            throw new IllegalStateException();
        }
    }

    public int size() {
        return totalQuantity;
    }
}