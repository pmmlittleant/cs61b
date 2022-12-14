
public class OffByN implements CharacterComparator {

    private int n;
    /** CharacterComparator constructor*/
    public OffByN(int N) {
        n = N;
    }
    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        return Math.abs(diff) == n;
    }
}

