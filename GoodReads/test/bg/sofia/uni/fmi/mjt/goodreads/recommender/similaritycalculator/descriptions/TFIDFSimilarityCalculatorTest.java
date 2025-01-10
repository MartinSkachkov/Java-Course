package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TFIDFSimilarityCalculatorTest {
    
    @Mock
    private TextTokenizer mockTokenizer;

    private TFIDFSimilarityCalculator calculator;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        book1 = new Book("1", "Book1", "Author1", "cats dogs animals", List.of("Fiction"), 4.5, 100, "url1");
        book2 = new Book("2", "Book2", "Author2", "cats birds pets", List.of("Fiction"), 4.0, 150, "url2");
        book3 = new Book("3", "Book3", "Author3", "technology computers software", List.of("Technology"), 4.2, 120, "url3");

        lenient().when(mockTokenizer.tokenize("cats dogs animals")).thenReturn(List.of("cats", "dogs", "animals"));
        lenient().when(mockTokenizer.tokenize("cats birds pets")).thenReturn(List.of("cats", "birds", "pets"));
        lenient().when(mockTokenizer.tokenize("technology computers software")).thenReturn(List.of("technology", "computers", "software"));

        calculator = new TFIDFSimilarityCalculator(Set.of(book1, book2, book3), mockTokenizer);
    }

    @Test
    void testCalculateSimilarityWithSimilarBooks() {
        double similarity = calculator.calculateSimilarity(book1, book2);
        assertEquals(0.064, similarity, 0.001, "Cosine similarity between book1 and book2 should be approximately 0.064");
    }

    @Test
    void testCalculateSimilarityWithDifferentBooks() {
        double similarity = calculator.calculateSimilarity(book1, book3);
        assertEquals(0.0, similarity, 0.001, "Similarity should be 0.0 for books with no shared words");
    }

    @Test
    void testCalculateSimilarityWithSameBook() {
        double similarity = calculator.calculateSimilarity(book1, book1);
        assertEquals(1.0, similarity, 0.001, "Similarity should be 1.0 when comparing a book with itself");
    }

    @Test
    void testCalculateSimilarityWithNullBooks() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(null, book1),
                "Should throw IllegalArgumentException when the first book is null");

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(book1, null),
                "Should throw IllegalArgumentException when the second book is null");
    }

}
