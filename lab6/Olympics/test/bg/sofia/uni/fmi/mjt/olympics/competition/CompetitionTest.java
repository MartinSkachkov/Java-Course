package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompetitionTest {

    private final String DISCIPLINE = "100m Sprint";
    private final String COMPETITION_NAME = "Olympic Games";
    private Set<Competitor> competitors;

    @BeforeEach
    void setUp() {
        competitors = new HashSet<>();
        competitors.add(new Athlete("ID1", "Vanko Byrziq", "BG"));
        competitors.add(new Athlete("ID2", "Marto Begacha", "GBR"));
    }

    @Test
    void testConstructorThrowsExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition(null, DISCIPLINE, competitors),
                "Expected IllegalArgumentException when name is null, but no exception was thrown.");
    }

    @Test
    void testConstructorThrowsExceptionWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition("  ", DISCIPLINE, competitors),
                "Expected IllegalArgumentException when name is blank, but no exception was thrown.");
    }

    @Test
    void testConstructorThrowsExceptionWhenDisciplineIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition(COMPETITION_NAME, null, competitors),
                "Expected IllegalArgumentException when discipline is null, but no exception was thrown.");
    }

    @Test
    void testConstructorThrowsExceptionWhenDisciplineIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition(COMPETITION_NAME, "  ", competitors),
                "Expected IllegalArgumentException when discipline is blank, but no exception was thrown.");
    }

    @Test
    void testConstructorThrowsExceptionWhenCompetitorsIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition(COMPETITION_NAME, DISCIPLINE, null),
                "Expected IllegalArgumentException when competitors is null, but no exception was thrown.");
    }

    @Test
    void testConstructorThrowsExceptionWhenCompetitorsIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition(COMPETITION_NAME, DISCIPLINE, new HashSet<>()),
                "Expected IllegalArgumentException when competitors is empty, but no exception was thrown.");
    }

    @Test
    void testEqualsSymmetric() {
        Competition c1 = new Competition(COMPETITION_NAME, DISCIPLINE, competitors);
        Competition c2 = new Competition(COMPETITION_NAME, DISCIPLINE, new HashSet<>(competitors));

        assertTrue(c1.equals(c2) && c2.equals(c1), "Competitions with same name and discipline should be equal regardless of competitors set instance");
        assertEquals(c1.hashCode(), c2.hashCode(), "Competitions with same name and discipline should have same hash code");
    }

    @Test
    void testEqualsDifferentCompetitorsButSameNameAndDiscipline() {
        Set<Competitor> otherCompetitors = new HashSet<>();
        otherCompetitors.add(new Athlete("ID3", "Pesho Begacha", "BG"));

        Competition c1 = new Competition(COMPETITION_NAME, DISCIPLINE, competitors);
        Competition c2 = new Competition(COMPETITION_NAME, DISCIPLINE, otherCompetitors);

        assertTrue(c1.equals(c2) && c2.equals(c1), "Competitions should be equal even with different competitors if name and discipline match");
        assertEquals(c1.hashCode(), c2.hashCode(), "Competitions should have same hash code even with different competitors if name and discipline match");
    }

    @Test
    void testEqualsDifferentCompetitionName() {
        Competition c1 = new Competition(COMPETITION_NAME, DISCIPLINE, competitors);
        Competition c2 = new Competition("World Championship", DISCIPLINE, competitors);

        assertFalse(c1.equals(c2) || c2.equals(c1), "Competitions with different names should not be equal");
        assertNotEquals(c1.hashCode(), c2.hashCode(), "Competitions with different names should have different hash codes");
    }

    @Test
    void testEqualsDifferentDiscipline() {
        Competition c1 = new Competition(COMPETITION_NAME, DISCIPLINE, competitors);
        Competition c2 = new Competition(COMPETITION_NAME, "200m Sprint", competitors);

        assertFalse(c1.equals(c2) || c2.equals(c1), "Competitions with different disciplines should not be equal");
        assertNotEquals(c1.hashCode(), c2.hashCode(), "Competitions with different disciplines should have different hash codes");
    }

    @Test
    void testEqualsNull() {
        Competition c1 = new Competition(COMPETITION_NAME, DISCIPLINE, competitors);

        assertNotEquals(null, c1, "Competition should not be equal to null");
    }

    @Test
    void testEqualsSameObject() {
        Competition c1 = new Competition(COMPETITION_NAME, DISCIPLINE, competitors);

        assertEquals(c1, c1, "Competition should be equal to itself");
        assertEquals(c1.hashCode(), c1.hashCode(), "Same competition instance should have consistent hash code");
    }

}
