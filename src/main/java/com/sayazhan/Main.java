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
        System.out.println("Total cost: " +
                RouteFilter.getTotalCost(selectedRoute, request) + " KZT");




        System.out.println();

        planner.setStrategy(new CheapestRouteStrategy());

        Route cheapestRoute = planner.plan(routes, request);

        CheapestRouteStrategy cheapestStrategy = new CheapestRouteStrategy();

        System.out.println("CHEAPEST ROUTE RESULT");
        System.out.println("Strategy: " + cheapestStrategy.getStrategyName());
        System.out.println("Selected: " + cheapestRoute.id());
        System.out.println("Distance: " + cheapestRoute.distanceKm() + " km");
        System.out.println("Final time: " + RouteFilter.getFinalTime(cheapestRoute) + " min");
        System.out.println("Total cost: " +
                cheapestStrategy.calculateCost(cheapestRoute, request) + " KZT");




        System.out.println();

        planner.setStrategy(new EcoFriendlyRouteStrategy());

        Route ecoRoute = planner.plan(routes, request);

        EcoFriendlyRouteStrategy ecoStrategy = new EcoFriendlyRouteStrategy();

        System.out.println("ECO-FRIENDLY ROUTE RESULT");
        System.out.println("Strategy: " + ecoStrategy.getStrategyName());
        System.out.println("Selected: " + ecoRoute.id());
        System.out.println("Distance: " + ecoRoute.distanceKm() + " km");
        System.out.println("CO2 emission: " + ecoRoute.carbonEmission() + " kg");
        System.out.println("Eco score: " + ecoStrategy.calculateEcoScore(ecoRoute));
        System.out.println("Total cost: " +
                RouteFilter.getTotalCost(ecoRoute, request) + " KZT");
    }
}