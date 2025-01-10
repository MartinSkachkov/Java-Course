package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;

import java.util.Comparator;
import java.util.Map;

public class BookSimilarityComparator implements Comparator<Book> {
    private final Map<Book, Double> scores;

    public BookSimilarityComparator(Map<Book, Double> scores) {
        this.scores = scores;
    }

    @Override
    public int compare(Book b1, Book b2) {
        int scoreComparison = Double.compare(scores.get(b2), scores.get(b1));
        return scoreComparison != 0 ? scoreComparison : b1.title().compareTo(b2.title());
    }
}