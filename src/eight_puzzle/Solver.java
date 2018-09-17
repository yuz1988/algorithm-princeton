package eight_puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Solver {

    private boolean solvable;
    private LinkedList<Board> solution;

    /**
     * find a solution to the initial board (using the A* algorithm)
     *
     * @param initial
     */
    public Solver(Board initial) {

        solvable = false;
        solution = new LinkedList<Board>();

        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();

        // add initial and twin board to the min priority queue.
        minPQ.insert(new SearchNode(0, initial, null));
        minPQ.insert(new SearchNode(0, initial.twin(), null));

        // record the last search node (if success)
        SearchNode lastNode = null;
        while (!minPQ.isEmpty()) {
            SearchNode node = minPQ.delMin();
            Board board = node.getBoard();
            if (board.isGoal()) {
                lastNode = node;
                break;
            } else {
                Iterable<Board> neighbors = board.neighbors();
                for (Board b : neighbors) {
                    // critical optimization: compare next board with parent board
                    if ((node.getParent() == null) || (!b.equals(node.getParent().getBoard()))) {
                        minPQ.insert(new SearchNode(node.getMoves() + 1, b, node));
                    }
                }
            }
        }

        // from the last search node, recover the solution by parent nodes
        while (lastNode != null) {
            solution.addFirst(lastNode.getBoard());
            lastNode = lastNode.getParent();
        }

        // check the first search node, if it is the twin board, solvable is false
        if (solution.getFirst().equals(initial)) {
            solvable = true;
        } else {
            solvable = false;
            // if not solvable, return solution as null
            solution = null;
        }
    }

    /**
     * is the initial board solvable?
     *
     * @return
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     *
     * @return
     */
    public int moves() {
        if (solvable) {
            return solution.size() - 1;
        } else {
            return -1;
        }
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     *
     * @return
     */
    public Iterable<Board> solution() {
        return solution;
    }

    /**
     * solve a slider puzzle (given below)
     * use given implementation
     *
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

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }


    /**
     * SearchNode contains the information of board, moves and parent node,
     * each search node will be inserted to the min priority queue
     */
    private class SearchNode implements Comparable<SearchNode> {

        private int moves;
        private Board board;
        private SearchNode parent;

        public SearchNode(int moves, Board board, SearchNode parent) {
            this.moves = moves;
            this.board = board;
            this.parent = parent;
        }

        public int getMoves() {
            return moves;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getParent() {
            return parent;
        }

        public int compareTo(SearchNode that) {
            return (moves + board.manhattan() - that.moves - that.board.manhattan());
        }
    }

}