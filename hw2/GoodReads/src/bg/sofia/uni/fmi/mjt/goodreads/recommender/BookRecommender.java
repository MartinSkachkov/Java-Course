package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class BookRecommender implements BookRecommenderAPI {
    private final Set<Book> initialBooks;
    private final SimilarityCalculator calculator;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        validateInput(initialBooks, calculator);

        this.initialBooks = initialBooks;
        this.calculator = calculator;
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        validateRecommendBooksParameters(origin, maxN);

        Map<Book, Double> unsortedSimilarBooks = new HashMap<>();

        for (Book book : initialBooks) {
            if (book.equals(origin)) {
                continue;
            }

            double similarityScore = calculator.calculateSimilarity(origin, book);
            unsortedSimilarBooks.put(book, similarityScore);
        }

        SortedMap<Book, Double> sortedSimilarBooks = new TreeMap<>(new BookSimilarityComparator(unsortedSimilarBooks));
        sortedSimilarBooks.putAll(unsortedSimilarBooks);

        return getTopEntries(sortedSimilarBooks, maxN);
    }

    private void validateInput(Set<Book> initialBooks, SimilarityCalculator calculator) {
        if (initialBooks == null || initialBooks.isEmpty()) {
            throw new IllegalArgumentException("The initialBooks set cannot be null or empty.");
        }

        if (calculator == null) {
            throw new IllegalArgumentException("The similarity calculator cannot be null.");
        }
    }

    private void validateRecommendBooksParameters(Book origin, int maxN) {
        if (origin == null) {
            throw new IllegalArgumentException("Origin book cannot be null.");
        }

        if (maxN <= 0) {
            throw new IllegalArgumentException("maxN must be greater than 0.");
        }
    }

    private SortedMap<Book, Double> getTopEntries(SortedMap<Book, Double> sortedMap, int maxN) {
        SortedMap<Book, Double> topEntries = new TreeMap<>(sortedMap.comparator());
        int count = 0;

        for (Map.Entry<Book, Double> entry : sortedMap.entrySet()) {
            if (count >= maxN) {
                break;
            }

            topEntries.put(entry.getKey(), entry.getValue());
            count++;
        }

        return topEntries;
    }
}