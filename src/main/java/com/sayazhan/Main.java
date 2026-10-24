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
                        20.0, 30, 2500, 2.0, 0, true),

                new Route("Route D", warehouse, airport,
                        22.0, 19, 4500, 4.5, 0, true),

                new Route("Route E", warehouse, airport,
                        24.0, 34, 1700, 1.3, 1, true),

                new Route("Route F", warehouse, airport,
                        16.0, 26, 3200, 2.7, 2, true),

                new Route("Route G", warehouse, airport,
                        12.0, 18, 1500, 1.0, 0, false),

                new Route("Route H", warehouse, airport,
                        19.0, 24, 3600, 0.9, 1, true)
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
        System.out.printf("Eco score: %.2f%n",
                ecoStrategy.calculateEcoScore(ecoRoute));
        System.out.println("Total cost: " +
                RouteFilter.getTotalCost(ecoRoute, request) + " KZT");




        System.out.println();

        BalancedRouteStrategy balancedStrategy =
                new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2);

        planner.setStrategy(balancedStrategy);

        Route balancedRoute = planner.plan(routes, request);

        List<Route> validRoutes = RouteFilter.getValidRoutes(routes, request);

        double balancedScore =
                balancedStrategy.calculateScore(balancedRoute, validRoutes, request);

        System.out.println("BALANCED ROUTE RESULT");
        System.out.println("Strategy: " + balancedStrategy.getStrategyName());
        System.out.println("Selected: " + balancedRoute.id());
        System.out.println("Final time: " +
                RouteFilter.getFinalTime(balancedRoute) + " min");
        System.out.println("Total cost: " +
                RouteFilter.getTotalCost(balancedRoute, request) + " KZT");
        System.out.println("Distance: " + balancedRoute.distanceKm() + " km");
        System.out.println("CO2 emission: " + balancedRoute.carbonEmission() + " kg");
        System.out.printf("Balanced score: %.3f%n", balancedScore);





        System.out.println();
        System.out.println("AUTOMATIC STRATEGY SELECTION");

        StrategySelector selector = new StrategySelector();

        DeliveryRequest urgentRequest = new DeliveryRequest(
                DeliveryPriority.URGENT, 6000, 40, false
        );

        DeliveryRequest economyRequest = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 5000, 50, false
        );

        DeliveryRequest ecoRequest = new DeliveryRequest(
                DeliveryPriority.ECO, 5000, 50, true
        );

        DeliveryRequest standardRequest = new DeliveryRequest(
                DeliveryPriority.STANDARD, 6000, 45, false
        );

        List<DeliveryRequest> requests = List.of(
                urgentRequest,
                economyRequest,
                ecoRequest,
                standardRequest
        );

        for (DeliveryRequest currentRequest : requests) {

            RouteStrategy selectedStrategy = selector.selectStrategy(currentRequest);

            planner.setStrategy(selectedStrategy);

            Route result = planner.plan(routes, currentRequest);

            System.out.println();
            System.out.println("Priority: " + currentRequest.priority());
            System.out.println("Strategy: " + selectedStrategy.getStrategyName());
            System.out.println("Selected route: " + result.id());
        }




        System.out.println();
        System.out.println("SHORTEST DISTANCE ROUTE RESULT");

        planner.setStrategy(new DistanceFirstRouteStrategy());

        Route shortestRoute = planner.plan(routes, request);

        System.out.println("Strategy: Shortest Distance Route");
        System.out.println("Selected: " + shortestRoute.id());
        System.out.println("Distance: " + shortestRoute.distanceKm() + " km");
        System.out.println("Final time: " +
                RouteFilter.getFinalTime(shortestRoute) + " min");
        System.out.println("Total cost: " +
                RouteFilter.getTotalCost(shortestRoute, request) + " KZT");



        System.out.println();

        RouteExplanationService explanationService = new RouteExplanationService();


        List<RouteStrategy> allStrategies = List.of(
                new FastestRouteStrategy(),
                new CheapestRouteStrategy(),
                new EcoFriendlyRouteStrategy(),
                new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2),
                new DistanceFirstRouteStrategy()
        );

        System.out.println();
        System.out.println("STRATEGY EXPLANATIONS");

        for (RouteStrategy currentStrategy : allStrategies) {

            RouteDecision decision = explanationService.explain(
                    currentStrategy,
                    routes,
                    request
            );

            decision.printDecision();
        }


        System.out.println();

        StrategyComparison comparison = new StrategyComparison(allStrategies);

        comparison.compare(routes, request);



        System.out.println();
        System.out.println("FAILURE SCENARIOS");

        // Test 1: Empty route list
        try {
            planner.plan(List.of(), request);
        } catch (NoSuitableRouteException e) {
            System.out.println("Test 1 - Empty routes: " + e.getMessage());
        }

        // Test 2: Very low budget
        DeliveryRequest lowBudgetRequest = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 500, 60, false
        );

        try {
            planner.plan(routes, lowBudgetRequest);
        } catch (NoSuitableRouteException e) {
            System.out.println("Test 2 - Low budget: " + e.getMessage());
        }

        // Test 3: Short deadline
        DeliveryRequest shortDeadlineRequest = new DeliveryRequest(
                DeliveryPriority.URGENT, 10000, 10, false
        );

        try {
            planner.plan(routes, shortDeadlineRequest);
        } catch (NoSuitableRouteException e) {
            System.out.println("Test 3 - Short deadline: " + e.getMessage());
        }

        // Test 4: Invalid balanced weights
        try {
            new BalancedRouteStrategy(0.5, 0.5, 0.5, 0.5);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 4 - Invalid weights: " + e.getMessage());
        }
    }
}