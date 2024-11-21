package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record Delivery(
        Location client,
        Location restaurant,
        Location deliveryGuy,
        String foodItem,
        double price,
        int estimatedTime
) {
    public Delivery {
        if (client == null || restaurant == null || deliveryGuy == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }

        if (foodItem == null || foodItem.isBlank()) {
            throw new IllegalArgumentException("Food item cannot be null or blank");
        }

        if (price < 0 || estimatedTime < 0) {
            throw new IllegalArgumentException("Price and time must be non-negative");
        }
    }
}