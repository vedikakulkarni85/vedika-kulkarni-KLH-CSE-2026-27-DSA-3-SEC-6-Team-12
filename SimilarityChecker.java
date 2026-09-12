import java.util.*;

/**
 * Computes how similar two clauses are in content using TF-IDF
 * (Term Frequency - Inverse Document Frequency) vectors and Cosine
 * Similarity. This is used to find "related clauses" across
 * documents and to support conflict detection (comparing clauses
 * that talk about the same thing).
 */
public class SimilarityChecker {

    /** Tokenizes text into lowercase words, ignoring punctuation. */
    private static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        for (String word : text.toLowerCase().split("\\W+")) {
            if (!word.isBlank()) tokens.add(word);
        }
        return tokens;
    }

    /** Computes term frequency (TF) for a single clause's tokens. */
    private static Map<String, Double> computeTf(List<String> tokens) {
        Map<String, Double> tf = new HashMap<>();
        for (String token : tokens) {
            tf.merge(token, 1.0, Double::sum);
        }
        int total = tokens.size();
        for (String key : tf.keySet()) {
            tf.put(key, tf.get(key) / total);
        }
        return tf;
    }

    /**
     * Computes inverse document frequency (IDF) across all clauses
     * in the corpus, so common words (e.g. "the", "shall") count
     * for less than rare, meaningful words (e.g. "termination").
     */
    private static Map<String, Double> computeIdf(List<List<String>> allTokenLists) {
        Map<String, Double> idf = new HashMap<>();
        int totalDocs = allTokenLists.size();

        Map<String, Integer> docFrequency = new HashMap<>();
        for (List<String> tokens : allTokenLists) {
            Set<String> uniqueWords = new HashSet<>(tokens);
            for (String word : uniqueWords) {
                docFrequency.merge(word, 1, Integer::sum);
            }
        }

        for (String word : docFrequency.keySet()) {
            idf.put(word, Math.log((double) totalDocs / docFrequency.get(word)));
        }
        return idf;
    }

    /** Builds a TF-IDF vector (word -> score) for one clause. */
    private static Map<String, Double> tfIdfVector(Map<String, Double> tf, Map<String, Double> idf) {
        Map<String, Double> vector = new HashMap<>();
        for (String word : tf.keySet()) {
            vector.put(word, tf.get(word) * idf.getOrDefault(word, 0.0));
        }
        return vector;
    }

    /** Computes cosine similarity between two TF-IDF vectors: 0 (unrelated) to 1 (identical). */
    private static double cosineSimilarity(Map<String, Double> v1, Map<String, Double> v2) {
        Set<String> allWords = new HashSet<>();
        allWords.addAll(v1.keySet());
        allWords.addAll(v2.keySet());

        double dotProduct = 0.0, magnitude1 = 0.0, magnitude2 = 0.0;
        for (String word : allWords) {
            double a = v1.getOrDefault(word, 0.0);
            double b = v2.getOrDefault(word, 0.0);
            dotProduct += a * b;
            magnitude1 += a * a;
            magnitude2 += b * b;
        }

        if (magnitude1 == 0 || magnitude2 == 0) return 0.0;
        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }

    /**
     * Given a target clause and a list of candidate clauses, returns
     * the candidates ranked by similarity to the target (most similar
     * first). Used for "related clause" suggestions.
     */
    public static List<ScoredClause> findSimilarClauses(Clause target, List<Clause> candidates) {
        List<Clause> all = new ArrayList<>(candidates);
        if (!all.contains(target)) all.add(target);

        List<List<String>> allTokenLists = new ArrayList<>();
        for (Clause c : all) allTokenLists.add(tokenize(c.getText()));

        Map<String, Double> idf = computeIdf(allTokenLists);

        Map<String, Double> targetVector = tfIdfVector(computeTf(tokenize(target.getText())), idf);

        List<ScoredClause> results = new ArrayList<>();
        for (Clause candidate : candidates) {
            if (candidate == target) continue;
            Map<String, Double> candidateVector = tfIdfVector(computeTf(tokenize(candidate.getText())), idf);
            double score = cosineSimilarity(targetVector, candidateVector);
            results.add(new ScoredClause(candidate, score));
        }

        results.sort((a, b) -> Double.compare(b.score, a.score));
        return results;
    }

    /** Holder pairing a clause with its similarity score to some target clause. */
    public static class ScoredClause {
        public final Clause clause;
        public final double score;

        public ScoredClause(Clause clause, double score) {
            this.clause = clause;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("(similarity=%.3f) %s", score, clause);
        }
    }
}
