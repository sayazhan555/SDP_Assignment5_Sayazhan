package com.sayazhan;

import java.util.Comparator;
import java.util.List;

public class DistanceFirstRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        List<Route> validRoutes = RouteFilter.getValidRoutes(routes, request);

        return validRoutes.stream()
                .min(Comparator.comparingDouble(Route::distanceKm))
                .orElseThrow(() -> new IllegalStateException("No suitable route found"));
    }

    @Override
    public String getStrategyName() {
        return "Shortest Distance Route";
    }
}