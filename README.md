# LexVault 

### Legal Document Repository using Advanced Algorithms

LexVault is a simple Java terminal project that I made to use Advanced Algorithms on a real-world type of problem.

The idea is pretty simple. We have a folder containing different legal documents, and the program lets us search, compare, and find useful information from them.

## What does it do?

The program can:

- Show all the legal documents
- Add a new document
- Search for an exact word or phrase
- Find a word even if it has a spelling mistake
- Search for multiple legal clauses at once
- Compare two documents
- Find similar documents
- Explain the algorithms used in the project

## Why legal documents?

Legal documents can be quite long, and finding one particular word or clause manually can take time.

For example, if I want to find all the places where **"confidentiality"** is mentioned, I can just search for it instead of going through the whole document.

The interesting part is that different types of searches can be solved using different algorithms.

## Algorithms Used

### KMP

I use **KMP (Knuth-Morris-Pratt)** for exact phrase searching.

For example, if the user searches for:

```text
confidentiality
```

KMP finds where that phrase occurs in the documents.

**Time Complexity:** O(n + m)

---

### Levenshtein Distance

This is used for fuzzy searching.

For example, if the user types:

```text
confidenciality
```

the program can find:

```text
confidentiality
```

It checks how many changes are needed to turn one word into the other.

This is done using **Dynamic Programming**.

---

### Aho-Corasick

Sometimes we want to search for many things at the same time.

For example:

```text
termination
confidentiality
arbitration
liability
indemnity
```

Instead of searching for each one separately, I use **Aho-Corasick** to search for multiple patterns together.

---

### Dynamic Programming

Dynamic Programming is used when comparing two documents.

The program calculates the **edit distance** between them and uses it to give an approximate similarity percentage.

---

### Suffix Array and LCP

The project also contains a Suffix Array and LCP implementation.

These are useful for working with common parts of text and understanding how similar pieces of documents can be found.

## How the project is connected to the syllabus

This project mainly uses topics from **Module 1, Module 2 and Module 3**.

```text
Module 1
TextHack idea
      ↓
Different queries → Different algorithms

Module 2
String Algorithms
      ↓
KMP
Aho-Corasick
Suffix Array
LCP

Module 3
Dynamic Programming
      ↓
Levenshtein Distance
Document Comparison
```

## Project Structure

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
│   ├── EmploymentAgreement.txt
│   ├── NDA.txt
│   ├── ServiceAgreement.txt
│   ├── SoftwareLicense.txt
│   └── CourtJudgment.txt
│
└── README.md
```

## Running the Project

You just need Java and VS Code.

Open the LexVault folder in VS Code and open the terminal.

Compile everything:

```bash
javac -d out Main.java LegalDocument.java DocumentRepository.java algorithms/*.java
```

Then run:

```bash
java -cp out Main
```

## Main Menu

```text
1. View All Legal Documents
2. Add Legal Document
3. Search Exact Phrase
4. Fuzzy Search
5. Scan Legal Clauses
6. Compare Two Documents
7. Find Similar Documents
8. View Document
9. Algorithm Information
0. Exit
```

## Sample Documents

I have included a few sample documents so the project can be tested immediately:

- Employment Agreement
- NDA
- Service Agreement
- Software License Agreement
- Court Judgment

The documents are only sample/fictitious documents for the project.

## What I learned from this project

The main thing I learned from this project is that there isn't one algorithm that is best for everything.

For example:

```text
Exact search       → KMP
Spelling mistakes  → Levenshtein
Multiple searches  → Aho-Corasick
Document comparison → Dynamic Programming
Common text        → Suffix Array + LCP
```

So the basic idea behind LexVault is:

> **Choose the right algorithm for the problem.**

## Future Ideas

If I continue working on this project, I could add:

- PDF support
- DOCX support
- A proper database
- Better document similarity
- Citation searching
- More algorithms from the remaining modules

---

### Built With

**Java | VS Code | Advanced Algorithms**
