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



    @Override
    public String explainRoute(Route route, DeliveryRequest request) {

        return "Selected distance: " + route.distanceKm() + " km\n" +
                "Final time: " + RouteFilter.getFinalTime(route) + " min\n" +
                "Total cost: " + RouteFilter.getTotalCost(route, request) + " KZT\n" +
                "This route has the shortest distance among valid routes.";
    }
}