package com.sayazhan;

import java.util.Comparator;
import java.util.List;

public class FastestRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        List<Route> validRoutes = RouteFilter.getValidRoutes(routes, request);

        return validRoutes.stream()
                .min(Comparator.comparingInt(RouteFilter::getFinalTime))
                .orElseThrow(() -> new IllegalStateException("No suitable route found"));
    }


    @Override
    public String getStrategyName() {
        return "Fastest Route";
    }


    @Override
    public String explainRoute(Route route, DeliveryRequest request) {

        int baseTime = route.estimatedMinutes();
        int trafficPenalty = route.trafficLevel() * 3;
        int finalTime = RouteFilter.getFinalTime(route);

        return "Base time: " + baseTime + " min\n" +
                "Traffic penalty: +" + trafficPenalty + " min\n" +
                "Final time: " + finalTime + " min\n" +
                "This route has the lowest delivery time among valid routes.";
    }
}