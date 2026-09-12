import java.io.*;
import java.util.*;

/**
 * The core repository: stores LegalDocument objects and maintains
 * an in-memory inverted index (HashMap) mapping each word to the
 * clauses that contain it, for fast clause-level lookup.
 *
 * Persistence is done through plain Java object serialization to a
 * local file (repository.dat) — no external database is used.
 */
public class DocumentRepository {

    private static final String STORAGE_FILE = "repository.dat";

    private final List<LegalDocument> documents = new ArrayList<>();

    // Inverted index: word -> list of clauses containing that word
    private final Map<String, List<Clause>> invertedIndex = new HashMap<>();

    /** Adds a document to the repository and updates the inverted index. */
    public void addDocument(LegalDocument doc) {
        documents.add(doc);
        for (Clause clause : doc.getClauses()) {
            indexClause(clause);
        }
    }

    /** Breaks a clause into words and adds it to the inverted index. */
    private void indexClause(Clause clause) {
        String[] words = clause.getText().toLowerCase().split("\\W+");
        for (String word : words) {
            if (word.isBlank()) continue;
            invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(clause);
        }
    }

    /** Exact keyword search using the inverted index — O(1) average lookup. */
    public List<Clause> searchExactWord(String word) {
        return invertedIndex.getOrDefault(word.toLowerCase(), Collections.emptyList());
    }

    public List<LegalDocument> getAllDocuments() {
        return documents;
    }

    public List<Clause> getAllClauses() {
        List<Clause> all = new ArrayList<>();
        for (LegalDocument doc : documents) {
            all.addAll(doc.getClauses());
        }
        return all;
    }

    /** Saves the repository (all documents) to a local file. */
    @SuppressWarnings("unchecked")
    public void saveToDisk() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            out.writeObject(documents);
            System.out.println("Repository saved to " + STORAGE_FILE);
        } catch (IOException e) {
            System.out.println("Error saving repository: " + e.getMessage());
        }
    }

    /** Loads the repository from a local file, rebuilding the index. */
    @SuppressWarnings("unchecked")
    public void loadFromDisk() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            System.out.println("No existing repository file found — starting fresh.");
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            List<LegalDocument> loaded = (List<LegalDocument>) in.readObject();
            documents.clear();
            invertedIndex.clear();
            for (LegalDocument doc : loaded) {
                addDocument(doc);
            }
            System.out.println("Repository loaded from " + STORAGE_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading repository: " + e.getMessage());
        }
    }
}
