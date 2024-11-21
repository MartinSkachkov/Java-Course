package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

import java.util.ArrayList;
import java.util.List;

public class ControlCenter implements ControlCenterApi {
    private final MapEntity[][] mapLayout;

    public ControlCenter(char[][] mapLayout) {
        validateMapLayoutRowLength(mapLayout);

        int rows = mapLayout.length;
        int cols = mapLayout[0].length;

        this.mapLayout = new MapEntity[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                MapEntityType type = MapEntityType.fromChar(mapLayout[row][col]);
                this.mapLayout[row][col] = new MapEntity(new Location(row, col), type);
            }
        }
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation,
                                               double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        validateFindOptimalDeliveryGuyFuncParams(restaurantLocation, clientLocation, maxPrice, maxTime, shippingMethod);

        // Намери всички доставчици
        List<MapEntity> deliveryGuys = findAllDeliveryGuys();

        // Намери доставчика с най-кратко разстояние

        throw new IllegalArgumentException("ds");

    }

    @Override
    public MapEntity[][] getLayout() {
        return mapLayout;
    }

    private void validateMapLayoutRowLength(char[][] mapLayout) {
        int rowLength = mapLayout[0].length;

        for (int row = 1; row < mapLayout.length; row++) {
            if (mapLayout[row].length != rowLength) {
                throw new IllegalArgumentException("Rows of the map should be equal length");
            }
        }
    }

    private void validateFindOptimalDeliveryGuyFuncParams(Location restaurantLocation, Location clientLocation,
                                                          double maxPrice, int maxTime, ShippingMethod shippingMethod) {
        if (restaurantLocation == null || clientLocation == null) {
            throw new IllegalArgumentException("Restaurant's location or client's location must not be null");
        }

        if (maxPrice < 0 || maxTime < 0) {
            throw new IllegalArgumentException("Max Price or max time must be non-negative");
        }

        if (shippingMethod == null) {
            throw new IllegalArgumentException("Shipping method must not be null");
        }
    }

    // public List<MapEntity> test() {
    //   return findAllDeliveryGuys();
    //}

    private List<MapEntity> findAllDeliveryGuys() {
        List<MapEntity> deliveryGuys = new ArrayList<>();

        for (MapEntity[] row : mapLayout) {
            for (MapEntity entity : row) {
                if (entity.type() == MapEntityType.DELIVERY_GUY_BIKE ||
                        entity.type() == MapEntityType.DELIVERY_GUY_CAR) {
                    deliveryGuys.add(entity);
                }
            }
        }

        if (deliveryGuys.isEmpty()) {
            throw new IllegalArgumentException("No delivery guys found on the map");
        }

        return deliveryGuys;
    }
}
