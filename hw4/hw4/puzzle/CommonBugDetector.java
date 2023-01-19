package hw4.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hug. See https://goo.gl/MVL8up for details on these puzzles.
 */
public class CommonBugDetector {
    public static class CommonBugPuzzleState implements WorldState {
        private char name;
        public CommonBugPuzzleState() {
            name = 's';
        }

        private CommonBugPuzzleState(char n) {
            name = n;
        }

        @Override
        public int estimatedDistanceToGoal() {
            if (name == 'g') {
                return 0;
            }
            if (name == 'x') {
                return 4;
            }
            return 1;
        }


        @Override
        public Iterable<WorldState> neighbors() {
            List<Character> ls = new ArrayList<>();
            switch (name) {
                case('s'): ls.add('a');ls.add('x');return createWorldStateList(ls);
                case('a'): ls.add('b');return createWorldStateList(ls);
                case('b'): ls.add('c');return createWorldStateList(ls);
                case('c'): ls.add('d');return createWorldStateList(ls);
                case('d'): ls.add('e');return createWorldStateList(ls);
                case('e'): ls.add('g');return createWorldStateList(ls);
                case('x'): ls.add('c');return createWorldStateList(ls);
                default: return null;
            }
        }



        private static List<WorldState> createWorldStateList(List<Character> lc) {
            List<WorldState> lws = new ArrayList<>();
            for (char c : lc) {
                lws.add(new CommonBugPuzzleState(c));
            }
            return lws;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CommonBugPuzzleState that = (CommonBugPuzzleState) o;
            return name == that.name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }


    public static void main(String[] args) {
        CommonBugPuzzleState cbps = new CommonBugPuzzleState();
        Solver s = new Solver(cbps);

        // s.moves() should be 5
        System.out.println("s.moves() should be 5, and your s.moves() is: " + s.moves());

        AlphabetEasyPuzzle aep = new AlphabetEasyPuzzle('a');
        Solver s3 = new Solver(aep);
        s3.solution();
        System.out.println("TODO: Print out the number of total things ever"
                           + " enqueued in your MinPQ and compare to the comments.");
        // if you print out the total number of items enqueued by s3
        // it should be approximately 25, not approximately 50.
    }
}
