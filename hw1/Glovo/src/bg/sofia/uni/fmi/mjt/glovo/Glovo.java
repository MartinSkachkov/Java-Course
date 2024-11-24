package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Glovo implements GlovoApi {
    private final ControlCenter controlCenter;

    public Glovo(char[][] mapLayout) {
        this.controlCenter = new ControlCenter(mapLayout);
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException {

        validateInput(client, restaurant, foodItem);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
                restaurant.location(),
                client.location(),
                -1,
                -1,
                ShippingMethod.CHEAPEST
        );

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("No available delivery guys found");
        }

        return new Delivery(
                client.location(),
                restaurant.location(),
                deliveryInfo.deliveryGuyLocation(),
                foodItem,
                deliveryInfo.price(),
                deliveryInfo.estimatedTime()
        );
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem) throws NoAvailableDeliveryGuyException {
        return null;
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem, double maxPrice) throws NoAvailableDeliveryGuyException {
        return null;
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem, int maxTime) throws NoAvailableDeliveryGuyException {
        return null;
    }

    private void validateInput(MapEntity client, MapEntity restaurant, String foodItem) {
        MapEntity[][] map = controlCenter.getLayout();
        int mapRows = controlCenter.getRows();
        int mapCols = controlCenter.getCols();

        if (client == null || restaurant == null || foodItem == null || foodItem.isBlank()) {
            throw new InvalidOrderException("Invalid input parameters");
        }

        if (client.type() != MapEntityType.CLIENT || restaurant.type() != MapEntityType.RESTAURANT) {
            throw new InvalidOrderException("Invalid client or restaurant type");
        }

        if (checkIfLocationIsOutsideTheMap(restaurant.location(), mapRows, mapCols) ||
                checkIfLocationIsOutsideTheMap(client.location(), mapRows, mapCols)) {
            throw new InvalidOrderException("The specified location is outside the bounds of the map");
        }

        if (map[restaurant.location().x()][restaurant.location().y()].type() != MapEntityType.RESTAURANT ||
                map[client.location().x()][client.location().y()].type() != MapEntityType.CLIENT) {
            throw new InvalidOrderException("There is no client or restaurant at the specified location");
        }
    }

    private boolean checkIfLocationIsOutsideTheMap(Location location, int rows, int cols) {
        return location.x() < 0 || location.x() >= rows || location.y() < 0 || location.y() >= cols;
    }
}