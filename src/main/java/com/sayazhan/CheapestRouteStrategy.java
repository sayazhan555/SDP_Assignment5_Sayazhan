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
                .orElseThrow(() -> new IllegalStateException("No suitable route found"));
    }

    public double calculateCost(Route route, DeliveryRequest request) {
        return RouteFilter.getTotalCost(route, request);
    }

    @Override
    public String getStrategyName() {
        return "Cheapest Route";
    }
}