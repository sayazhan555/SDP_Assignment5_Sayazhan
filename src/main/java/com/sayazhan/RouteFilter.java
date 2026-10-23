package com.sayazhan;

import java.util.List;

public class RouteFilter {

    public static List<Route> getValidRoutes(
            List<Route> routes,
            DeliveryRequest request
    ) {
        return routes.stream()
                .filter(Route::available)
                .filter(route -> route.cost() <= request.maxBudget())
                .filter(route -> getFinalTime(route) <= request.maxDeliveryMinutes())
                .toList();
    }

    public static int getFinalTime(Route route) {
        return route.estimatedMinutes() + route.trafficLevel() * 3;
    }
}