package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule implements Rule {
    private final double zScoreThreshold;
    private final double ruleWeight;

    public ZScoreRule(double zScoreThreshold, double weight) {
        validateInput(weight);

        this.zScoreThreshold = zScoreThreshold;
        this.ruleWeight = weight;
    }

    private void validateInput(double weight) {
        if (weight > 1.0 || weight < 0.0) {
            throw new IllegalArgumentException("Illegal arguments are passed to the ZScoreRule constructor");
        }
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        double mean = transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .average()
                .orElse(0.0);

        double variance = transactions.stream()
                .mapToDouble(t -> Math.pow(t.transactionAmount() - mean, 2))
                .average()
                .orElse(0.0);

        double stdDev = Math.sqrt(variance);

        return transactions.stream()
                .anyMatch(t -> Math.abs((t.transactionAmount() - mean) / stdDev) > zScoreThreshold);
    }

    @Override
    public double weight() {
        return ruleWeight;
    }
}