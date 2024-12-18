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
        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        validateInput(input);

        Set<String> formattedStopwords = formatStopwords();

        return Arrays.stream(input.toLowerCase()
                        .replaceAll("\\p{Punct}", "")
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

    private void validateInput(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null or blank");
        }
    }

    private Set<String> formatStopwords() {
        if (stopwords == null) {
            return Set.of();
        }

        return stopwords.stream()
                .map(String::toLowerCase)
                .map(word -> word.replaceAll("\\p{Punct}", ""))
                .collect(Collectors.toSet());
    }

}