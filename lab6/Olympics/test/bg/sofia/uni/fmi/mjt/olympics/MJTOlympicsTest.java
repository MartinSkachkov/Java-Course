package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MJTOlympicsTest {

    @Mock
    private CompetitionResultFetcher resultFetcher;

    @Mock
    private Competitor competitor1;

    @Mock
    private Competitor competitor2;

    @Mock
    private Competitor competitor3;

    private Set<Competitor> registeredCompetitors;
    private MJTOlympics olympics;
    private Competition competition;

    @BeforeEach
    void setUp() {
        registeredCompetitors = new HashSet<>(Set.of(competitor1, competitor2, competitor3));
        olympics = new MJTOlympics(registeredCompetitors, resultFetcher);
    }

    @Test
    void testUpdateMedalStatisticsWithNullCompetitionReturnsException() {
        assertThrows(IllegalArgumentException.class,
                () -> olympics.updateMedalStatistics(null),
                "Should throw IllegalArgumentException when competition is null.");
    }

    @Test
    void testUpdateMedalStatisticsWithUnregisteredCompetitorsReturnsException() {
        Set<Competitor> unregisteredCompetitors = new HashSet<>(Set.of(mock(Competitor.class)));
        Competition competition = new Competition("TestCompetition", "TestDiscipline", unregisteredCompetitors);

        assertThrows(IllegalArgumentException.class,
                () -> olympics.updateMedalStatistics(competition),
                "Should throw IllegalArgumentException when competition contains unregistered competitors.");
    }

    @Test
    void testUpdateMedalStatisticsUpdatesCompetitorMedalsCorrectly() {
        when(competitor1.getNationality()).thenReturn("BG");
        when(competitor2.getNationality()).thenReturn("USA");
        when(competitor3.getNationality()).thenReturn("GBR");

        Set<Competitor> competitionCompetitors = new HashSet<>(Set.of(competitor1, competitor2, competitor3));
        Competition testCompetition = new Competition("Swimming", "100m Freestyle", competitionCompetitors);

        TreeSet<Competitor> results = new TreeSet<>(new TestCompetitorMockComparator());
        results.add(competitor1); // GOLD
        results.add(competitor2); // SILVER
        results.add(competitor3); // BRONZE

        when(resultFetcher.getResult(testCompetition)).thenReturn(results);

        olympics.updateMedalStatistics(testCompetition);

        Map<String, EnumMap<Medal, Integer>> medalTable = olympics.getNationsMedalTable();

        assertEquals(1, medalTable.get("BG").get(Medal.GOLD), "Bulgaria should have exactly 1 GOLD medal");
        assertEquals(1, medalTable.get("USA").get(Medal.SILVER), "USA should have exactly 1 SILVER medal");
        assertEquals(1, medalTable.get("GBR").get(Medal.BRONZE), "Great Britain should have exactly 1 BRONZE medal");
    }

    @Test
    void testGetTotalMedalsWhenNullNationalityIsPassed() {
        assertThrows(IllegalArgumentException.class,
                () -> olympics.getTotalMedals(null),
                "Should throw IllegalArgumentException when null nationality is passed.");
    }

    @Test
    void testGetTotalMedalsWhenUnregisteredNationalityIsPassed() {
        assertThrows(IllegalArgumentException.class,
                () -> olympics.getTotalMedals("UnknownNation"),
                "Should throw IllegalArgumentException when unregistered nationality is passed.");
    }

    @Test
    void testGetTotalMedalsWithValidNationality() {
        olympics.getNationsMedalTable().put("BG", new EnumMap<>(Medal.class));
        olympics.getNationsMedalTable().get("BG").put(Medal.GOLD, 2);
        olympics.getNationsMedalTable().get("BG").put(Medal.SILVER, 1);

        int totalMedals = olympics.getTotalMedals("BG");

        assertEquals(3, totalMedals, "Total medals for 'BUL' should be 3.");
    }

    @Test
    void testGetTotalMedalsWithZeroMedals() {
        olympics.getNationsMedalTable().put("USA", new EnumMap<>(Medal.class));

        int totalMedals = olympics.getTotalMedals("USA");

        assertEquals(0, totalMedals, "Total medals for 'USA' should be 0.");
    }

    @Test
    void testGetNationsRankList() {
        olympics.getNationsMedalTable().put("BG", new EnumMap<>(Medal.class));
        olympics.getNationsMedalTable().get("BG").put(Medal.GOLD, 3);
        olympics.getNationsMedalTable().get("BG").put(Medal.SILVER, 2);

        olympics.getNationsMedalTable().put("USA", new EnumMap<>(Medal.class));
        olympics.getNationsMedalTable().get("USA").put(Medal.GOLD, 2);
        olympics.getNationsMedalTable().get("USA").put(Medal.BRONZE, 3);

        olympics.getNationsMedalTable().put("GBR", new EnumMap<>(Medal.class));
        olympics.getNationsMedalTable().get("GBR").put(Medal.SILVER, 4);

        TreeSet<String> rankedNations = olympics.getNationsRankList();

        Iterator<String> iterator = rankedNations.iterator();
        assertEquals("BG", iterator.next(), "Bulgaria should be the top-ranked nation");
        assertEquals("USA", iterator.next(), "USA should be the second-ranked nation");
        assertEquals("GBR", iterator.next(), "Great Britain should be the third-ranked nation");
    }

}
