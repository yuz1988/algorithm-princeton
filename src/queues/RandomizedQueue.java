package queues;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Based on array ResizingArrayStack:
 * http://algs4.cs.princeton.edu/13stacks/ResizingArrayStack.java.html
 *
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private Item[] a;         // array of items
    private int n;            // number of elements on stack
	
	/**
	 * construct an empty randomized queue
	 */
	public RandomizedQueue() {
		a = (Item[]) new Object[2];
        n = 0;
	}

	/**
	 * is the queue empty?
	 * @return
	 */
	public boolean isEmpty() {
		return n == 0;
	}

	/**
	 * return the number of items on the queue
	 * @return
	 */
	public int size() {
		return n;
	}

	/**
	 * resize the underlying array holding the elements
	 * @param capacity
	 */
    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;

       // alternative implementation
       // a = java.util.Arrays.copyOf(a, capacity);
    }
    
	/**
	 * add the item
	 * @param item
	 */
	public void enqueue(Item item) {
		if (item == null) {
			throw new NullPointerException("add null");
		}
		if (n == a.length) resize(2*a.length);    // double size of array if necessary
        a[n++] = item;                            // add item
	}
	

	/**
	 * remove and return a random item
	 * @return
	 */
	public Item dequeue() {
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		int index = StdRandom.uniform(n);
		Item item = a[index];
		a[index] = a[n-1];  // swap last item and selected item
		a[n-1] = null;
		n--;
		
        // shrink size of array if necessary
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;
	}

	/**
	 * return (but do not remove) a random item
	 * @return
	 */
	public Item sample() {
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		int index = StdRandom.uniform(n);
		return a[index];
	}
	
	// an iterator, doesn't implement remove() since it's optional
    private class ShuffleArrayIterator implements Iterator<Item> {
        private int i;
        private Item[] shuffledArray;
        
        public ShuffleArrayIterator() {
        	shuffledArray = Arrays.copyOf(a, n);
        	StdRandom.shuffle(shuffledArray);
            i = 0;
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return shuffledArray[i++];
        }
    }

	/**
	 * return an independent iterator over items in random order
	 */
	public Iterator<Item> iterator() {
		return new ShuffleArrayIterator();
	}

	/**
	 * unit testing
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
}
