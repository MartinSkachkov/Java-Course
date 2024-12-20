package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookFinder implements BookFinderAPI {
    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        validateInput(books, tokenizer);

        this.books = books;
        this.tokenizer = tokenizer;
    }

    public Set<Book> allBooks() {
        return books;
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        validateAuthorName(authorName);

        return books.stream()
                .filter(book -> book.author().equalsIgnoreCase(authorName))
                .toList();
    }

    @Override
    public Set<String> allGenres() {
        return books.stream()
                .flatMap(book -> book.genres().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        validateSearchByGenresParams(genres, option);

        return switch (option) {
            case MATCH_ALL -> books.stream()
                    .filter(book -> book.genres().containsAll(genres))
                    .toList();
            case MATCH_ANY -> books.stream()
                    .filter(book -> !Collections.disjoint(book.genres(), genres))
                    .toList();
        };
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        validateSearchByKeywordsParams(keywords, option);

        Set<String> lowerCaseTokenizedKeywords = keywords.stream()
                .map(tokenizer::tokenize)
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        return books.stream()
                .filter(book -> {
                    List<String> titleTokens = tokenizer.tokenize(book.title());
                    List<String> descriptionTokens = tokenizer.tokenize(book.description());

                    Set<String> allTokens = new HashSet<>();
                    allTokens.addAll(titleTokens);
                    allTokens.addAll(descriptionTokens);

                    return switch (option) {
                        case MATCH_ALL -> lowerCaseTokenizedKeywords.stream()
                                .allMatch(allTokens::contains);
                        case MATCH_ANY -> lowerCaseTokenizedKeywords.stream()
                                .anyMatch(allTokens::contains);
                    };
                })
                .toList();
    }

    private void validateInput(Set<Book> books, TextTokenizer tokenizer) {
        if (books == null || books.isEmpty()) {
            throw new IllegalArgumentException("Books set cannot be null or empty");
        }

        if (tokenizer == null) {
            throw new IllegalArgumentException("TextTokenizer cannot be null");
        }
    }

    private void validateAuthorName(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }
    }

    private void validateSearchByGenresParams(Set<String> genres, MatchOption option) {
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be null or empty");
        }

        if (option == null) {
            throw new IllegalArgumentException("MatchOption cannot be null");
        }
    }

    private void validateSearchByKeywordsParams(Set<String> keywords, MatchOption option) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords cannot be null or empty");
        }

        if (option == null) {
            throw new IllegalArgumentException("MatchOption cannot be null");
        }
    }
}
