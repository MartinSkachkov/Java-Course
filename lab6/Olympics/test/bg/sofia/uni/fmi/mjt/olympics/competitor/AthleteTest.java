package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthleteTest {

    private Athlete athlete;

    @BeforeEach
    void setUp() {
        athlete = new Athlete("ID123", "Mike Tyson", "USA");
    }

    @Test
    void testAddNullMedal() {
        assertThrows(IllegalArgumentException.class,
                () -> athlete.addMedal(null), "Expected IllegalArgumentException when null medal was passed");
    }

    @Test
    void testAddLegitMedal() {
        athlete.addMedal(Medal.GOLD);
        List<Medal> medals = athlete.getMedals();
        assertTrue(medals.contains(Medal.GOLD), "The medals set should contain the exact medal added.");
    }

    @Test
    void testMedalSetSizeWhitEmptySet() {
        List<Medal> medals = athlete.getMedals();
        assertEquals(0, medals.size(), "The medals set should be empty when no medals are added.");
    }

    @Test
    void testMedalsSetSizeAfterAddingMedals() {
        Medal goldMedal = Medal.GOLD;
        Medal silverMedal = Medal.SILVER;

        athlete.addMedal(goldMedal);
        athlete.addMedal(silverMedal);

        List<Medal> medals = athlete.getMedals();
        assertEquals(2, medals.size(), "The medals set size should be 2 after adding two medals.");
    }

    @Test
    void testEqualsWithSameObject() {
        assertEquals(athlete, athlete, "Same athlete objects should be equal");
    }

    @Test
    void testEqualsWhenAthletesAreEqual() {
        Athlete athlete2 = new Athlete("ID123", "Mike Tyson", "USA");
        assertEquals(athlete, athlete2, "Athletes with the same attributes should be equal");
    }

    @Test
    void testEqualsWhenAnotherAthleteIsNull() {
        Athlete nullAthlete = null;
        assertNotEquals(athlete, nullAthlete, "An athlete should not be equal to null.");
    }

    @Test
    void testEqualsWhenAthletesAreNotEqual() {
        Athlete athlete2 = new Athlete("ID124", "Jake Paul", "USA");
        assertNotEquals(athlete, athlete2, "Athletes with different names and ids should not be equal");
    }

    @Test
    void testHashCodeWithEqualAthletes() {
        Athlete athlete2 = new Athlete("ID123", "Mike Tyson", "USA");
        assertEquals(athlete.hashCode(), athlete2.hashCode(), "Hash codes should be equal for equal athletes");
    }

    @Test
    void testHashCodeWithNotEqualAthletes() {
        Athlete athlete2 = new Athlete("ID124", "Jake Paul", "USA");
        assertNotEquals(athlete.hashCode(), athlete2.hashCode(), "Hash codes should be different for unequal athletes");
    }

}