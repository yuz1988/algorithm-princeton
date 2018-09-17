package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	
	private int n;
	private boolean[][] matrix;
	private WeightedQuickUnionUF uFinder;
	private WeightedQuickUnionUF uFinderTop;
	
	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		else {
			this.n = n;
			matrix = new boolean[n][n];
			// two virtual positions: beginning and end
			uFinder = new WeightedQuickUnionUF(n*n + 2);
			
			// for "backwash" problem, only create top virtual position
			uFinderTop = new WeightedQuickUnionUF(n*n + 1);
		}
	}

	// open site (row, col) if it is not open already
	public void open(int row, int col) {
		row--;
		col--;
		if (row < 0 || row >= n || col < 0 || col >= n) {
			throw new IndexOutOfBoundsException();
		}
		else if (!matrix[row][col]) {
			matrix[row][col] = true;
			
			int index = row*n + col + 1;
			// up
			if (row == 0) {
				uFinder.union(0, index);
				uFinderTop.union(0, index);
			}
			else if (matrix[row-1][col]) {
				uFinder.union(index, index-n);
				uFinderTop.union(index, index-n);
			}
			
			// down
			if (row == n-1) {
				uFinder.union(n*n+1, index);
			}
			else if (matrix[row+1][col]) {
				uFinder.union(index, index+n);
				uFinderTop.union(index, index+n);
			}
			
			// left
			if (col > 0 && matrix[row][col-1]) {
				uFinder.union(index, index-1);
				uFinderTop.union(index, index-1);
			}
			
			// right
			if (col < n-1 && matrix[row][col+1]) {
				uFinder.union(index, index+1);
				uFinderTop.union(index, index+1);
			}
		}
	}

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		row--;
		col--;
		if (row < 0 || row >= n || col < 0 || col >= n) {
			throw new IndexOutOfBoundsException();
		}
		else {
			return matrix[row][col];
		}
	}

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            row--;
            col--;
            return uFinderTop.connected(0, row * n + col + 1);
        }
        return false;
	}
	
	// does the system percolate?
	public boolean percolates() {
		return uFinder.connected(0, n*n+1);
	}

	// test client (optional)
	public static void main(String[] args) {
		
	}
}