public class OffByN implements CharacterComparator {

    private int Num;
    /** CharacterComparator constructor*/
    public OffByN(int N) {
        Num = N;
    }
    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        return Math.abs(diff) == Num;
    }
}