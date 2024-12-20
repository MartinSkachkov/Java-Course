package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.Arrays;
import java.util.List;

public record Book(
        String ID,
        String title,
        String author,
        String description,
        List<String> genres,
        double rating,
        int ratingCount,
        String URL
) {
    private static final int REQUIRED_TOKENS = 8;

    private static final int INDEX_ID = 0;
    private static final int INDEX_TITLE = 1;
    private static final int INDEX_AUTHOR = 2;
    private static final int INDEX_DESCRIPTION = 3;
    private static final int INDEX_GENRES = 4;
    private static final int INDEX_RATING = 5;
    private static final int INDEX_RATING_COUNT = 6;
    private static final int INDEX_URL = 7;

    public Book {
        if (ID == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }

        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null.");
        }

        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null.");
        }

        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null.");
        }

        if (genres == null) {
            throw new IllegalArgumentException("Genres cannot be null.");
        }

        if (URL == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }

        if (rating < 0) {
            throw new IllegalArgumentException("Rating cannot be negative.");
        }

        if (ratingCount < 0) {
            throw new IllegalArgumentException("Rating count cannot be negative.");
        }
    }

    public static Book of(String[] tokens) {
        validateInput(tokens);

        String id = tokens[INDEX_ID];
        String title = tokens[INDEX_TITLE];
        String author = tokens[INDEX_AUTHOR];
        String description = tokens[INDEX_DESCRIPTION];
        List<String> genres = parseGenres(tokens[INDEX_GENRES]);
        double rating = parseRating(tokens[INDEX_RATING]);
        int ratingCount = parseRatingCount(tokens[INDEX_RATING_COUNT]);
        String url = tokens[INDEX_URL];

        return new Book(id, title, author, description, genres, rating, ratingCount, url);
    }

    private static void validateInput(String[] tokens) {
        if (tokens == null || tokens.length != REQUIRED_TOKENS) {
            throw new IllegalArgumentException("Invalid input tokens");
        }
    }

    private static List<String> parseGenres(String genreString) {
        return Arrays.stream(genreString
                        .replace("[", "")
                        .replace("]", "")
                        .split(","))
                .map(String::trim)
                .toList();
    }

    private static double parseRating(String ratingString) {
        try {
            return Double.parseDouble(ratingString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating value: " + ratingString, e);
        }
    }

    private static int parseRatingCount(String ratingCountString) {
        try {
            return Integer.parseInt(ratingCountString.replace(",", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating count value: " + ratingCountString, e);
        }
    }
}