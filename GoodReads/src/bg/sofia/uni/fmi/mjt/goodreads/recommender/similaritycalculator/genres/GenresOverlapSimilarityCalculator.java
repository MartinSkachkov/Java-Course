package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        validateInput(first, second);

        Set<String> firstBookGenresSet = new HashSet<>(first.genres());
        Set<String> secondBookGenresSet = new HashSet<>(second.genres());

        Set<String> intersection = new HashSet<>(firstBookGenresSet);
        intersection.retainAll(secondBookGenresSet);

        int intersectionSize = intersection.size();
        int minSize = Math.min(firstBookGenresSet.size(), secondBookGenresSet.size());

        return (double) intersectionSize / minSize;
    }

    private void validateInput(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Books cannot be null");
        }
    }

}