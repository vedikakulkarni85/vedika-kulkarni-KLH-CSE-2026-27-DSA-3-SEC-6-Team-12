import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Knuth-Morris-Pratt (KMP) pattern matching algorithm.
 * Used to find every occurrence of a search term (pattern) inside a
 * clause's text (the "haystack") in O(n + m) time, instead of relying
 * on Java's built-in String.contains(), which is what a DSA course
 * project is expected to avoid.
 */
public class PatternMatcher {

    /**
     * Builds the KMP "longest proper prefix which is also a suffix"
     * (LPS) table for the given pattern. This table lets the search
     * skip re-checking characters it has already matched.
     */
    private static int[] buildLpsTable(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0;
        int i = 1;
        lps[0] = 0;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }

    /**
     * Returns a list of every starting index in `text` where `pattern`
     * occurs, using the KMP algorithm. Case-insensitive.
     */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (pattern == null || pattern.isEmpty() || text == null) {
            return matches;
        }

        String haystack = text.toLowerCase();
        String needle = pattern.toLowerCase();
        int[] lps = buildLpsTable(needle);

        int i = 0; // index into haystack
        int j = 0; // index into needle

        while (i < haystack.length()) {
            if (haystack.charAt(i) == needle.charAt(j)) {
                i++;
                j++;
                if (j == needle.length()) {
                    matches.add(i - j); // match found starting at this index
                    j = lps[j - 1];
                }
            } else if (j != 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return matches;
    }

    /** Convenience method: does the pattern occur at all in the text? */
    public static boolean contains(String text, String pattern) {
        return !search(text, pattern).isEmpty();
    }
}
