package com.sayazhan;

import java.util.List;

public class StrategyComparison {

    private final List<RouteStrategy> strategies;

    public StrategyComparison(List<RouteStrategy> strategies) {
        this.strategies = List.copyOf(strategies);
    }

    public void compare(List<Route> routes, DeliveryRequest request) {

        System.out.println();
        System.out.println("STRATEGY COMPARISON");
        System.out.println("Priority: " + request.priority());
        System.out.println("Budget: " + request.maxBudget() + " KZT");
        System.out.println("Deadline: " + request.maxDeliveryMinutes() + " min");

        for (RouteStrategy strategy : strategies) {

            System.out.println();
            System.out.println(strategy.getStrategyName() + ":");

            try {
                Route selected = strategy.selectRoute(routes, request);

                System.out.println("Selected: " + selected.id());
                System.out.println("Time: " +
                        RouteFilter.getFinalTime(selected) + " min");
                System.out.println("Cost: " +
                        RouteFilter.getTotalCost(selected, request) + " KZT");
                System.out.println("Distance: " +
                        selected.distanceKm() + " km");
                System.out.println("CO2: " +
                        selected.carbonEmission() + " kg");

            } catch (IllegalStateException e) {
                System.out.println("No suitable route: " + e.getMessage());
            }
        }
    }
}
