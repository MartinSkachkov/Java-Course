package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;

public final class Car extends MotorVehicle {
    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model, fuelType, numberOfSeats, pricePerWeek, pricePerDay, pricePerHour);
    }

    @Override
    protected void validateRentingPeriod(LocalDateTime start, LocalDateTime end) throws InvalidRentingPeriodException {
        // Cars can be rented for any period
        if (end.isBefore(start)) {
            throw new InvalidRentingPeriodException("End time cannot be before start time");
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

        return rentalPrice + calculateSeatPrice() + calculateFuelTypePrice(totalDays);
    }
}
