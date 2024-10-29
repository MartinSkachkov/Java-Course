package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public sealed abstract class NonMotorVehicle extends Vehicle permits Bicycle {
    private double pricePerDay;
    private double pricePerHour;

    protected NonMotorVehicle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }
}
