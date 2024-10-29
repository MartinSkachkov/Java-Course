package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Caravan extends MotorVehicle {
    private static final double PRICE_PER_BED = 10.0;
    private static final int MIN_RENTAL_HOURS = 24;
    private final int numberOfBeds;

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model, fuelType, numberOfSeats, pricePerWeek, pricePerDay, pricePerHour);
        this.numberOfBeds = numberOfBeds;
    }

    @Override
    protected void validateRentingPeriod(LocalDateTime start, LocalDateTime end) throws InvalidRentingPeriodException {
        if (end.isBefore(start)) {
            throw new InvalidRentingPeriodException("End time cannot be before start time");
        }

        Duration duration = Duration.between(start, end);

        if (duration.toHours() < MIN_RENTAL_HOURS) {
            throw new InvalidRentingPeriodException("Caravans must be rented for at least 24 hours");
        }
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (startOfRent == null || endOfRent == null) {
            throw new IllegalArgumentException("Start and end times cannot be null");
        }

        validateRentingPeriod(startOfRent, endOfRent);

        long totalDays = calculateDays(startOfRent, endOfRent);
        long weeks = calculateWeeks(startOfRent, endOfRent);
        long remainingDays = totalDays % 7;
        long remainingHours = calculateHours(startOfRent, endOfRent);

        double rentalPrice = (weeks * getPricePerWeek()) + (remainingDays * getPricePerDay()) + (remainingHours * getPricePerHour());

        return rentalPrice + calculateSeatPrice() + (numberOfBeds * PRICE_PER_BED) + calculateFuelTypePrice(totalDays);
    }
}
