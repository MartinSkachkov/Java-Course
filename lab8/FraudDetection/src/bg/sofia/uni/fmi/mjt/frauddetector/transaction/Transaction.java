package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(
        String transactionID,
        String accountID,
        double transactionAmount,
        LocalDateTime transactionDate,
        String location,
        Channel channel
) {
    private static final int TRANSACTION_ID_INDEX = 0;
    private static final int ACCOUNT_ID_INDEX = 1;
    private static final int TRANSACTION_AMOUNT_INDEX = 2;
    private static final int TRANSACTION_DATE_INDEX = 3;
    private static final int LOCATION_INDEX = 4;
    private static final int CHANNEL_INDEX = 5;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Transaction of(String line) {
        String[] parts = line.split(",");

        return new Transaction(
                parts[TRANSACTION_ID_INDEX],
                parts[ACCOUNT_ID_INDEX],
                Double.parseDouble(parts[TRANSACTION_AMOUNT_INDEX]),
                LocalDateTime.parse(parts[TRANSACTION_DATE_INDEX], FORMATTER),
                parts[LOCATION_INDEX],
                Channel.valueOf(parts[CHANNEL_INDEX].toUpperCase())
        );
    }
}