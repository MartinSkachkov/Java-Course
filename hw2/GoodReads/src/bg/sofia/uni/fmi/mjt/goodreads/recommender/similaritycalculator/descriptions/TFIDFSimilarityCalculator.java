package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {
    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        validateInput(books, tokenizer);

        this.books = books;
        this.tokenizer = tokenizer;
    }

    private void validateInput(Set<Book> books, TextTokenizer tokenizer) {
        if (books == null || tokenizer == null) {
            throw new IllegalArgumentException("Books set or Tokenizer cannot be null.");
        }
    }

    /*
     * Do not modify!
     */
    @Override
    public double calculateSimilarity(Book first, Book second) {
        booksNotNullValidation(first, second);

        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        bookNotNullValidation(book);

        Map<String, Double> tf = computeTF(book);
        Map<String, Double> idf = computeIDF(book);

        Map<String, Double> tfIdf = new HashMap<>();

        for (String word : tf.keySet()) {
            double tfIdfScore = tf.get(word) * idf.get(word);
            tfIdf.put(word, tfIdfScore);
        }

        return tfIdf;
    }

    public Map<String, Double> computeTF(Book book) {
        bookNotNullValidation(book);

        List<String> tokens = tokenizer.tokenize(book.description());
        Map<String, Double> termFrequency = new HashMap<>();

        for (String token : tokens) {
            termFrequency.put(token, termFrequency.getOrDefault(token, 0.0) + 1);
        }

        double totalWords = tokens.size();
        for (String word : termFrequency.keySet()) {
            termFrequency.put(word, termFrequency.get(word) / totalWords);
        }

        return termFrequency;
    }

    public Map<String, Double> computeIDF(Book book) {
        bookNotNullValidation(book);

        Map<String, Double> idf = new HashMap<>();
        int totalBooks = books.size();

        List<String> wordsInDescription = tokenizer.tokenize(book.description());
        Set<String> uniqueWordsInDescription = new HashSet<>(wordsInDescription);

        for (String word : uniqueWordsInDescription) {
            long booksWithWord = books.stream()
                    .filter(b -> tokenizer.tokenize(b.description()).contains(word))
                    .count();

            double idfValue = Math.log10((double) totalBooks / booksWithWord);
            idf.put(word, idfValue);
        }

        return idf;
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        validateMapsNotNull(first, second);

        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        validateMapsNotNull(first, second);

        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
                .mapToDouble(word -> first.get(word) * second.get(word))
                .sum();
    }

    private double magnitude(Collection<Double> input) {
        validateCollectionNotNull(input);

        double squaredMagnitude = input.stream()
                .map(v -> v * v)
                .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }

    private void bookNotNullValidation(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("The book cannot be null.");
        }
    }

    private void booksNotNullValidation(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Books cannot be null");
        }
    }

    private void validateMapsNotNull(Map<String, Double> first, Map<String, Double> second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("The first or second map cannot be null");
        }
    }

    private void validateCollectionNotNull(Collection<Double> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("The collection cannot be null");
        }
    }
}