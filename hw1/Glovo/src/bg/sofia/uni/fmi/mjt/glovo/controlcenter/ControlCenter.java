package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapLayoutSizeException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

import java.util.ArrayList;
import java.util.List;

public class ControlCenter implements ControlCenterApi {
    private final MapEntity[][] mapLayout;
    private final int rows;
    private final int cols;

    public ControlCenter(char[][] mapLayout) {
        validateMapLayoutRowLength(mapLayout);

        this.rows = mapLayout.length;
        this.cols = mapLayout[0].length;
        this.mapLayout = new MapEntity[this.rows][this.cols];

        convertCharMapLayoutToMapEntityLayout(mapLayout);
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation,
                                               double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        validateFindOptimalDeliveryGuyFuncParams(maxPrice, maxTime, shippingMethod);

        List<MapEntity> deliveryGuys = getAvailableDeliveryGuys();
        if (deliveryGuys == null) {
            return null;
        }

        return findBestDeliveryOption(deliveryGuys, restaurantLocation,
                clientLocation, maxPrice, maxTime, shippingMethod);

    }

    @Override
    public MapEntity[][] getLayout() {
        return mapLayout;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private void validateMapLayoutRowLength(char[][] mapLayout) {
        int rowLength = mapLayout[0].length;

        for (int row = 1; row < mapLayout.length; row++) {
            if (mapLayout[row].length != rowLength) {
                throw new InvalidMapLayoutSizeException("Rows of the map should be equal length");
            }
        }
    }

    private void convertCharMapLayoutToMapEntityLayout(char[][] mapLayout) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                MapEntityType type = MapEntityType.fromChar(mapLayout[row][col]);
                this.mapLayout[row][col] = new MapEntity(new Location(row, col), type);
            }
        }
    }

    private void validateFindOptimalDeliveryGuyFuncParams(double maxPrice, int maxTime, ShippingMethod shippingMethod) {
        if ((maxPrice <= 0 && maxPrice != -1) || (maxTime <= 0 && maxTime != -1)) {
            throw new IllegalArgumentException("Max Price or Max Time must be greater than 0 or -1 (no constraint)");
        }

        if (shippingMethod == null) {
            throw new IllegalArgumentException("Shipping method must not be null");
        }
    }

    private List<MapEntity> findAllDeliveryGuys() throws NoAvailableDeliveryGuyException {
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
            throw new NoAvailableDeliveryGuyException("No delivery guy entities found on the map");
        }

        return deliveryGuys;
    }

    private List<MapEntity> getAvailableDeliveryGuys() {
        try {
            return findAllDeliveryGuys();
        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private DeliveryInfo findBestDeliveryOption(List<MapEntity> deliveryGuys, Location restaurantLocation,
                                                Location clientLocation, double maxPrice, int maxTime,
                                                ShippingMethod shippingMethod) {

        DeliveryInfo bestDeliveryInfo = null;

        for (MapEntity deliveryGuy : deliveryGuys) {
            Location deliveryGuyLocation = deliveryGuy.location();
            DeliveryType deliveryType = mapDeliveryType(deliveryGuy.type());

            int pathLength = calculatePathLength(deliveryGuyLocation, restaurantLocation, clientLocation);
            if (pathLength <= 0) {
                continue;
            }

            int estimatedTime = calculateEstimatedTime(pathLength, deliveryType);
            double estimatedPrice = calculateEstimatedPrice(pathLength, deliveryType);

            if (isWithinPriceConstraint(estimatedPrice, maxPrice) && isWithinTimeConstraint(estimatedTime, maxTime)) {
                DeliveryInfo currentDeliveryInfo = new DeliveryInfo(
                        deliveryGuyLocation, estimatedPrice, estimatedTime, deliveryType
                );

                if (shouldReplace(bestDeliveryInfo, currentDeliveryInfo, shippingMethod)) {
                    bestDeliveryInfo = currentDeliveryInfo;
                }
            }
        }

        return bestDeliveryInfo;

    }

    private DeliveryType mapDeliveryType(MapEntityType entityType) {
        return entityType == MapEntityType.DELIVERY_GUY_CAR ? DeliveryType.CAR : DeliveryType.BIKE;
    }

    private int calculatePathLength(Location start, Location restaurant, Location client) {
        int pathLength = ShortestPathFinder.findShortestPath(mapLayout, start, restaurant);
        pathLength += ShortestPathFinder.findShortestPath(mapLayout, restaurant, client);

        return pathLength;
    }

    private int calculateEstimatedTime(int pathLength, DeliveryType deliveryType) {
        return pathLength * deliveryType.getTimePerKm();
    }

    private double calculateEstimatedPrice(int pathLength, DeliveryType deliveryType) {
        return pathLength * deliveryType.getPricePerKm();
    }

    private boolean isWithinPriceConstraint(double estimatedPrice, double maxPrice) {
        return maxPrice == -1 || estimatedPrice <= maxPrice;
    }

    private boolean isWithinTimeConstraint(int estimatedTime, int maxTime) {
        return maxTime == -1 || estimatedTime <= maxTime;
    }

    private boolean shouldReplace(DeliveryInfo currentBest, DeliveryInfo candidate, ShippingMethod method) {
        if (currentBest == null) {
            return true;
        }

        return switch (method) {
            case FASTEST -> candidate.estimatedTime() < currentBest.estimatedTime();
            case CHEAPEST -> candidate.price() < currentBest.price();
        };
    }
}
