import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	private final Digraph G;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null) {
			throw new IllegalArgumentException("Graph input is null");
		}
		this.G = G;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		List<Integer> listV = new ArrayList<Integer>();
		listV.add(v);
		List<Integer> listW = new ArrayList<Integer>();
		listW.add(w);
		return length(listV, listW);
	}

	// a common ancestor of v and w that participates in a shortest ancestral path;
	// -1 if no such path
	public int ancestor(int v, int w) {
		List<Integer> listV = new ArrayList<Integer>();
		listV.add(v);
		List<Integer> listW = new ArrayList<Integer>();
		listW.add(w);
		return ancestor(listV, listW);
	}

	// length of shortest ancestral path between any vertex in v and any vertex in
	// w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		return bfs(v, w)[1];
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such
	// path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		return bfs(v, w)[0];
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In("./digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d,  ancestor = %d\n", length, ancestor);
        }
	}

	/**
	 * First do bfs from vertices in one list, then do bfs of the other list of
	 * vertices and check if parents (vertex it points to) appear and keep the
	 * minimum sum of distance.
	 * 
	 * @param listV
	 * @param listW
	 * @return [ancester_id, length of ancestral path]
	 */
	private int[] bfs(Iterable<Integer> listV, Iterable<Integer> listW) {
		// Sanity check.
		if (listV == null || listW == null) {
			throw new IllegalArgumentException("Null listV or listW");
		}
		for (int v : listV) {
			if (v < 0 || v > G.V()) {
				throw new IllegalArgumentException("vertex v is out of range");
			}
		}
		for (int w : listW) {
			if (w < 0 || w > G.V()) {
				throw new IllegalArgumentException("vertex w is out of range");
			}
		}

		// Returned result [vertex, distance].
		int[] res = new int[] { -1, -1 };

		// <parent of v, distance to v>
		HashMap<Integer, Integer> mapV = new HashMap<Integer, Integer>();
		// For bfs, [vertex, distance]. Use algs4 version Queue.
		Queue<int[]> queue = new Queue<int[]>();

		// Init.
		for (int v : listV) {
			mapV.put(v, 0);
			queue.enqueue(new int[] { v, 0 });
		}

		// Enqueue all the parent vertices (with distance) of v to queue.
		while (!queue.isEmpty()) {
			int[] top = queue.dequeue();
			int vertex = top[0];
			int dist = top[1];
			for (int parent : G.adj(vertex)) {
				// Enqueue only the non-visited vertices.
				if (!mapV.containsKey(parent)) {
					mapV.put(parent, dist + 1);
					queue.enqueue(new int[] { parent, dist + 1 });
				}
			}
		}

		// Init the bfs starting from w.
		HashSet<Integer> visitedW = new HashSet<Integer>();
		for (int w : listW) {
			visitedW.add(w);
			queue.enqueue(new int[] { w, 0 });
		}

		// Bfs from vertex w.
		while (!queue.isEmpty()) {
			int[] top = queue.dequeue();
			int vertex = top[0];
			int dist = top[1];
			// Check if parents of v contains parents of w.
			if (mapV.containsKey(vertex)) {
				int lengthOfPath = dist + mapV.get(vertex);
				if (res[0] == -1 || res[1] > lengthOfPath) {
					res[0] = vertex;
					res[1] = lengthOfPath;
				}
			}

			for (int parent : G.adj(vertex)) {
				if (!visitedW.contains(parent)) {
					visitedW.add(parent);
					queue.enqueue(new int[] { parent, dist + 1 });
				}
			}
		}

		return res;
	}
}
