package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenresOverlapSimilarityCalculatorTest {

    private static GenresOverlapSimilarityCalculator calculator;

    @BeforeAll
    static void setUp() {
        calculator = new GenresOverlapSimilarityCalculator();
    }

    @Test
    void testCalculateSimilarityWithNullFirstBook() {
        Book book = new Book(
                "id", "Test Title", "Test Author", "Test Description",
                List.of("Fiction"), 4.5, 100, "http://test.url"
        );

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(null, book),
                "Should throw IllegalArgumentException when first book is null");
    }

    @Test
    void testCalculateSimilarityWithNullSecondBook() {
        Book book = new Book(
                "id", "Test Title", "Test Author", "Test Description",
                List.of("Fiction"), 4.5, 100, "http://test.url"
        );

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(book, null),
                "Should throw IllegalArgumentException when second book is null");
    }

    @Test
    void testCalculateSimilarityWithIdenticalGenres() {
        Book book1 = new Book(
                "id1", "Test Title 1", "Test Author", "Test Description",
                List.of("Fiction", "Fantasy", "Adventure"), 4.5, 100, "http://test.url"
        );
        Book book2 = new Book(
                "id2", "Test Title 2", "Test Author", "Test Description",
                List.of("Fiction", "Fantasy", "Adventure"), 4.5, 100, "http://test.url"
        );

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(1.0, similarity, 0.001, "Similarity should be 1.0 for books with identical genres");
    }

    @Test
    void testCalculateSimilarityWithNoOverlap() {
        Book book1 = new Book(
                "id1", "Test Title 1", "Test Author", "Test Description",
                List.of("Fiction", "Fantasy"), 4.5, 100, "http://test.url"
        );
        Book book2 = new Book(
                "id2", "Test Title 2", "Test Author", "Test Description",
                List.of("NonFiction", "Biography"), 4.5, 100, "http://test.url"
        );

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.0, similarity, 0.001, "Similarity should be 0.0 for books with no overlapping genres");
    }

    @Test
    void testCalculateSimilarityWithPartialOverlap() {
        Book book1 = new Book(
                "id1", "Test Title 1", "Test Author", "Test Description",
                List.of("Fiction", "Fantasy", "Adventure"), 4.5, 100, "http://test.url"
        );
        Book book2 = new Book(
                "id2", "Test Title 2", "Test Author", "Test Description",
                List.of("Fiction", "Fantasy", "Romance"), 4.5, 100, "http://test.url"
        );

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(2.0 / 3.0, similarity, 0.001, "Similarity should be 2/3 when 2 out of 3 genres overlap");
    }

    @Test
    void testCalculateSimilarityWithDifferentSizeLists() {
        Book book1 = new Book(
                "id1", "Test Title 1", "Test Author", "Test Description",
                List.of("Fiction", "Fantasy", "Adventure", "Romance"), 4.5, 100, "http://test.url"
        );
        Book book2 = new Book(
                "id2", "Test Title 2", "Test Author", "Test Description",
                List.of("Fiction", "Fantasy"), 4.5, 100, "http://test.url"
        );

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(1.0, similarity, 0.001, "Similarity should be 1.0 when all genres of smaller list are contained in larger list");
    }

    @Test
    void testCalculateSimilarityWithEmptyGenres() {
        Book book1 = new Book(
                "id1", "Test Title 1", "Test Author", "Test Description",
                List.of(), 4.5, 100, "http://test.url"
        );
        Book book2 = new Book(
                "id2", "Test Title 2", "Test Author", "Test Description",
                List.of("Fiction", "Fantasy"), 4.5, 100, "http://test.url"
        );

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.0, similarity, 0.001, "Similarity should be 0.0 when one book has no genres");
    }

}
