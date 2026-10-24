package com.sayazhan;

import java.util.Comparator;
import java.util.List;

public class EcoFriendlyRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        List<Route> validRoutes = RouteFilter.getValidRoutes(routes, request);

        return validRoutes.stream()
                .min(Comparator.comparingDouble(this::calculateEcoScore))
                .orElseThrow(() -> new IllegalStateException("No suitable route found"));
    }

    public double calculateEcoScore(Route route) {
        return route.carbonEmission() * 0.8
                + route.distanceKm() * 0.2;
    }

    @Override
    public String getStrategyName() {
        return "Eco-Friendly Route";
    }
}