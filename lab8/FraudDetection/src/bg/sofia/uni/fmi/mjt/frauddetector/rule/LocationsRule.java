package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class LocationsRule implements Rule {
    private final int locationCountThreshold;
    private final double ruleWeight;

    public LocationsRule(int threshold, double weight) {
        validateInput(threshold, weight);

        this.locationCountThreshold = threshold;
        this.ruleWeight = weight;
    }

    private void validateInput(int threshold, double weight) {
        if (threshold < 0 || weight > 1.0 || weight < 0.0) {
            throw new IllegalArgumentException("Illegal arguments are passed to the LocationRule constructor");
        }
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException("The transactions list cannot be null");
        }

        long distinctLocationsCount = transactions.stream()
                .map(Transaction::location)
                .distinct()
                .count();

        return distinctLocationsCount > locationCountThreshold;
    }

    @Override
    public double weight() {
        return ruleWeight;
    }
}