import java.util.ArrayList;
import java.util.List;

/**
 * Implements fuzzy (typo-tolerant) search using Levenshtein Edit
 * Distance — the minimum number of single-character insertions,
 * deletions, or substitutions needed to turn one word into another.
 *
 * This lets a search for "contarct" still match "contract" even
 * though the spelling is wrong.
 */
public class FuzzySearch {

    /**
     * Computes the Levenshtein distance between two words using
     * dynamic programming. dp[i][j] = edit distance between the
     * first i characters of a and the first j characters of b.
     */
    public static int editDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int m = a.length();
        int n = b.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;   // delete all of a
        for (int j = 0; j <= n; j++) dp[0][j] = j;   // insert all of b

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // characters match, no edit needed
                } else {
                    int substitute = dp[i - 1][j - 1] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    dp[i][j] = Math.min(substitute, Math.min(insert, delete));
                }
            }
        }
        return dp[m][n];
    }

    /**
     * Searches a list of clauses for any word within `maxDistance`
     * edits of the query term. Returns clauses that contain at least
     * one closely-matching word, ranked by how close the best match is.
     */
    public static List<FuzzyMatch> search(List<Clause> clauses, String query, int maxDistance) {
        List<FuzzyMatch> matches = new ArrayList<>();

        for (Clause clause : clauses) {
            String[] words = clause.getText().split("\\W+");
            int bestDistance = Integer.MAX_VALUE;
            String bestWord = null;

            for (String word : words) {
                if (word.isBlank()) continue;
                int dist = editDistance(query, word);
                if (dist < bestDistance) {
                    bestDistance = dist;
                    bestWord = word;
                }
            }

            if (bestDistance <= maxDistance) {
                matches.add(new FuzzyMatch(clause, bestWord, bestDistance));
            }
        }

        matches.sort((m1, m2) -> Integer.compare(m1.distance, m2.distance));
        return matches;
    }

    /** Small holder class pairing a clause with its closest matching word. */
    public static class FuzzyMatch {
        public final Clause clause;
        public final String matchedWord;
        public final int distance;

        public FuzzyMatch(Clause clause, String matchedWord, int distance) {
            this.clause = clause;
            this.matchedWord = matchedWord;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "(matched \"" + matchedWord + "\", distance=" + distance + ") " + clause;
        }
    }
}
