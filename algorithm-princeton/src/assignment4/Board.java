package assignment4;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;

public class Board {
	
	private static final int[] DELTA_X = new int[] { -1, 1, 0, 0 };
    private static final int[] DELTA_Y = new int[] { 0, 0, -1, 1 };
    
	private int n;
	private int[][] board;
	private int blankX;
	private int blankY;
	
	
	/**
	 * construct a board from an n-by-n array of blocks
	 * (where blocks[i][j] = block in row i, column j)
	 * @param blocks
	 */
	public Board(int[][] blocks) {
		n = blocks.length;
		board = new int[n][n];
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				board[i][j] = blocks[i][j];
				if (blocks[i][j] == 0) {
					blankX = i;
					blankY = j;
				}
			}
		}
	}

	/**
	 * board dimension n
	 * @return
	 */
	public int dimension()  {
		return n;
	}

	/**
	 * number of blocks out of place
	 * @return
	 */
	public int hamming() {
		int count = 0;
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				// ignore the blank square
				if ((i+j != 2*n-2) && (board[i][j] != i*n+j+1)) {
					count++;
				}
			}
		}
		
		// only the hamming distance without moves
		return count;
	}

	/**
	 * sum of Manhattan distances between blocks and goal
	 * @return
	 */
	public int manhattan() {
		int count = 0;
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				// ignore the blank square
				int num = board[i][j];
				if (num != 0) {
					// compute the goal row and column number
					int row = (num - 1) / n;
					int col = (num - 1) % n;
					
					// compute the Manhattan distance
					count += Math.abs(row - i) + Math.abs(col - j);
				}
			}
		}
		
		// without moves
		return count;
	}

	/**
	 * is this board the goal board?
	 * @return
	 */
	public boolean isGoal() {
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				if ((i+j != 2*n-2) && (board[i][j] != i*n+j+1)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * a board that is obtained by exchanging any pair of blocks
	 * @return
	 */
	public Board twin() {
		int[][] twinBoard = copyBoard();
		
		// find two un-blank blocks
		int row = (blankX + 1) % n;
		
		// swap (row1,col1) and (row2,col2)
		twinBoard[row][0] = board[row][1];
		twinBoard[row][1] = board[row][0];
		
		return new Board(twinBoard);
	}

	/**
	 * does this board equal y?
	 * refer the implementation of Date.java and Transaction.java
	 */
	public boolean equals(Object y) {
		if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        // we do not consider moves
        if (this.n != that.n) {
        	return false;
        }
        else {
        	for (int i=0; i<n; i++) {
        		for (int j=0; j<n; j++) {
        			if (this.board[i][j] != that.board[i][j]) {
        				return false;
        			}
        		}
        	}
        }
        return true;
	}

	/**
	 * return all neighboring boards
	 * @return
	 */
	public Iterable<Board> neighbors() {
		
		int[][] copyBoard = copyBoard();
		List<Board> list = new ArrayList<Board>();
		
		// swap the blank square with neighbor squares
		for (int i=0; i<4; i++) {
			int row = blankX + DELTA_X[i];
			int col = blankY + DELTA_Y[i];
			// check if neighbor's position is valid
			if ((row >= 0) && (row < n) && (col >= 0) && (col < n)) {
				exchange(copyBoard, row, col, blankX, blankY);
				list.add(new Board(copyBoard));
				// do not forget to change back
				exchange(copyBoard, row, col, blankX, blankY);
			}
		}
		
		return list;
	}
	
	/**
	 * return a new copy of the board
	 * @return
	 */
	private int[][] copyBoard() {
		int[][] copy = new int[n][n];
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				copy[i][j] = board[i][j];
			}
		}
		return copy;
	}
	
	/**
	 * exchange two squares of a board
	 * @param b
	 * @param x
	 * @param y
	 * @param x1
	 * @param y1
	 */
	private void exchange(int[][] b, int x, int y, int x1, int y1) {
		int temp = b[x][y];
		b[x][y] = b[x1][y1];
		b[x1][y1] = temp;
	}
	
	
	/**
	 * string representation of this board (in the output format specified
	 * below), use given implementation
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(n + "\n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s.append(String.format("%2d ", board[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

	/**
	 * unit tests (not graded)
	 * @param args
	 */
	public static void main(String[] args) {
		// create initial board from file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    for (Board b : initial.neighbors()) {
	    	System.out.println(b.toString());
	    }
	}
}