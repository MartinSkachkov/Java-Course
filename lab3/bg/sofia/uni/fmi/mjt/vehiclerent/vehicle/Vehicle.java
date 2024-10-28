package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

public sealed abstract class Vehicle permits NonMotorVehicle, MotorVehicle{
    private final String id;
    private final String model;
    private Driver currentDriver;
    private LocalDateTime rentStartTime;

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
    }

    /**
     * Simulates rental of the vehicle. The vehicle now is considered rented by the provided driver and the start of the rental is the provided date.
     * @param driver the driver that wants to rent the vehicle.
     * @param startRentTime the start time of the rent
     * @throws VehicleAlreadyRentedException in case the vehicle is already rented by someone else or by the same driver.
     */
    public void rent(Driver driver, LocalDateTime startRentTime) {
        if (driver == null || startRentTime == null) {
            throw new IllegalArgumentException("Driver and start time cannot be null");
        }

        if (currentDriver != null) {
            throw new VehicleAlreadyRentedException("Vehicle is already rented");
        }

        currentDriver = driver;
        rentStartTime = startRentTime;
    }

    /**
     * Simulates end of rental for the vehicle - it is no longer rented by a driver.
     * @param rentalEnd time of end of rental
     * @throws IllegalArgumentException in case @rentalEnd is null
     * @throws VehicleNotRentedException in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     * in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     * and the driver tries to return them after an hour.
     */
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        if (rentalEnd == null) {
            throw new IllegalArgumentException("Rental end time cannot be null");
        }

        if (currentDriver == null || rentStartTime == null) {
            throw new VehicleNotRentedException("Vehicle is not currently rented");
        }

        if (rentalEnd.isBefore(rentStartTime)) {
            throw new InvalidRentingPeriodException("End time cannot be before start time");
        }

        validateRentingPeriod(rentStartTime, rentalEnd);

        currentDriver = null;
        rentStartTime = null;
    }

    protected abstract void validateRentingPeriod(LocalDateTime start, LocalDateTime end) throws InvalidRentingPeriodException;

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or
     * the period is not valid (end date is before start date)
     */
    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;

    protected long calculateHours(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toHoursPart() + (duration.toMinutesPart() > 0 ? 1 : 0);
    }

    protected long calculateDays(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toDays();
    }

    protected long calculateWeeks(LocalDateTime start, LocalDateTime end) {
        return calculateDays(start, end) / 7;
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public Driver getCurrentDriver() {
        return currentDriver;
    }

    public LocalDateTime getRentStartTime() {
        return rentStartTime;
    }
}
