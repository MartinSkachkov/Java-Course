package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BookFinderTest {

    private final Book book1 = new Book("1", "Book One", "Author A", "Description of Book One. More.", List.of("Fantasy", "Adventure"), 4.5, 100, "url1");
    private final Book book2 = new Book("2", "Book Two", "Author B", "Description of Book Two", List.of("Fantasy"), 4.0, 150, "url2");
    private final Book book3 = new Book("3", "Book Three", "Author A", "Description of Book Three", List.of("Adventure", "Science Fiction"), 4.7, 120, "url3");
    private final Book book4 = new Book("4", "Book Four", "Author М", "Description of Book Four", List.of(), 4.3, 80, "url4");

    private BookFinder bookFinder;
    private TextTokenizer tokenizer;
    private Set<Book> books;

    @BeforeEach
    void setUp() {
        books = new HashSet<>(List.of(book1, book2, book3, book4));

        String stopwords = "a" + System.lineSeparator() + "an" + System.lineSeparator() + "the" + System.lineSeparator() +
                "of" + System.lineSeparator() + "for" + System.lineSeparator() + "with" + System.lineSeparator();
        tokenizer = new TextTokenizer(new StringReader(stopwords));

        bookFinder = new BookFinder(books, tokenizer);
    }

    @Test
    void testConstructorWithInvalidBooks() {
        assertThrows(IllegalArgumentException.class, () -> new BookFinder(null, tokenizer), "Books set cannot be null or empty");
    }

    @Test
    void testConstructorWithInvalidTokenizer() {
        assertThrows(IllegalArgumentException.class, () -> new BookFinder(books, null), "TextTokenizer cannot be null");
    }

    @Test
    void testSearchByAuthorWithInvalidAuthorName() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(""), "Author name cannot be null or empty");
    }

    @Test
    void testSearchByAuthorWithValidAuthor() {
        List<Book> result = bookFinder.searchByAuthor("Author A");

        assertEquals(2, result.size(), "There should be 2 books by Author A");
        assertTrue(result.stream().allMatch(book -> book.author().equals("Author A")), "All books should have the author 'Author A'");
    }

    @Test
    void testSearchByAuthorWithInvalidAuthor() {
        List<Book> result = bookFinder.searchByAuthor("Author C");
        assertTrue(result.isEmpty(), "There should be no books by Author C");
    }

    @Test
    void testSearchByGenresByMatchAll() {
        Set<String> genres = new HashSet<>(List.of("Fantasy"));
        List<Book> result = bookFinder.searchByGenres(genres, MatchOption.MATCH_ALL);

        assertEquals(2, result.size(), "There should be 2 books that match all the genres");
        assertTrue(result.stream().allMatch(book -> book.genres().containsAll(genres)), "All books should match all the genres");
    }

    @Test
    void testSearchByGenresByMatchAny() {
        Set<String> genres = new HashSet<>(List.of("Adventure", "Drama"));
        List<Book> result = bookFinder.searchByGenres(genres, MatchOption.MATCH_ANY);

        assertEquals(2, result.size(), "There should be 2 books that match any of the genres");
        assertTrue(result.stream().anyMatch(book -> book.genres().contains("Adventure")), "At least one genre should match");
    }

    @Test
    void testSearchByGenresByMatchAllButNoMatches() {
        Set<String> genres = new HashSet<>(List.of("Fantasy", "Adventure", "Drama"));
        List<Book> result = bookFinder.searchByGenres(genres, MatchOption.MATCH_ALL);

        assertEquals(0, result.size(), "There should be 0 books that match all the genres");
        assertTrue(result.isEmpty(), "Result list should be empty");
    }

    @Test
    void testSearchByGenresByMatchAnyButNoMatches() {
        Set<String> genres = new HashSet<>(List.of("Drama"));
        List<Book> result = bookFinder.searchByGenres(genres, MatchOption.MATCH_ANY);

        assertEquals(0, result.size(), "There should be 0 books that match any of the genres");
        assertTrue(result.isEmpty(), "Result list should be empty");
    }

    @Test
    void testSearchByGenresByMatchAllWithEmptyGenresList() {
        Set<String> genres = new HashSet<>(List.of());
        List<Book> result = bookFinder.searchByGenres(genres, MatchOption.MATCH_ALL);

        assertEquals(1, result.size(), "There should be 1 book that matches empty genres");
    }

    @Test
    void testSearchByGenresByMatchAnyWithEmptyGenresList() {
        Set<String> genres = new HashSet<>(List.of());
        List<Book> result = bookFinder.searchByGenres(genres, MatchOption.MATCH_ANY);

        assertEquals(1, result.size(), "There should be 1 book that matches empty genres");
    }

    @Test
    void testSearchByKeywordsMatchAll() {
        Set<String> keywords = new HashSet<>(List.of("description", "book"));

        List<Book> result = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ALL);

        assertEquals(4, result.size(), "There should be 4 books matching all keywords");
        assertTrue(result.contains(book1), "Book One should match all keywords");
        assertTrue(result.contains(book2), "Book Two should match all keywords");
        assertTrue(result.contains(book3), "Book Two should match all keywords");
        assertTrue(result.contains(book4), "Book Two should match all keywords");
    }

    @Test
    void testSearchByKeywordsNoMatches() {
        Set<String> keywords = new HashSet<>(List.of("nonexistent"));
        List<Book> result = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ANY);

        assertTrue(result.isEmpty(), "There should be no books matching the keywords");
    }

    @Test
    void testSearchByKeywordsMatchAny() {
        Set<String> keywords = new HashSet<>(List.of("book", "fantasy"));

        List<Book> result = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ANY);

        assertEquals(4, result.size(), "There should be 4 books matching any of the keywords");
        assertTrue(result.contains(book1), "Book One should match any keyword");
        assertTrue(result.contains(book2), "Book Two should match any keyword");
        assertTrue(result.contains(book3), "Book Three should match any keyword");
        assertTrue(result.contains(book4), "Book Three should match any keyword");
    }

    @Test
    void testSearchByKeywordsWithEmptyKeywords() {
        Set<String> keywords = new HashSet<>();

        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ALL),
                "Keywords cannot be null or empty");
    }

    @Test
    void testAllGenres() {
        Set<String> expectedGenres = new HashSet<>(List.of("Fantasy", "Adventure", "Science Fiction"));
        Set<String> actualGenres = bookFinder.allGenres();

        assertIterableEquals(expectedGenres, actualGenres, "All genres should be returned correctly");
    }
}
