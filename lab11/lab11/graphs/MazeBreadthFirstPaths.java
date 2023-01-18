package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Queue<Integer> fringe;
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        s = m.xyTo1D(sourceX, sourceY);
        t = m.xyTo1D(targetX, targetY);
        fringe = new ArrayDeque<>();
        distTo[s] = 0;
        edgeTo[s] = s;
        marked[s] = true;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        fringe.add(s);
        while (!fringe.isEmpty()) {
            int v = fringe.poll();
            if (v == t) {
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    fringe.add(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    announce();
                    if (w == t) {
                        return;
                    }
                }
            }

        }
    }


    @Override
    public void solve() {
         bfs();
    }
}

