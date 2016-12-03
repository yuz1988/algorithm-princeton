import java.util.ArrayList;
import java.util.Arrays;

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
		for (int i=0; i<n; i++) {
			if (points[i] == null) {
				throw new NullPointerException();
			}
		}
		// sort points
		Arrays.sort(points);
		// check repeated points
		for (int i=0; i<n-1; i++) {
			if (points[i].compareTo(points[i+1]) == 0) {
				throw new IllegalArgumentException();
			}
		}
		
		// n^4 time complexity
		for (int i=0; i<n-3; i++) {
			
			for (int j=i+1; j<n-2; j++) {
				double slope1 = points[i].slopeTo(points[j]);
				
				for (int k=j+1; k<n-1; k++) {
					double slope2 = points[i].slopeTo(points[k]);
					
					if (slope1 == slope2) {
						for (int l=k+1; l<n; l++) {
							double slope3 = points[i].slopeTo(points[l]);
							if (slope1 == slope3) {
								numOfSeg++;
								segList.add(new LineSegment(points[i], points[l]));
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
}