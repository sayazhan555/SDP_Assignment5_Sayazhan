package com.sayazhan;

import java.util.List;

public interface RouteStrategy {

    Route selectRoute(List<Route> routes, DeliveryRequest request);

    String getStrategyName();
}