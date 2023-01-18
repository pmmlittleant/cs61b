package lab11.graphs;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private LinkedList<Integer> unvisited = new LinkedList<>();
    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        for (int v = 0; v < maze.V(); v++) {
            unvisited.add(v);
        }
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int vX = maze.toX(v);
        int vY = maze.toY(v);
        int tX = maze.toX(t);
        int tY = maze.toY(t);
        return Math.abs(vY - tY) + Math.abs(vX - tX);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        cmparatorHeur cmpt = new cmparatorHeur();
        Queue<Integer> fringe = new PriorityQueue<>(cmpt);
        for (int v : unvisited) {
            fringe.add(v);
        }

        return fringe.poll();
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        marked[s] = true;
        while (!unvisited.isEmpty()) {
            int v = findMinimumUnmarked();
            marked[v] = true;
            announce();
            unvisited.remove((Object) v);
            if (v == t) {
                targetFound = true;
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    edgeTo[w] = v;
                    if (w == t) {
                        marked[w] = true;
                        targetFound = true;
                        announce();
                        return;
                    }
                }
            }
        }


    }

    private class cmparatorHeur implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return h(o1) + distTo[o1] - h(o2) - distTo[o2];
        }
    }
    @Override
    public void solve() {
        astar(s);
    }

}

