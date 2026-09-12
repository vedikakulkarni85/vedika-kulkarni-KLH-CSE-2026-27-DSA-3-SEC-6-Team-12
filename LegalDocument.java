import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a full legal document made up of multiple clauses.
 * Documents are split into clauses at creation time (by splitting
 * on blank lines / numbered clause markers) so the repository can
 * index and search at the clause level.
 */
public class LegalDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String uploadDate;
    private final List<Clause> clauses;

    public LegalDocument(String title, String uploadDate, String rawText) {
        this.title = title;
        this.uploadDate = uploadDate;
        this.clauses = splitIntoClauses(rawText);
    }

    /**
     * Splits raw document text into clauses. Clauses are assumed to be
     * separated by blank lines (a simple, robust heuristic for a course
     * project — real legal text is usually clause/paragraph separated).
     */
    private List<Clause> splitIntoClauses(String rawText) {
        List<Clause> result = new ArrayList<>();
        String[] parts = rawText.split("\\n\\s*\\n");
        int clauseNumber = 1;
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                String clauseId = title + "-C" + clauseNumber;
                result.add(new Clause(clauseId, title, clauseNumber, trimmed));
                clauseNumber++;
            }
        }
        return result;
    }

    public String getTitle() {
        return title;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public List<Clause> getClauses() {
        return clauses;
    }
}
