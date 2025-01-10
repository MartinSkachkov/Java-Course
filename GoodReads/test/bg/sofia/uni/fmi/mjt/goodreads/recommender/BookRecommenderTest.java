package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class BookRecommenderTest {

    @Mock
    private SimilarityCalculator mockCalculator;

    private BookRecommender recommender;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        book1 = new Book("1", "Book1", "Author1", "Description1", List.of("Fiction"), 4.5, 100, "url1");
        book2 = new Book("2", "Book2", "Author2", "Description2", List.of("Fiction"), 4.0, 150, "url2");
        book3 = new Book("3", "Book3", "Author3", "Description3", List.of("Technology"), 4.2, 120, "url3");

        Set<Book> books = Set.of(book1, book2, book3);
        recommender = new BookRecommender(books, mockCalculator);

        lenient().when(mockCalculator.calculateSimilarity(book1, book2)).thenReturn(0.8);
        lenient().when(mockCalculator.calculateSimilarity(book1, book3)).thenReturn(0.3);
        lenient().when(mockCalculator.calculateSimilarity(book2, book3)).thenReturn(0.4);
    }

    @Test
    void testRecommendBooksWithValidInput() {
        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 2);

        assertEquals(2, recommendations.size(), "The recommendation list should contain 2 books.");
        assertTrue(recommendations.containsKey(book2), "Book2 should be included in the recommendations.");
        assertTrue(recommendations.containsKey(book3), "Book3 should be included in the recommendations.");
        assertEquals(0.8, recommendations.get(book2), 0.001, "The similarity score for Book2 should be approximately 0.8.");
        assertEquals(0.3, recommendations.get(book3), 0.001, "The similarity score for Book3 should be approximately 0.3.");
    }

    @Test
    void testRecommendBooksWithMaxNGreaterThanAvailableBooks() {
        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 5);
        assertEquals(2, recommendations.size(), "When asking for more books than available, only the available books should be returned.");
    }

    @Test
    void testRecommendBooksWithNullBook() {
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(null, 2),
                "Should throw IllegalArgumentException when the book to recommend from is null.");
    }

    @Test
    void testRecommendBooksWithInvalidMaxN() {
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(book1, 0),
                "Should throw IllegalArgumentException when the max number of recommendations is 0.");
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(book1, -1),
                "Should throw IllegalArgumentException when the max number of recommendations is negative.");
    }

    @Test
    void testRecommendBooksOrder() {
        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 2);

        var entries = recommendations.entrySet().toArray();
        assertTrue(((Map.Entry<Book, Double>) entries[0]).getValue() > ((Map.Entry<Book, Double>) entries[1]).getValue(),
                "The books in the recommendation list should be ordered by similarity score, with the highest score first.");
    }
}
