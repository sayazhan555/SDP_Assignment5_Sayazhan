package com.sayazhan;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Location warehouse = new Location("Warehouse", 51.1694, 71.4491);
        Location airport = new Location("Astana Airport", 51.0222, 71.4669);

        List<Route> routes = List.of(
                new Route("Route A", warehouse, airport,
                        15.5, 20, 5000, 4.0, 5, true),

                new Route("Route B", warehouse, airport,
                        18.0, 25, 3000, 3.0, 1, true),

                new Route("Route C", warehouse, airport,
                        20.0, 30, 2500, 2.0, 0, true)
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT,
                6000,
                40,
                false
        );

        RoutePlanner planner = new RoutePlanner(new FastestRouteStrategy());

        Route selectedRoute = planner.plan(routes, request);

        System.out.println(" SMART DELIVERY SYSTEM ");
        System.out.println("Strategy: Fastest Route");
        System.out.println("Selected: " + selectedRoute.id());
        System.out.println("Distance: " + selectedRoute.distanceKm() + " km");
        System.out.println("Estimated time: " + selectedRoute.estimatedMinutes() + " min");
        System.out.println("Traffic level: " + selectedRoute.trafficLevel());
        System.out.println("Final time: " +
                RouteFilter.getFinalTime(selectedRoute) + " min");
        System.out.println("Cost: " + selectedRoute.cost() + " KZT");
    }
}