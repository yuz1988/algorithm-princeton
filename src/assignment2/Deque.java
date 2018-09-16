package assignment2;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


/**
 * Based on LinkedStack: http://algs4.cs.princeton.edu/13stacks/LinkedStack.java
 * 
 * @param <Item>
 */

public class Deque<Item> implements Iterable<Item> {
    private int n;          // size of the deque
    private Node first;     // top of deque
    private Node last;      // end of deque

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    /**
     * Initializes an empty stack.
     */
    public Deque() {
        first = null;
        last = first;
        n = 0;
    }

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in the stack.
     * @return the number of items in the stack
     */
    public int size() {
        return n;
    }
    
    /*
     * add the item to the front
     */
    public void addFirst(Item item) {
    	if (item == null) {
    		throw new NullPointerException("add null");
    	}
    	Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (oldfirst != null) {
        	oldfirst.prev = first;
        }
        else {
        	last = first;
        }
        n++;
    }
    
    /*
     * add the item to the end
     */
    public void addLast(Item item) {
    	if (item == null) {
    		throw new NullPointerException("add null");
    	}
    	Node oldlast = last;
    	last = new Node();
    	last.item = item;
    	last.next = null;
    	last.prev = oldlast;
    	if (oldlast != null) {
    		oldlast.next = last;
    	}
    	else {
    		first = last;
    	}
    	n++;
    }
    
    /**
     * remove and return the item from the front
     * @return first item
     */
    public Item removeFirst() {
    	if (isEmpty()) throw new NoSuchElementException("deque empty");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node
        if (first != null) {
        	first.prev = null;
        }
        else {
        	last = null;
        }
        n--;
        return item;                   // return the saved item
    }
    
    /**
     * remove and return the item from the end
     * @return last item
     */
    public Item removeLast() {
    	if (isEmpty()) throw new NoSuchElementException("deque empty");
        Item item = last.item;        // save item to return
        last = last.prev;            // delete last node
        if (last != null) {
        	last.next = null;
        }
        else {
        	first = null;
        }
        n--;
        return item;                   // return the saved item
    }


    /**
     * Returns an iterator to this deque that iterates through the items in LIFO order.
     * @return an iterator to this deque that iterates through the items in LIFO order.
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()  { 
        	return current != null;                     
        }
        public void remove() { 
        	throw new UnsupportedOperationException();  
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }


    /**
     * Unit tests the {@code Deque} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
    	System.out.println("begin");
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                deque.addFirst(item);
            else {
                StdOut.print(deque.removeLast() + " ");
            }
        }
        
    }
}
