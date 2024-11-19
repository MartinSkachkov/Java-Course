package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NationMedalComparatorTest {

    @Mock
    private MJTOlympics olympics;

    private NationMedalComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new NationMedalComparator(olympics);
    }

    @Test
    void testCompareNationsWithDifferentMedalCountBiggerFirstLowerSecond() {
        when(olympics.getTotalMedals("USA")).thenReturn(3);
        when(olympics.getTotalMedals("BG")).thenReturn(6);

        int result = comparator.compare("USA", "BG");
        assertTrue(result > 0, "BG (6 medals) should come before USA (3 medals)");
    }

    @Test
    void testCompareNationsWithDifferentMedalCountLowerFirstBiggerSecond() {
        when(olympics.getTotalMedals("USA")).thenReturn(3);
        when(olympics.getTotalMedals("BG")).thenReturn(6);

        int result = comparator.compare("BG", "USA");
        assertTrue(result < 0, "BG (6 medals) should come before USA (3 medals)");
    }

    @Test
    void testCompareNationsWithEqualMedalCount() {
        when(olympics.getTotalMedals("USA")).thenReturn(3);
        when(olympics.getTotalMedals("BG")).thenReturn(3);

        int result = comparator.compare("USA", "BG");
        assertTrue(result > 0, "Alphabetical order should be used when medal counts are equal");
    }

    @Test
    void testCompareWithSameNation() {
        when(olympics.getTotalMedals("BG")).thenReturn(5);

        int result = comparator.compare("BG", "BG");
        assertEquals(0, result, "Comparing the same nation should return 0");
    }
    
}
