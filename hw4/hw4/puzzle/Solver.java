package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import javax.naming.directory.SearchControls;
import java.util.*;

public class Solver {

    private List<WorldState> solution = new ArrayList<>();
    private Map<WorldState, Integer> edtgCaches = new HashMap<>();

    private class searchNode {
        private WorldState world;
        private int m;      // number of moves from initial world state.
        private searchNode parent;  // the previous world state.
        private int priority;       // the priority of this worldState.
        public searchNode(WorldState w, int m, searchNode p) {
            this.world = w;
            this.m = m;
            this.parent = p;

            }
    }

    private class NodeComparator implements Comparator<searchNode> {

        @Override
        public int compare(searchNode o1, searchNode o2) {
            int o1Edtg = getEdtg(o1);
            int o2Edtg = getEdtg(o2);
            int o1Prio = o1.m + o1Edtg;
            int o2Prio = o2.m + o2Edtg;
            return o1Prio - o2Prio;
        }

        private int getEdtg(searchNode sn) {
            if (!edtgCaches.containsKey(sn)) {
                edtgCaches.put(sn.world, sn.world.estimatedDistanceToGoal());
            }
            return edtgCaches.get(sn.world);
        }

    }

    /**Constructor which solves the puzzle, computing every necessary for moves() to not have to solve the problem again
     * Solve the puzzle using the A* algorithm.
     * Assumes a solution exists.
     * */
    public Solver(WorldState initial) {
        MinPQ<searchNode> PQ = new MinPQ<>(new NodeComparator());
        PQ.insert(new searchNode(initial, 0, null));
        while (!PQ.isEmpty()) {
            searchNode n = PQ.delMin();
            if (n.world.isGoal()) {
                bestMoveSequence(n);
                return;
            }
            for (WorldState w : n.world.neighbors()) {
                searchNode newNode = new searchNode(w, n.m + 1, n);
                // A critical optimization checks that no enqueued WorldState is its own grandparent.
                if (n.parent != null && w.equals(n.parent.world)) {
                    continue;
                }
                PQ.insert(newNode);
            }
        }
    }

    private void bestMoveSequence(searchNode n) {
        while (n.parent != null) {
            solution.add(n.world);
            n = n.parent;
        }
        solution.add(n.world);
        Collections.reverse(solution);
    }
    /**
     * Returns the minimum number of moves to solve the puzzle starting at the initial WorldState.
     * */
    public int moves() {
        return solution.size() - 1;
    }

    /** Returns a sequence of WorldStates from the initial worldState to the solution. */
    public Iterable<WorldState> solution() {

        return solution;
    }
}
