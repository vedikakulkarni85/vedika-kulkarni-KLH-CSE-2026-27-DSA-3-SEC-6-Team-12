# ⚖️ LexVault

### Legal Document Repository Using Advanced Algorithms

LexVault is a **Java-based terminal application** that stores, searches, compares, and analyses legal documents using concepts from **Advanced Algorithms**.

The project demonstrates how different algorithmic techniques can be applied to different types of text-processing problems in a legal document repository.

---

## 📌 Problem Statement

Legal repositories can contain many documents such as agreements, contracts, NDAs, licenses, and court judgments.

Finding specific information manually can be time-consuming, especially when users need to:

- Search for exact phrases
- Search despite spelling mistakes
- Find multiple legal clauses
- Compare two documents
- Find similar documents

LexVault solves these problems by using different algorithms for different types of queries.

---

## 💡 Key Idea

The main idea of LexVault is:

> **Different questions require different algorithms.**

| User Requirement | Algorithm Used |
|---|---|
| Find an exact phrase | KMP |
| Find a word with spelling mistakes | Levenshtein Distance |
| Find multiple clauses at once | Aho-Corasick |
| Compare two documents | Dynamic Programming |
| Find common/similar text | Suffix Array + LCP |

---

## 🚀 Features

### 🔎 Exact Phrase Search

Search for an exact word or phrase across all legal documents using the **Knuth-Morris-Pratt (KMP)** algorithm.

Example:

```text
Search: confidentiality
```

The system displays the documents and positions where the phrase occurs.

**Complexity:** `O(n + m)`

---

### ✏️ Fuzzy Search

Find the closest matching word even when the user makes a spelling mistake.

Example:

```text
Input:
confidenciality

Possible match:
confidentiality
```

Implemented using **Levenshtein Distance** and Dynamic Programming.

**Complexity:** `O(n × m)`

---

### 🔍 Legal Clause Scanner

Search for multiple important legal clauses simultaneously.

Example clauses:

- Termination
- Confidentiality
- Arbitration
- Indemnity
- Liability
- Force Majeure
- Governing Law
- Intellectual Property

Implemented using the **Aho-Corasick algorithm**.

---

### 📑 Document Comparison

Compare two legal documents and calculate their edit distance and similarity.

The feature demonstrates **Dynamic Programming** and concepts related to sequence alignment.

---

### 🧩 Document Similarity

Compare a selected document with other documents in the repository to identify documents with similar text.

The project also includes a **Suffix Array and LCP implementation** for advanced substring analysis.

---

### 🧪 Algorithm Lab

LexVault contains an educational Algorithm Lab explaining:

- KMP
- Levenshtein Distance
- Aho-Corasick
- Suffix Array
- LCP
- Dynamic Programming

For each algorithm, the application displays its purpose and complexity.

---

## 🏗️ Project Structure

```text
LexVault/
│
├── Main.java
├── LegalDocument.java
├── DocumentRepository.java
│
├── algorithms/
│   ├── KMP.java
│   ├── Levenshtein.java
│   ├── AhoCorasick.java
│   └── SuffixArray.java
│
├── documents/
│   ├──
