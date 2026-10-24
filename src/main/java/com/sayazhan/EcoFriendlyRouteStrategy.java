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



    @Override
    public String explainRoute(Route route, DeliveryRequest request) {

        double emissionScore = route.carbonEmission() * 0.8;
        double distanceScore = route.distanceKm() * 0.2;
        double totalScore = calculateEcoScore(route);

        return "CO2 emission: " + route.carbonEmission() + " kg\n" +
                "Distance: " + route.distanceKm() + " km\n" +
                "Emission contribution: " + emissionScore + "\n" +
                "Distance contribution: " + distanceScore + "\n" +
                String.format(java.util.Locale.US, "Final eco score: %.2f\n", totalScore) +
                "This route has the lowest eco score among valid routes.";
    }
}