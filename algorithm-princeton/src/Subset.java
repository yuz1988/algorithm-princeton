import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {

	public static void main(String[] args) {
		
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> randQ = new RandomizedQueue<String>();
		// solution 1: randomized queue with size n
		while (!StdIn.isEmpty()) {
			randQ.enqueue(StdIn.readString());
		}
		
		for (int i = 0; i < k; i++) {
			StdOut.println(randQ.dequeue());
		}
		
//		// solution 2: randomized queue with size k
//		// Reservoir Sampling
//		int counter = 0;
//		while (!StdIn.isEmpty()) {
//			counter++;
//			if (counter <= k) {
//				randQ.enqueue(StdIn.readString());
//			}
//			else {
//				double prob = ((double) counter - 1) / counter;
//				if (StdRandom.uniform() < prob) {
//					randQ.dequeue();
//					randQ.enqueue(StdIn.readString());
//				}
//			}
//		}
//		for (int i=0; i<k; i++) {
//			StdOut.println(randQ.dequeue());
//		}
		
	}

}
