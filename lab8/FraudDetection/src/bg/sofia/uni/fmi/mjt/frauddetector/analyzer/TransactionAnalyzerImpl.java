package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {
    private final double epsilone = 1e-10;
    private final List<Transaction> transactions;
    private final List<Rule> rules;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        validateInput(reader, rules);

        this.transactions = loadTransactions(reader);
        this.rules = rules;
    }

    private void validateInput(Reader reader, List<Rule> rules) {
        if (reader == null || rules == null) {
            throw new IllegalArgumentException("Illegal arguments are passed to the" +
                    "TransactionAnalyzerImpl constructor");
        }

        double totalWeight = rules.stream()
                .mapToDouble(Rule::weight)
                .sum();

        if (Math.abs(totalWeight) - 1.0 > epsilone) {
            throw new IllegalArgumentException("Rule weights must sum to 1.0");
        }
    }

    private List<Transaction> loadTransactions(Reader reader) {
        try (BufferedReader bf = new BufferedReader(reader)) {
            bf.readLine();

            return bf.lines()
                    .map(Transaction::of)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Error reading transactions", e);
        }
    }

    @Override
    public List<Transaction> allTransactions() {
        return new ArrayList<>(transactions);
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
                .map(Transaction::accountID)
                .distinct()
                .toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::channel,
                        Collectors.summingInt(t -> 1)
                ));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null || accountID.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        return transactions.stream()
                .filter(t -> t.accountID().equals(accountID))
                .mapToDouble(Transaction::transactionAmount)
                .sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        return transactions.stream()
                .filter(t -> t.accountID().equals(accountId))
                .toList();
    }

    @Override
    public double accountRating(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        List<Transaction> userTransactions = allTransactionsByUser(accountId);

        return userTransactions.isEmpty() ? 0.0 :
                rules.stream()
                        .filter(rule -> rule.applicable(userTransactions))
                        .mapToDouble(Rule::weight)
                        .sum();
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        SortedMap<String, Double> riskScores = new TreeMap<>(
                (a, b) -> Double.compare(
                        accountRating(b),
                        accountRating(a)
                )
        );

        allAccountIDs().forEach(accId -> riskScores.put(accId, accountRating(accId)));

        return riskScores;
    }
}
