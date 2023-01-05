package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] sites;
    private int openSites;
    private WeightedQuickUnionUF UF;
    private WeightedQuickUnionUF UFbackWash;

    private final int N;
    private int topSite;
    private int bottomSite;

    /** create N by N grid, with all sites initially blocked. */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive!");
        }
        this.N = N;
        sites = new boolean[N][N];
        openSites  = 0;
        topSite = N * N;
        bottomSite = N * N + 1;
        UF = new WeightedQuickUnionUF(N * N + 2);
        UFbackWash = new WeightedQuickUnionUF(N * N + 1);

    }
    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        return sites[row][col];
    }

    /** is the site (row, col) full?*/
    public boolean isFull(int row, int col) {
        int p = xyTo1D(row, col);
        return UFbackWash.connected(p, topSite);
    }

    /** open the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        if (!sites[row][col]) {
            sites[row][col] = true;
            openSites += 1;
            unionAround(row, col);
            if (row == 0) {
                UF.union(xyTo1D(row, col), topSite);
                UFbackWash.union(xyTo1D(row, col), topSite);
            }
            if (row == N - 1) {
                UF.union(xyTo1D(row, col), bottomSite);
            }
        }
    }

    /** connect site (row, col) with open site around it.*/
    private void unionAround(int row, int col) {
        int d1 = xyTo1D(row, col);
        int d2;
        if (row + 1 < N && isOpen(row + 1, col)) {
            d2 = xyTo1D(row + 1, col);
            UF.union(d1, d2);
            UFbackWash.union(d1, d2);
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            d2 = xyTo1D(row, col - 1);
            UF.union(d1, d2);
            UFbackWash.union(d1, d2);

        }
        if (col + 1 < N && isOpen(row, col + 1)) {
            d2 = xyTo1D(row, col + 1);
            UF.union(d1, d2);
            UFbackWash.union(d1, d2);
        }
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            d2 = xyTo1D(row - 1, col);
            UF.union(d1, d2);
            UFbackWash.union(d1, d2);

        }
    }

    /** number of open sites. */
    public int numberOfOpenSites() {
        return openSites;
    }

    /** does the system percolate？*/
    public boolean percolates() {
        return UF.connected(topSite, bottomSite);
    }

    /**translate site (row, col) to number d */
    private int xyTo1D(int r, int c) {
        return r * N + c;
    }
}
