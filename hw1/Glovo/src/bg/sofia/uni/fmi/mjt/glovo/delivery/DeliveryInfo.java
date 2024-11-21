package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record DeliveryInfo(
        Location deliveryGuyLocation,
        double price,
        int estimatedTime,
        DeliveryType deliveryType
) {
    public DeliveryInfo {
        if (deliveryGuyLocation == null || deliveryType == null) {
            throw new IllegalArgumentException("Location and delivery type must not be null");
        }

        if (price < 0 || estimatedTime < 0) {
            throw new IllegalArgumentException("Price and time must be non-negative");
        }
    }
}