package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public record Location(int x, int y) {
    public Location {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be non-negative");
        }
    }
}