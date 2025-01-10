package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Map;

public class CompositeSimilarityCalculator implements SimilarityCalculator {
    private final Map<SimilarityCalculator, Double> similarityCalculatorMap;

    public CompositeSimilarityCalculator(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        validateInput(similarityCalculatorMap);

        this.similarityCalculatorMap = similarityCalculatorMap;
    }
    
    @Override
    public double calculateSimilarity(Book first, Book second) {
        booksNotNullValidation(first, second);

        double totalSimilarity = 0.0;

        for (var entry : similarityCalculatorMap.entrySet()) {
            SimilarityCalculator calculator = entry.getKey();
            double weight = entry.getValue();

            double similarity = calculator.calculateSimilarity(first, second);
            totalSimilarity += weight * similarity;
        }

        return totalSimilarity;
    }

    private void validateInput(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        if (similarityCalculatorMap == null || similarityCalculatorMap.isEmpty()) {
            throw new IllegalArgumentException("The similarityCalculatorMap cannot be null or empty.");
        }
    }

    private void booksNotNullValidation(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Books cannot be null.");
        }
    }
}
