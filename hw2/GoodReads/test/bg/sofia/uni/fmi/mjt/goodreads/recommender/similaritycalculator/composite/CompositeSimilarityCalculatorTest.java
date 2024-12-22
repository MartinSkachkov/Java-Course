package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompositeSimilarityCalculatorTest {

    @Mock
    private SimilarityCalculator calculator1;

    @Mock
    private SimilarityCalculator calculator2;

    @Mock
    private Book book1;

    @Mock
    private Book book2;

    @Test
    void testConstructorWithNullMapThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeSimilarityCalculator(null),
                "Should throw IllegalArgumentException when map is null");
    }

    @Test
    void testConstructorWithEmptyMapThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeSimilarityCalculator(new HashMap<>()),
                "Should throw IllegalArgumentException when map is empty");
    }

    @Test
    void testCalculateSimilarityWithNullFirstBookThrows() {
        Map<SimilarityCalculator, Double> calculators = Map.of(calculator1, 1.0);
        CompositeSimilarityCalculator calculator = new CompositeSimilarityCalculator(calculators);

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(null, book2),
                "Should throw IllegalArgumentException when first book is null");
    }

    @Test
    void testCalculateSimilarityWithNullSecondBookThrows() {
        Map<SimilarityCalculator, Double> calculators = Map.of(calculator1, 1.0);
        CompositeSimilarityCalculator calculator = new CompositeSimilarityCalculator(calculators);

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(book1, null),
                "Should throw IllegalArgumentException when second book is null");
    }

    @Test
    void testCalculateSimilarityWithSingleCalculator() {
        Map<SimilarityCalculator, Double> calculators = Map.of(calculator1, 1.0);
        CompositeSimilarityCalculator calculator = new CompositeSimilarityCalculator(calculators);

        when(calculator1.calculateSimilarity(book1, book2)).thenReturn(0.5);

        double actualSimilarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.5, actualSimilarity,
                "Should return correct weighted similarity for single calculator");
    }

    @Test
    void testCalculateSimilarityWithMultipleCalculators() {
        Map<SimilarityCalculator, Double> calculators = new HashMap<>();
        calculators.put(calculator1, 0.7);
        calculators.put(calculator2, 0.3);

        CompositeSimilarityCalculator calculator = new CompositeSimilarityCalculator(calculators);

        when(calculator1.calculateSimilarity(book1, book2)).thenReturn(1.0);
        when(calculator2.calculateSimilarity(book1, book2)).thenReturn(0.0);

        double actualSimilarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.7, actualSimilarity, 0.0001,
                "Should return correct weighted average of similarities");
    }

    @Test
    void testCalculateSimilarityWithZeroWeights() {
        Map<SimilarityCalculator, Double> calculators = Map.of(calculator1, 0.0);
        CompositeSimilarityCalculator calculator = new CompositeSimilarityCalculator(calculators);

        when(calculator1.calculateSimilarity(book1, book2)).thenReturn(1.0);

        double actualSimilarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.0, actualSimilarity,
                "Should return zero when all weights are zero");
    }
    
}
