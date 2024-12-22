package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookTest {

    private static final String VALID_ID = "70";
    private static final String VALID_TITLE = "Winnie-the-Pooh (Winnie-the-Pooh #1)";
    private static final String VALID_AUTHOR = "A.A. Milne";
    private static final String VALID_DESCRIPTION =
            "The adventures of Christopher Robin and his friends in which Pooh Bear uses a balloon to get honey, " +
                    "Piglet meets a Heffalump, and Eeyore has a birthday.";
    private static final List<String> VALID_GENRES = List.of(
            "Classics", "Childrens", "Fiction", "Fantasy", "Animals", "Middle Grade", "Audiobook");
    private static final double VALID_RATING = 4.35;
    private static final int VALID_RATING_COUNT = 313861;
    private static final String VALID_URL = "https://www.goodreads.com/book/show/99107.Winnie_the_Pooh";

    private static final String VALID_GENRES_STRING = "[Classics, Childrens, Fiction, Fantasy, Animals, Middle Grade, Audiobook]";
    private static final String VALID_RATING_STRING = "4.35";
    private static final String VALID_RATING_COUNT_STRING = "313,861";

    @Test
    void testOfMethodWithValidInput() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        Book book = Book.of(tokens);

        assertEquals(VALID_ID, book.ID(), "Book ID does not match the expected value. Expected ID: "
                + VALID_ID + ", but got: " + book.ID());

        assertEquals(VALID_TITLE, book.title(), "Book title does not match the expected value. Expected title: "
                + VALID_TITLE + ", but got: " + book.title());

        assertEquals(VALID_AUTHOR, book.author(), "Book author does not match the expected value. Expected author: "
                + VALID_AUTHOR + ", but got: " + book.author());

        assertEquals(VALID_DESCRIPTION, book.description(), "Book description does not match the expected value. Expected description: "
                + VALID_DESCRIPTION + ", but got: " + book.description());

        assertIterableEquals(VALID_GENRES, book.genres(), "Book genres do not match the expected value. Expected genres: "
                + VALID_GENRES + ", but got: " + book.genres());

        assertEquals(VALID_RATING, book.rating(), "Book rating does not match the expected value. Expected rating: "
                + VALID_RATING + ", but got: " + book.rating());

        assertEquals(VALID_RATING_COUNT, book.ratingCount(), "Book rating count does not match the expected value. Expected rating count: "
                + VALID_RATING_COUNT + ", but got: " + book.ratingCount());

        assertEquals(VALID_URL, book.URL(), "Book URL does not match the expected value. Expected URL: "
                + VALID_URL + ", but got: " + book.URL());
    }

    @Test
    void testOfMethodWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> Book.of(null),
                "Expected IllegalArgumentException when 'null' tokens argument is passed to the 'of' method, but no exception was thrown.");
    }

    @Test
    void testOfMethodWithInvalidTokensLength() {
        String[] tokens = {VALID_ID, VALID_TITLE, VALID_AUTHOR};
        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the token array has an invalid length (not equal to the required length 8), but no exception was thrown.");
    }

    @Test
    void testOfMethodWithInvalidNonNumericRating() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                "invalid",
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the rating is invalid (non-numeric value), but no exception was thrown."
        );
    }

    @Test
    void testOfMethodWithInvalidNegativeRating() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                "-1",
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the rating is negative, but no exception was thrown."
        );
    }

    @Test
    void testOfMethodWithInvalidNonNumericRatingCount() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                VALID_RATING_STRING,
                "invalid",
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the rating count is invalid (non-numeric value), but no exception was thrown.");
    }

    @Test
    void testOfMethodWithInvalidNegativeRatingCount() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                VALID_RATING_STRING,
                "-1",
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the rating count is negative, but no exception was thrown.");
    }

    @Test
    void testOfMethodWithEmptyGenres() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                "[]",
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        Book book = Book.of(tokens);
        assertTrue(book.genres().isEmpty(), "Expected genres to be empty, but found: " + book.genres());
    }

    @Test
    void testOfMethodWithSingleGenre() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                "[Classics]",
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        Book book = Book.of(tokens);
        assertIterableEquals(List.of("Classics"), book.genres(), "Expected genres to be [Classics], but found: " + book.genres());
    }

    @Test
    void testOfMethodWithNullId() {
        String[] tokens = {
                null,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the book ID is null, but no exception was thrown.");
    }

    @Test
    void testOfMethodWithNullTitle() {
        String[] tokens = {
                VALID_ID,
                null,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the book title is null, but no exception was thrown.");
    }

    @Test
    void testOfMethodWithNullAuthor() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                null,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the book author is null, but no exception was thrown.");
    }

    @Test
    void testOfMethodWithNullDescription() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                null,
                VALID_GENRES_STRING,
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the book description is null, but no exception was thrown.");
    }

    @Test
    void testOfMethodWithNullGenresString() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                null,
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                VALID_URL
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the book genres string is null, but no exception was thrown.");
    }

    @Test
    void testOfMethodWithNullUrl() {
        String[] tokens = {
                VALID_ID,
                VALID_TITLE,
                VALID_AUTHOR,
                VALID_DESCRIPTION,
                VALID_GENRES_STRING,
                VALID_RATING_STRING,
                VALID_RATING_COUNT_STRING,
                null
        };

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Expected IllegalArgumentException when the book URL is null, but no exception was thrown.");
    }
    
}