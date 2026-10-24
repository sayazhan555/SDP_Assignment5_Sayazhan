package com.sayazhan;

import java.util.Comparator;
import java.util.List;

public class CheapestRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        List<Route> validRoutes = RouteFilter.getValidRoutes(routes, request);

        return validRoutes.stream()
                .filter(route -> calculateCost(route, request) <= request.maxBudget())
                .min(Comparator.comparingDouble(route -> calculateCost(route, request)))
                .orElseThrow(() -> new NoSuitableRouteException("No suitable route found"));
    }

    public double calculateCost(Route route, DeliveryRequest request) {
        return RouteFilter.getTotalCost(route, request);
    }

    @Override
    public String getStrategyName() {
        return "Cheapest Route";
    }


    @Override
    public String explainRoute(Route route, DeliveryRequest request) {

        double baseCost = route.cost();
        double distanceFee = route.distanceKm() * 50;

        double priorityFee = 0;

        if (request.priority() == DeliveryPriority.URGENT) {
            priorityFee = 500;
        }

        double totalCost = RouteFilter.getTotalCost(route, request);

        return "Base cost: " + baseCost + " KZT\n" +
                "Distance fee: " + distanceFee + " KZT\n" +
                "Priority fee: " + priorityFee + " KZT\n" +
                "Total cost: " + totalCost + " KZT\n" +
                "This route has the lowest total cost among valid routes.";
    }
}