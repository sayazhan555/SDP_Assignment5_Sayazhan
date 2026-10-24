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

        String explanation = strategy.explainRoute(selected, request);


        return new RouteDecision(
                selected,
                strategy.getStrategyName(),
                explanation,
                rejected
        );
    }
}