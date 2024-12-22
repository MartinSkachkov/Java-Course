package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextTokenizerTest {

    private TextTokenizer tokenizer;
    private String stopwords = "a" + System.lineSeparator() +
            "an" + System.lineSeparator() +
            "the" + System.lineSeparator() +
            "is" + System.lineSeparator() +
            "are" + System.lineSeparator() +
            "was" + System.lineSeparator() +
            "were" + System.lineSeparator() +
            "aren't" + System.lineSeparator() +
            "they're";

    @BeforeEach
    void setUp() {
        Reader stopwordsReader = new StringReader(stopwords);
        tokenizer = new TextTokenizer(stopwordsReader);
    }

    @Test
    void testConstructorWithNullReader() {
        assertThrows(IllegalArgumentException.class, () -> new TextTokenizer(null),
                "Constructor should throw IllegalArgumentException when reader is null");
    }

    @Test
    void testTokenizeWithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> tokenizer.tokenize(null),
                "Tokenize should throw IllegalArgumentException when input is null");
    }

    @Test
    void testTokenizeWithEmptyString() {
        List<String> tokens = tokenizer.tokenize("");
        assertTrue(tokens.isEmpty(), "Empty string should result in empty token list");
    }

    @Test
    void testTokenizeWithBlankString() {
        List<String> tokens = tokenizer.tokenize("   ");
        assertTrue(tokens.isEmpty(), "Blank string should result in empty token list");
    }

    @Test
    void testTokenizeRemovesStopwords() {
        String input = "The cat is sitting on a mat. Children aren't playing with them.";
        List<String> expected = List.of("cat", "sitting", "on", "mat", "children", "playing", "with", "them");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should remove stopwords and return remaining tokens");
    }

    @Test
    void testTokenizeHandlesPunctuation() {
        String input = "Hello, world! How are you? This is a test...";
        List<String> expected = List.of("hello", "world", "how", "you", "this", "test");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should remove punctuation and stopwords");
    }

    @Test
    void testTokenizeHandlesApostrophes() {
        String input = "It's John's book. They're going to Mary's house but aren't staying there.";
        List<String> expected = List.of("its", "johns", "book", "going", "to", "marys", "house", "but", "staying", "there");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should handle apostrophes correctly");
    }

    @Test
    void testTokenizeHandlesMultipleSpaces() {
        String input = "This   has    multiple     spaces!!!";
        List<String> expected = List.of("this", "has", "multiple", "spaces");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should handle multiple spaces correctly");
    }

    @Test
    void testTokenizeMaintainsCorrectOrder() {
        String input = "cat dog bird cat dog";
        List<String> expected = List.of("cat", "dog", "bird", "cat", "dog");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should maintain the order of words");
    }

    @Test
    void testTokenizeConvertsToLowerCase() {
        String input = "The CAT is SITTING on A mat";
        List<String> expected = List.of("cat", "sitting", "on", "mat");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should convert all words to lowercase");
    }

    @Test
    void testTokenizeWithSpecialCharacters() {
        String input = "Hello@world#$%^&* Special-Characters";
        List<String> expected = List.of("hello", "world", "special", "characters");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should handle special characters correctly");
    }

    @Test
    void testTokenizeWithNumbers() {
        String input = "The 123 numbers456 are 789here";
        List<String> expected = List.of("123", "numbers456", "789here");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should handle numbers appropriately");
    }

    @Test
    void testTokenizeWithMixedCase() {
        String input = "MiXeD CaSe WoRdS";
        List<String> expected = List.of("mixed", "case", "words");
        List<String> actual = tokenizer.tokenize(input);

        assertIterableEquals(expected, actual,
                "Tokenizer should convert mixed case words to lowercase");
    }

}