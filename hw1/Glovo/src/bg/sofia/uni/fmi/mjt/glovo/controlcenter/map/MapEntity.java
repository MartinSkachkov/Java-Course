package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public record MapEntity(Location location, MapEntityType type) {
    public MapEntity {
        if (location == null || type == null) {
            throw new IllegalArgumentException("Location and map entity type must not be null");
        }
    }
}
