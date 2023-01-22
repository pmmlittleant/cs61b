import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxLen = Integer.MIN_VALUE;
        //find the max length of string in array asciis
        for (String s : asciis) {
            maxLen = maxLen > s.length() ? maxLen : s.length();
        }
        String[] sort;
        for (int d = 0; d < maxLen; d++) {
            sort = sortHelperLSD(asciis, d);
            asciis = sort;
        }

        return asciis;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        int[] counts = new int[256];
        //gather counts of value in index of s.
        for (String s : asciis) {
            int i = s.length() - index - 1;
            int c = i < 0 ? 0 : s.charAt(i);
            counts[c]++;
        }
        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < starts.length; i++) {
            starts[i] = pos;
            pos += counts[i];
        }
        String[] sorted = new String[asciis.length];
        for (String s : asciis) {
            int i = s.length() - index - 1;
            int c = i < 0 ? 0 : s.charAt(i);
            int place = starts[c];
            sorted[place] = s;
            starts[c] += 1;
        }
        return sorted;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }


    public static void main(String[] args) {
        String[] a = new String[]{"baa", "aaa", "acb", "c", "abd"};
        String[] sort = sort(a);
        for (String s : a) {
            System.out.print(s + " ");
        }
        System.out.println();
        for (String s : sort) {
            System.out.print(s + " ");
        }
    }
}
