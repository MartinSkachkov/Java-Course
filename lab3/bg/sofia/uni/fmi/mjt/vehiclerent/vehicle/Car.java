package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;

public final class Car extends MotorVehicle {
    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model, fuelType, numberOfSeats, pricePerWeek, pricePerDay, pricePerHour);
    }

    @Override
    protected void validateRentingPeriod(LocalDateTime start, LocalDateTime end) {
        // Cars can be rented for any period
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (startOfRent == null || endOfRent == null) {
            throw new IllegalArgumentException("Start and end times cannot be null");
        }

        if (endOfRent.isBefore(startOfRent)) {
            throw new InvalidRentingPeriodException("End time cannot be before start time");
        }

        long totalDays = calculateDays(startOfRent, endOfRent);
        long weeks = calculateWeeks(startOfRent, endOfRent);
        long remainingDays = totalDays % 7;
        long remainingHours = calculateHours(startOfRent, endOfRent);
    }
}
