package com.sayazhan;

import java.util.Comparator;
import java.util.List;

public class BalancedRouteStrategy implements RouteStrategy {

    private final double timeWeight;
    private final double costWeight;
    private final double distanceWeight;
    private final double emissionWeight;

    public BalancedRouteStrategy(
            double timeWeight,
            double costWeight,
            double distanceWeight,
            double emissionWeight
    ) {
        double sum = timeWeight + costWeight + distanceWeight + emissionWeight;

        if (!Double.isFinite(sum)
                || !Double.isFinite(timeWeight) || timeWeight < 0
                || !Double.isFinite(costWeight) || costWeight < 0
                || !Double.isFinite(distanceWeight) || distanceWeight < 0
                || !Double.isFinite(emissionWeight) || emissionWeight < 0
                || Math.abs(sum - 1.0) > 0.000001) {
            throw new IllegalArgumentException("Weights must be non-negative and sum to 1.0");
        }

        this.timeWeight = timeWeight;
        this.costWeight = costWeight;
        this.distanceWeight = distanceWeight;
        this.emissionWeight = emissionWeight;
    }

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        List<Route> validRoutes = RouteFilter.getValidRoutes(routes, request);

        if (validRoutes.isEmpty()) {
            throw new IllegalStateException("No suitable route found");
        }

        return validRoutes.stream()
                .min(Comparator.comparingDouble(route ->
                        calculateScore(route, validRoutes, request)))
                .orElseThrow();
    }

    public double calculateScore(
            Route route,
            List<Route> routes,
            DeliveryRequest request
    ) {
        double maxTime = routes.stream()
                .mapToInt(RouteFilter::getFinalTime)
                .max().orElse(1);

        double maxCost = routes.stream()
                .mapToDouble(r -> RouteFilter.getTotalCost(r, request))
                .max().orElse(1);

        double maxDistance = routes.stream()
                .mapToDouble(Route::distanceKm)
                .max().orElse(1);

        double maxEmission = routes.stream()
                .mapToDouble(Route::carbonEmission)
                .max().orElse(1);

        double normalizedTime = maxTime == 0 ? 0
                : RouteFilter.getFinalTime(route) / maxTime;

        double normalizedCost = maxCost == 0 ? 0
                : RouteFilter.getTotalCost(route, request) / maxCost;

        double normalizedDistance = maxDistance == 0 ? 0
                : route.distanceKm() / maxDistance;

        double normalizedEmission = maxEmission == 0 ? 0
                : route.carbonEmission() / maxEmission;

        return normalizedTime * timeWeight
                + normalizedCost * costWeight
                + normalizedDistance * distanceWeight
                + normalizedEmission * emissionWeight;
    }

    @Override
    public String getStrategyName() {
        return "Balanced Route";
    }



    @Override
    public String explainRoute(Route route, DeliveryRequest request) {

        return "Balanced strategy considers four criteria:\n" +
                "Time weight: " + timeWeight + "\n" +
                "Cost weight: " + costWeight + "\n" +
                "Distance weight: " + distanceWeight + "\n" +
                "Emission weight: " + emissionWeight + "\n" +
                "Final time: " + RouteFilter.getFinalTime(route) + " min\n" +
                "Total cost: " + RouteFilter.getTotalCost(route, request) + " KZT\n" +
                "Distance: " + route.distanceKm() + " km\n" +
                "CO2 emission: " + route.carbonEmission() + " kg\n" +
                "This route has the lowest weighted score among valid routes.";
    }
}