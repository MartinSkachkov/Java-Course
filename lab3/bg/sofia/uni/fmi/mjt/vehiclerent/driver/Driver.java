package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public record Driver (AgeGroup group){
    public double getAgeGroupTax() {
        return switch (group) {
            case JUNIOR -> 10;
            case EXPERIENCED -> 0;
            case SENIOR -> 15;
        };
    }
}