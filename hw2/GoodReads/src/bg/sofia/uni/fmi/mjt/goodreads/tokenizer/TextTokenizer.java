package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextTokenizer {

    private final Set<String> stopwords;

    public TextTokenizer(Reader stopwordsReader) {
        validateInput(stopwordsReader);

        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        validateStringInput(input);

        Set<String> formattedStopwords = formatStopwords();

        return Arrays.stream(input.toLowerCase()
                        .replace("'", "")
                        .replaceAll("\\p{Punct}", " ")
                        .replaceAll("\\s+", " ")
                        .split(" "))
                .map(String::trim)
                .filter(word -> !word.isBlank())
                .filter(word -> !formattedStopwords.contains(word))
                .toList();
    }

    public Set<String> stopwords() {
        return stopwords;
    }

    private void validateInput(Reader stopwordsReader) {
        if (stopwordsReader == null) {
            throw new IllegalArgumentException("stopwordsReader cannot be null");
        }
    }

    private void validateStringInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
    }

    public Set<String> formatStopwords() {
        if (stopwords == null) {
            return Set.of();
        }

        return stopwords.stream()
                .map(String::toLowerCase)
                .map(word -> word.replace("'", ""))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

}