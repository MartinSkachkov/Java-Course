package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Bicycle extends NonMotorVehicle{
    private static final int MAX_RENTAL_DAYS = 7;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id, model, pricePerDay, pricePerHour);
    }

    @Override
    protected void validateRentingPeriod(LocalDateTime start, LocalDateTime end) throws InvalidRentingPeriodException {
        if (end.isBefore(start)) {
            throw new InvalidRentingPeriodException("End time cannot be before start time");
        }

        Duration duration = Duration.between(start, end);

        if (duration.toDays() >= MAX_RENTAL_DAYS) {
            throw new InvalidRentingPeriodException("Bicycles cannot be rented for more than a week");
        }
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (startOfRent == null || endOfRent == null) {
            throw new IllegalArgumentException("Start and end times cannot be null");
        }

        validateRentingPeriod(startOfRent, endOfRent);

        long days = calculateDays(startOfRent, endOfRent);
        long hours = calculateHours(startOfRent, endOfRent);

        return (days * getPricePerDay()) + (hours * getPricePerHour());
    }
}
