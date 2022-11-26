public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> A = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);
            A.addLast(character);
        }
        return A;
    }

    public boolean isPalindrome(String word) {
        return isPalindrome(wordToDeque(word));
    }
    private boolean isPalindrome(Deque<Character> word) {
        if (word.size() == 1 || word.isEmpty()) {
            return true;
        } else if (word.removeFirst() == word.removeLast()) {
            return isPalindrome(word);
        } else {
            return false;
        }
    }


    public boolean isPalindrome(String word, CharacterComparator cc) {
        return isPalindrome(wordToDeque(word), cc);
    }
    private boolean isPalindrome(Deque<Character> word, CharacterComparator cc) {
        if (word.isEmpty() || word.size() == 1) {
            return true;
        } else if (cc.equalChars(word.removeFirst(), word.removeLast())) {
            return isPalindrome(word, cc);
        } else {
            return false;
        }
    }
}
