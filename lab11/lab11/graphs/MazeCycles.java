package lab11.graphs;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Random;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int[] parent;
    private boolean hasCycle = false;
    public MazeCycles(Maze m) {
        super(m);
        s = 0;
        parent = new int[maze.V()];
        parent[0] = 0;


    }

    @Override
    public void solve() {
        dfsCycle(s);

    }

    // Helper methods go here
    private void dfsCycle(int v) {
        marked[v] = true;
        if (hasCycle) {
            return;
        }
        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                parent[w] = v;
                announce();
                dfsCycle(w);
            } else if (marked[w] && parent[v] != w) {
                connectCycle(w, v);
                announce();
                hasCycle = true;
                return;
            }
        }
    }

    private void connectCycle(int w, int v) {
        edgeTo[w] = v;
        int c = v;
        while (edgeTo[v] != c) {
            edgeTo[v] = parent[v];
            v = parent[v];
        }
    }
}

