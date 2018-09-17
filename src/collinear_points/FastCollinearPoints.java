package collinear_points;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private int numOfSeg;
    private ArrayList<LineSegment> segList;

    /**
     * finds all line segments containing 4 or more points
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
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
        for (int i = 0; i < n - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        // n^2*log(n) complexity
        for (int i = 0; i < n; i++) {
            // consider for each point p in points
            Point p = points[i];

            // sort based on the slopes respect to p
            Arrays.sort(sortedPoints, p.slopeOrder());

            // find collinear points
            // collect the collinear points
            ArrayList<Point> collinearPoints = new ArrayList<Point>();

            // skip first point, this should be point p itself
            // as the slope of degenerate line is -infinity
            int j = 1;
            while (j <= n - 3) {
                Point q = sortedPoints[j];
                double slope = p.slopeTo(q);
                // do not forget to add point p itself
                collinearPoints.add(p);
                collinearPoints.add(q);

                // begin to collect collinear points
                while (j < n - 1) {
                    j++;
                    Point r = sortedPoints[j];
                    double slope1 = p.slopeTo(r);
                    if (slope == slope1) {
                        collinearPoints.add(r);
                    } else {
                        break;
                    }
                }

                int length = collinearPoints.size();
                if (length >= 4) {
                    // decide the two ends of the line segment
                    Point[] minMaxPoints = minMaxPoints(collinearPoints);
                    Point startPoint = minMaxPoints[0];

                    // as each line segment can only be added once,
                    // if p is not the start point, ignore it
                    if (p.compareTo(startPoint) == 0) {
                        Point endPoint = minMaxPoints[1];
                        segList.add(new LineSegment(startPoint, endPoint));
                        numOfSeg++;
                    }
                }
                // do not forget to clear the collinearPoints
                collinearPoints.clear();
            }

        }

    }

    /**
     * returns min-point: Point[0] and max-point: Point[1]
     *
     * @param points
     * @return
     */
    private Point[] minMaxPoints(ArrayList<Point> points) {
        Point[] minMax = new Point[2];
        Point min = points.get(0);
        Point max = points.get(0);
        for (Point p : points) {
            if (p.compareTo(min) < 0) {
                min = p;
            }
            if (p.compareTo(max) > 0) {
                max = p;
            }
        }
        minMax[0] = min;
        minMax[1] = max;
        return minMax;
    }

    /**
     * the number of line segments
     *
     * @return
     */
    public int numberOfSegments() {
        return numOfSeg;
    }

    /**
     * the line segments
     *
     * @return
     */
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[numOfSeg];
        for (int i = 0; i < numOfSeg; i++) {
            segs[i] = segList.get(i);
        }
        return segs;
    }


    /**
     * test client
     *
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}