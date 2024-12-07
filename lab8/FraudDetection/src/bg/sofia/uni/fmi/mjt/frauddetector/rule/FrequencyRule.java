package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Comparator;
import java.util.List;

public class FrequencyRule implements Rule {
    private final int transactionCountThreshold;
    private final TemporalAmount timeWindow;
    private final double ruleWeight;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        validateInput(transactionCountThreshold, timeWindow, weight);

        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.ruleWeight = weight;
    }

    private void validateInput(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        if (transactionCountThreshold < 0 || timeWindow == null || weight > 1.0 || weight < 0.0) {
            throw new IllegalArgumentException("Illegal arguments are passed to the FrequencyRule constructor");
        }
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException("The transactions list cannot be null");
        }

        List<Transaction> sortedTransactions = transactions.stream()
                .sorted(Comparator.comparing(Transaction::transactionDate))
                .toList();

        for (int i = 0; i < sortedTransactions.size(); i++) {
            LocalDateTime windowStart = sortedTransactions.get(i).transactionDate();
            LocalDateTime windowEnd = windowStart.plus(timeWindow);

            long transactionsInWindow = sortedTransactions.stream()
                    .filter(t -> !t.transactionDate().isBefore(windowStart) &&
                            !t.transactionDate().isAfter(windowEnd))
                    .count();

            if (transactionsInWindow > transactionCountThreshold) {
                return true;
            }
        }

        return false;
    }

    @Override
    public double weight() {
        return ruleWeight;
    }
}