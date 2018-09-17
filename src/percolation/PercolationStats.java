package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	
	private int trials;
	private double[] fractions;
	
	// perform trials independent experiments on an n-by-n grid
	public PercolationStats(int n, int trials) {
		if (n <= 0 || trials <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.trials = trials;
		fractions = new double[trials];
		
		// repeat experiment @trials times
		for (int i = 0; i < trials; i++) {
			int count = 0;
			Percolation p = new Percolation(n);
			while (!p.percolates()) {
				int row = StdRandom.uniform(n) + 1;
				int col = StdRandom.uniform(n) + 1;
				if (!p.isOpen(row, col)) {
					p.open(row, col);
					count++;
				}
			}
			fractions[i] = ((double) count) / (n*n);
		}
	}

	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(fractions);
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return StdStats.stddev(fractions);
	}

	// low  endpoint of 95% confidence interval
	public double confidenceLo() {
		return mean()-1.96*stddev()/Math.sqrt(trials);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mean()+1.96*stddev()/Math.sqrt(trials);
	}

	// test client (described below)
	public static void main(String[] args) {
		PercolationStats ps = new PercolationStats(2, 100);
		System.out.println(ps.mean());
		System.out.println(ps.stddev());
		System.out.println(ps.confidenceLo() + "   " + ps.confidenceHi());
	}
}
