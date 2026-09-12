import java.io.Serializable;

/**
 * Represents a single clause extracted from a legal document.
 * A document is broken down into multiple Clause objects so that
 * search and comparison can happen at the clause level instead of
 * the whole-document level.
 */
public class Clause implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String clauseId;
    private final String documentTitle;
    private final int clauseNumber;
    private final String text;

    public Clause(String clauseId, String documentTitle, int clauseNumber, String text) {
        this.clauseId = clauseId;
        this.documentTitle = documentTitle;
        this.clauseNumber = clauseNumber;
        this.text = text;
    }

    public String getClauseId() {
        return clauseId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public int getClauseNumber() {
        return clauseNumber;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "[" + documentTitle + " - Clause " + clauseNumber + "] " + text;
    }
}
