package com.sayazhan;

import java.util.ArrayList;
import java.util.List;

public class RouteExplanationService {

    public RouteDecision explain(
            RouteStrategy strategy,
            List<Route> routes,
            DeliveryRequest request
    ) {
        Route selected = strategy.selectRoute(routes, request);

        List<String> rejected = new ArrayList<>();

        for (Route route : routes) {
            String reason = RouteFilter.getRejectionReason(route, request);

            if (reason != null) {
                rejected.add(route.id() + " - " + reason);
            }
        }

        String explanation =
                "Selected route meets all delivery constraints.\n" +
                        "Final time: " + RouteFilter.getFinalTime(selected) + " min\n" +
                        "Total cost: " + RouteFilter.getTotalCost(selected, request) + " KZT\n" +
                        "Distance: " + selected.distanceKm() + " km\n" +
                        "CO2 emission: " + selected.carbonEmission() + " kg";

        return new RouteDecision(
                selected,
                strategy.getStrategyName(),
                explanation,
                rejected
        );
    }
}