package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class SmallTransactionsRule implements Rule {
    private final int transactionCountThreshold;
    private final double transactionAmountThreshold;
    private final double ruleWeight;

    public SmallTransactionsRule(int countThreshold, double amountThreshold, double weight) {
        validateInput(countThreshold, amountThreshold, weight);

        this.transactionCountThreshold = countThreshold;
        this.transactionAmountThreshold = amountThreshold;
        this.ruleWeight = weight;
    }

    private void validateInput(int countThreshold, double amountThreshold, double weight) {
        if (countThreshold < 0 || amountThreshold < 0 || weight > 1.0 || weight < 0.0) {
            throw new IllegalArgumentException("Illegal arguments are passed to the SmallTransactionsRule constructor");
        }
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException("The transactions list cannot be null");
        }

        long countTransactionsUnderAmount = transactions.stream()
                .filter(t -> t.transactionAmount() < transactionAmountThreshold)
                .count();

        return countTransactionsUnderAmount > transactionCountThreshold;
    }

    @Override
    public double weight() {
        return ruleWeight;
    }
}