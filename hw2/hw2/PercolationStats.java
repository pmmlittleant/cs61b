package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private final int T;
    private double[] xts;

    /** perform T independent experiments on an N-by-N grid.*/
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N < 0 || T < 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        xts = new double[T];
        int i = 0;
        while (i < T) {
            Percolation perc = pf.make(N);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                perc.open(row, col);
            }
            xts[i] = perc.numberOfOpenSites() / (N * N);
            i++;
        }
    }

    /** sample mean of percolation threshold.*/
    public double mean() {
        return StdStats.mean(xts);
    }

    /** sample standard deviation of percolation threshold.*/
    public double stddev() {
        double stddev = StdStats.stddev(xts);
        return stddev;
    }

    /** low endpoint of 95% confidence interval.*/
    public double confidenceLow() {
        double u = mean();
        double stddev = stddev();
        double low = u - 1.96 * stddev / Math.sqrt(T);
        return low;
    }

    /** high endpoint of 95% confidence interval.*/
    public double confidenceHigh() {
        double u = mean();
        double stddev = stddev();
        double high = u + 1.96 * stddev / Math.sqrt(T);
        return high;
    }

}
