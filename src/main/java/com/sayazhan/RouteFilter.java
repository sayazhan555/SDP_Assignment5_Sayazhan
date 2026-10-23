package com.sayazhan;

import java.util.List;

public class RouteFilter {

    public static List<Route> getValidRoutes(
            List<Route> routes,
            DeliveryRequest request
    ) {
        return routes.stream()
                .filter(Route::available)
                .filter(route -> getTotalCost(route, request) <= request.maxBudget())
                .filter(route -> getFinalTime(route) <= request.maxDeliveryMinutes())
                .toList();
    }

    public static int getFinalTime(Route route) {
        return route.estimatedMinutes() + route.trafficLevel() * 3;
    }

    public static double getTotalCost(Route route, DeliveryRequest request) {

        double distanceFee = route.distanceKm() * 50;

        double priorityFee = 0;

        if (request.priority() == DeliveryPriority.URGENT) {
            priorityFee = 500;
        }

        return route.cost() + distanceFee + priorityFee;
    }
}