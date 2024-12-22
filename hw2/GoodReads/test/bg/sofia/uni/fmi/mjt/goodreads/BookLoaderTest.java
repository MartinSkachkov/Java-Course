package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookLoaderTest {

    private static final String CSV_DATA = """
                N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL
                0,To Kill a Mockingbird,Harper Lee,"The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it. ""To Kill A Mockingbird"" became both an instant bestseller and a critical success when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later made into an Academy Award-winning film, also a classic.Compassionate, dramatic, and deeply moving, ""To Kill A Mockingbird"" takes readers to the roots of human behavior - to innocence and experience, kindness and cruelty, love and hatred, humor and pathos. Now with over 18 million copies in print and translated into forty languages, this regional story by a young Alabama woman claims universal appeal. Harper Lee always considered her book to be a simple love story. Today it is regarded as a masterpiece of American literature.","['Classics', 'Fiction', 'Historical Fiction', 'School', 'Literature', 'Young Adult', 'Historical']",4.27,"5,691,311",https://www.goodreads.com/book/show/2657.To_Kill_a_Mockingbird
                1,"Harry Potter and the Philosopher’s Stone (Harry Potter, #1)",J.K. Rowling,"Harry Potter thinks he is an ordinary boy - until he is rescued by an owl, taken to Hogwarts School of Witchcraft and Wizardry, learns to play Quidditch and does battle in a deadly duel. The Reason ... HARRY POTTER IS A WIZARD!","['Fantasy', 'Fiction', 'Young Adult', 'Magic', 'Childrens', 'Middle Grade', 'Classics']",4.47,"9,278,135",https://www.goodreads.com/book/show/72193.Harry_Potter_and_the_Philosopher_s_Stone
                2,Pride and Prejudice,Jane Austen,"Since its immediate success in 1813, Pride and Prejudice has remained one of the most popular novels in the English language. Jane Austen called this brilliant work ""her own darling child"" and its vivacious heroine, Elizabeth Bennet, ""as delightful a creature as ever appeared in print."" The romantic clash between the opinionated Elizabeth and her proud beau, Mr. Darcy, is a splendid performance of civilized sparring. And Jane Austen's radiant wit sparkles as her characters dance a delicate quadrille of flirtation and intrigue, making this book the most superb comedy of manners of Regency England.Alternate cover edition of ISBN 9780679783268","['Classics', 'Fiction', 'Romance', 'Historical Fiction', 'Literature', 'Historical', 'Audiobook']",4.28,"3,944,155",https://www.goodreads.com/book/show/1885.Pride_and_Prejudice
            """;
    private static final String EMPTY_CSV_DATA = "N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL";
    private static final String INCOMPLETE_CSV_DATA = """
            N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL
            3,Incomplete Book,Author,Description,,4.0,,https://www.goodreads.com/book/show/12345.Incomplete_Book
            """;

    @Test
    void testLoadBooks() {
        Reader reader = new StringReader(CSV_DATA);
        Set<Book> books = BookLoader.load(reader);

        assertNotNull(books, "The returned book set should not be null.");
        assertEquals(3, books.size(), "The book set should contain three books.");

        books.forEach(this::assertBookPropertiesNotNull);

        assertTrue(books.stream().anyMatch(b -> b.title().equals("To Kill a Mockingbird")),
                "Should contain the book 'To Kill a Mockingbird'");
        assertTrue(books.stream().anyMatch(b -> b.title().equals("Harry Potter and the Philosopher’s Stone (Harry Potter, #1)")),
                "Should contain the book 'Harry Potter and the Philosopher’s Stone (Harry Potter, #1)'");
        assertTrue(books.stream().anyMatch(b -> b.title().equals("Pride and Prejudice")),
                "Should contain the book 'Pride and Prejudice'");
    }

    @Test
    void testLoadBooksWithNullReader() {
        assertThrows(IllegalArgumentException.class, () -> BookLoader.load(null),
                "Loading books with null reader should throw IllegalArgumentException.");
    }

    @Test
    void testLoadBooksEmptyCsv() {
        Reader emptyReader = new StringReader(EMPTY_CSV_DATA);
        Set<Book> books = BookLoader.load(emptyReader);

        assertNotNull(books, "The set of books should not be null");
        assertTrue(books.isEmpty(), "The set of books should be empty");
    }

    @Test
    void testLoadBooksWithIncompleteData() {
        Reader incompleteReader = new StringReader(INCOMPLETE_CSV_DATA);

        assertThrows(IllegalArgumentException.class, () -> BookLoader.load(incompleteReader),
                "Expected load() to throw, but it didn't");
    }

    private void assertBookPropertiesNotNull(Book book) {
        assertNotNull(book.title(), "Book title should not be null");
        assertNotNull(book.author(), "Book author should not be null");
        assertNotNull(book.description(), "Book description should not be null");
        assertNotNull(book.genres(), "Book genres should not be null");
        assertNotNull(book.URL(), "Book URL should not be null");
    }
}