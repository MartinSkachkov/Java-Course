import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // LOCATION
        Location l = new Location(7, 4);
        try {
            Location l1 = new Location(7, 4);
            System.out.println(l.x());
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        // MapEntityType
        MapEntityType t = MapEntityType.CLIENT;
        System.out.println(t); // CLIENT
        System.out.println(t.getEntityTypeSymbol()); // 'C'

        System.out.println(MapEntityType.fromChar('B')); // DELIVERY_GUY_BIKE
        System.out.println(MapEntityType.fromChar('A')); // DELIVERY_GUY_CAR
        try {
            System.out.println(MapEntityType.fromChar('M'));
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        // MapEntity
        try {
            MapEntity me = new MapEntity(null, t);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        MapEntity me = new MapEntity(l, t);
        System.out.println(me); // MapEntity[location=Location[x=7, y=4], type=CLIENT]
        System.out.println("Location: " + me.location());
        System.out.println("Type: " + me.type());

        // Delivery
        Location client = new Location(3, 2);
        Location restaurant = new Location(4, 8);
        Location deliveryGuy = new Location(0, 0);
        Location sameClient = new Location(3, 2);

        System.out.println(client.equals(restaurant)); // false
        System.out.println(client.equals(sameClient)); // true

        try {
            Delivery d = new Delivery(client, restaurant, deliveryGuy, "pizza", 1, 7);
        } catch (RuntimeException e) {
            System.out.println(e);
        }

        // Shipping Method
        ShippingMethod sm = ShippingMethod.CHEAPEST;
        System.out.println(sm); // CHEAPEST

        // Delivery Type
        DeliveryType c = DeliveryType.CAR;
        System.out.println(c + String.valueOf(c.getPricePerKm()) + String.valueOf(c.getTimePerKm())); // CAR5.03

        // Control Center
        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', 'B', 'R', '.'},
                {'.', '.', '#', '.', '#'},
                {'#', 'C', '.', 'A', '.'},
                {'#', '.', '#', '#', '#'}
        };

        ControlCenter cc = new ControlCenter(layout);
        MapEntity[][] convertedMap = cc.getLayout();

        System.out.println(Arrays.deepToString(convertedMap));
        // [[MapEntity[location=Location[x=0, y=0], type=WALL], MapEntity[location=Location[x=0, y=1], type=WALL], MapEntity[location=Location[x=0, y=2], type=WALL], MapEntity[location=Location[x=0, y=3], type=ROAD], MapEntity[location=Location[x=0, y=4], type=WALL]],
        //  [MapEntity[location=Location[x=1, y=0], type=WALL], MapEntity[location=Location[x=1, y=1], type=ROAD], MapEntity[location=Location[x=1, y=2], type=DELIVERY_GUY_BIKE], MapEntity[location=Location[x=1, y=3], type=RESTAURANT], MapEntity[location=Location[x=1, y=4], type=ROAD]],
        //  [MapEntity[location=Location[x=2, y=0], type=ROAD], MapEntity[location=Location[x=2, y=1], type=ROAD], MapEntity[location=Location[x=2, y=2], type=WALL], MapEntity[location=Location[x=2, y=3], type=ROAD], MapEntity[location=Location[x=2, y=4], type=WALL]],
        //  [MapEntity[location=Location[x=3, y=0], type=WALL], MapEntity[location=Location[x=3, y=1], type=CLIENT], MapEntity[location=Location[x=3, y=2], type=ROAD], MapEntity[location=Location[x=3, y=3], type=DELIVERY_GUY_CAR], MapEntity[location=Location[x=3, y=4], type=ROAD]],
        //  [MapEntity[location=Location[x=4, y=0], type=WALL], MapEntity[location=Location[x=4, y=1], type=ROAD], MapEntity[location=Location[x=4, y=2], type=WALL], MapEntity[location=Location[x=4, y=3], type=WALL], MapEntity[location=Location[x=4, y=4], type=WALL]]]

        //System.out.println(cc.test()); // [MapEntity[location=Location[x=1, y=2], type=DELIVERY_GUY_BIKE], MapEntity[location=Location[x=3, y=3], type=DELIVERY_GUY_CAR]]

        // ShortestPathFinder
        Location car = new Location(3, 3);
        Location rest = new Location(1, 3);
        Location cli = new Location(3, 1);

        //System.out.println(ShortestPathFinder.findShortestPath(convertedMap, car, rest)); // 2
        //System.out.println(ShortestPathFinder.findShortestPath(convertedMap, rest, cli)); // 4

        System.out.println(cc.findOptimalDeliveryGuy(restaurant, cli, -1, -1, ShippingMethod.FASTEST));
    }
}