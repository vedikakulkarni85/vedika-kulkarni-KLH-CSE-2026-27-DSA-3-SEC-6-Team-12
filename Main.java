import java.util.List;
import java.util.Scanner;

/**
 * Demo driver for Review-2: shows the repository storing documents,
 * and demonstrates pattern matching (KMP), fuzzy search (Levenshtein),
 * and similarity scoring (TF-IDF + Cosine Similarity) all working on
 * the same in-memory clause data. After the fixed demo output, an
 * interactive menu lets the user type their own search terms live.
 */
public class Main {

    public static void main(String[] args) {

        // ---------- 1. REPOSITORY CREATION ----------
        System.out.println("===== 1. Repository: Adding Documents =====");
        DocumentRepository repo = new DocumentRepository();

        String contractA =
            "This Agreement is made between Party A and Party B.\n\n" +
            "Payment shall be made within 30 days of invoice date.\n\n" +
            "Either party may terminate this agreement with 60 days written notice.\n\n" +
            "Confidential information shall not be disclosed to third parties.";

        String contractB =
            "This Agreement is made between Party A and Party C.\n\n" +
            "Payment shall be made within 45 days of invoice date.\n\n" +
            "Either party may terminate this agreement with 30 days written notice.\n\n" +
            "All confidential information must remain protected from disclosure.";

        repo.addDocument(new LegalDocument("Contract_A_v1", "2026-01-10", contractA));
        repo.addDocument(new LegalDocument("Contract_B_v1", "2026-02-15", contractB));

        System.out.println("Documents in repository: " + repo.getAllDocuments().size());
        System.out.println("Total clauses indexed: " + repo.getAllClauses().size());
        repo.saveToDisk();

        // ---------- 2. PATTERN MATCHING (KMP) ----------
        System.out.println("\n===== 2. Pattern Matching (KMP) =====");
        String sampleClause = repo.getAllClauses().get(1).getText();
        System.out.println("Searching clause: \"" + sampleClause + "\"");
        List<Integer> matches = PatternMatcher.search(sampleClause, "days");
        System.out.println("Occurrences of \"days\" found at index positions: " + matches);

        // ---------- 3. FUZZY SEARCH (Levenshtein) ----------
        System.out.println("\n===== 3. Fuzzy Search (Levenshtein Edit Distance) =====");
        String typoQuery = "paymnt"; // deliberate typo for "payment"
        System.out.println("Searching (with typo) for: \"" + typoQuery + "\"");
        List<FuzzySearch.FuzzyMatch> fuzzyResults =
            FuzzySearch.search(repo.getAllClauses(), typoQuery, 2);
        for (FuzzySearch.FuzzyMatch match : fuzzyResults) {
            System.out.println("  " + match);
        }

        // ---------- 4. SIMILARITY (TF-IDF + Cosine Similarity) ----------
        System.out.println("\n===== 4. Similarity Detection (TF-IDF + Cosine Similarity) =====");
        Clause paymentClauseA = repo.getAllClauses().get(1); // Contract_A payment clause
        System.out.println("Target clause: " + paymentClauseA);
        List<SimilarityChecker.ScoredClause> similar =
            SimilarityChecker.findSimilarClauses(paymentClauseA, repo.getAllClauses());
        System.out.println("Most similar clauses:");
        for (SimilarityChecker.ScoredClause sc : similar) {
            System.out.println("  " + sc);
        }

        System.out.println("\nNote: the top match above is Contract_B's payment clause -");
        System.out.println("same topic (payment terms) but a DIFFERENT number of days.");
        System.out.println("This is exactly the kind of pair a conflict-detection pass would flag.");

        // ---------- 5. INTERACTIVE MODE (live user input) ----------
        runInteractiveMenu(repo);
    }

    /**
     * Lets the user type their own search terms live from the console,
     * running the same three algorithms (pattern match, fuzzy search,
     * similarity) against whatever they type instead of fixed demo data.
     */
    private static void runInteractiveMenu(DocumentRepository repo) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Interactive Search Menu =====");
            System.out.println("1. Pattern Match (exact search, KMP)");
            System.out.println("2. Fuzzy Search (typo-tolerant, Levenshtein)");
            System.out.println("3. Find Similar Clauses (TF-IDF + Cosine)");
            System.out.println("4. Exit");
            System.out.print("Choose an option (1-4): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": {
                    System.out.print("Enter a word or phrase to search for: ");
                    String query = scanner.nextLine().trim();
                    boolean found = false;
                    for (Clause clause : repo.getAllClauses()) {
                        List<Integer> matches = PatternMatcher.search(clause.getText(), query);
                        if (!matches.isEmpty()) {
                            found = true;
                            System.out.println("  Found in " + clause + "  -> positions: " + matches);
                        }
                    }
                    if (!found) System.out.println("  No exact matches found.");
                    break;
                }
                case "2": {
                    System.out.print("Enter a word to fuzzy search for: ");
                    String query = scanner.nextLine().trim();
                    List<FuzzySearch.FuzzyMatch> results = FuzzySearch.search(repo.getAllClauses(), query, 2);
                    if (results.isEmpty()) {
                        System.out.println("  No close matches found within edit distance 2.");
                    } else {
                        for (FuzzySearch.FuzzyMatch match : results) {
                            System.out.println("  " + match);
                        }
                    }
                    break;
                }
                case "3": {
                    List<Clause> allClauses = repo.getAllClauses();
                    System.out.println("  Pick a clause number to compare against:");
                    for (int i = 0; i < allClauses.size(); i++) {
                        System.out.println("   " + i + ": " + allClauses.get(i));
                    }
                    System.out.print("  Enter clause number: ");
                    String indexInput = scanner.nextLine().trim();
                    try {
                        int idx = Integer.parseInt(indexInput);
                        if (idx < 0 || idx >= allClauses.size()) {
                            System.out.println("  Invalid clause number.");
                            break;
                        }
                        Clause target = allClauses.get(idx);
                        List<SimilarityChecker.ScoredClause> similar =
                            SimilarityChecker.findSimilarClauses(target, allClauses);
                        System.out.println("  Most similar clauses to: " + target);
                        for (SimilarityChecker.ScoredClause sc : similar) {
                            System.out.println("    " + sc);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("  Please enter a valid number.");
                    }
                    break;
                }
                case "4":
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("  Invalid option, please choose 1-4.");
            }
        }
    }
}
