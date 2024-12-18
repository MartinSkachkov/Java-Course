package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Set;
import java.util.SortedMap;

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
}
