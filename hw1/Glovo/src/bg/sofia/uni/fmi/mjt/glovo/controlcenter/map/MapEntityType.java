package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public enum MapEntityType {
    ROAD('.'),
    WALL('#'),
    RESTAURANT('R'),
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B');

    private final char entityTypeSymbol;

    MapEntityType(char entityTypeSymbol) {
        this.entityTypeSymbol = entityTypeSymbol;
    }

    public static MapEntityType fromChar(char symbol) {
        for (MapEntityType entityType : values()) {
            if (entityType.entityTypeSymbol == symbol) {
                return entityType;
            }
        }

        throw new IllegalArgumentException("Unknown map entity symbol: " + symbol);
    }

    public char getEntityTypeSymbol() {
        return entityTypeSymbol;
    }
}