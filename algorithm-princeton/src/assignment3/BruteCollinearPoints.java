import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

	private int numOfSeg;
	private ArrayList<LineSegment> segList;

	/**
	 * finds all line segments containing 4 points
	 * 
	 * @param points
	 */
	public BruteCollinearPoints(Point[] points) {
		numOfSeg = 0;
		segList = new ArrayList<LineSegment>();
		if (points == null) {
			throw new NullPointerException();
		}
		
		int n = points.length;
		// check null point
		for (int i = 0; i < n; i++) {
			if (points[i] == null) {
				throw new NullPointerException();
			}
		}
				
		// make a copy of points
		// data type should have no side effects unless documented in API
		Point[] sortedPoints = Arrays.copyOf(points, n);
		
		// sort points
		Arrays.sort(sortedPoints);
		// check repeated points
		for (int i=0; i<n-1; i++) {
			if (sortedPoints[i].compareTo(sortedPoints[i+1]) == 0) {
				throw new IllegalArgumentException();
			}
		}
		
		// n^4 time complexity
		for (int i=0; i<n-3; i++) {
			
			for (int j=i+1; j<n-2; j++) {
				double slope1 = sortedPoints[i].slopeTo(sortedPoints[j]);
				
				for (int k=j+1; k<n-1; k++) {
					double slope2 = sortedPoints[i].slopeTo(sortedPoints[k]);
					
					if (slope1 == slope2) {
						for (int l=k+1; l<n; l++) {
							double slope3 = sortedPoints[i].slopeTo(sortedPoints[l]);
							if (slope1 == slope3) {
								numOfSeg++;
								segList.add(new LineSegment(sortedPoints[i], sortedPoints[l]));
							}
						}
					}
					
				}
			}
		}
		
	}

	/**
	 * the number of line segments
	 * @return
	 */
	public int numberOfSegments() {
		return numOfSeg;
	}

	/**
	 * the line segments
	 * @return
	 */
	public LineSegment[] segments() {
		LineSegment[] segs = new LineSegment[numOfSeg];
		for (int i=0; i<numOfSeg; i++) {
			segs[i] = segList.get(i);
		}
		return segs;
	}
	
	/**
	 * test client
	 * @param args
	 */
	public static void main(String[] args) {

	    // read the n points from a file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
	    StdDraw.enableDoubleBuffering();
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}