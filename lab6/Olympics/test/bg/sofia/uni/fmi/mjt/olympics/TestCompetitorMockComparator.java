package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;

import java.util.Comparator;

class TestCompetitorMockComparator implements Comparator<Competitor> {
    @Override
    public int compare(Competitor c1, Competitor c2) {
        return -1;
    }
}