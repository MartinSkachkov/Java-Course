package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public sealed abstract class MotorVehicle extends Vehicle permits Car, Caravan {
    private static final double PRICE_PER_SEAT = 5.0;
    private final FuelType fuelType;
    private final int numberOfSeats;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;

    protected MotorVehicle(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    protected double calculateSeatPrice() {
        return numberOfSeats * PRICE_PER_SEAT;
    }

    protected double calculateFuelTypePrice(long days) {
        return fuelType.getDailyFuelTax() * days;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public double getPricePerWeek() {
        return pricePerWeek;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }
}
