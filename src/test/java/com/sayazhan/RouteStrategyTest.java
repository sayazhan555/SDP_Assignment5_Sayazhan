package com.sayazhan;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteStrategyTest {

    Location start = new Location("Warehouse", 51.1, 71.4);
    Location end = new Location("Airport", 51.0, 71.5);

    Route a = new Route("A", start, end,
            10, 20, 3000, 3.0, 4, true);

    Route b = new Route("B", start, end,
            15, 25, 2000, 1.0, 0, true);

    Route c = new Route("C", start, end,
            8, 15, 1000, 0.5, 0, false);

    List<Route> routes = List.of(a, b, c);

    DeliveryRequest request = new DeliveryRequest(
            DeliveryPriority.STANDARD, 6000, 60, false
    );

    // 1. Fastest strategy
    @Test
    void fastestSelectsCorrectRoute() {
        Route result = new FastestRouteStrategy().selectRoute(routes, request);
        assertEquals("B", result.id());
    }

    // 2. Traffic affects delivery time
    @Test
    void trafficPenaltyIsCalculated() {
        assertEquals(32, RouteFilter.getFinalTime(a));
    }

    // 3. Cheapest strategy
    @Test
    void cheapestSelectsCorrectRoute() {
        Route result = new CheapestRouteStrategy().selectRoute(routes, request);
        assertEquals("B", result.id());
    }

    // 4. Total cost calculation
    @Test
    void totalCostIsCalculated() {
        assertEquals(2750.0, RouteFilter.getTotalCost(b, request), 0.001);
    }

    // 5. Urgent delivery surcharge
    @Test
    void urgentDeliveryHasExtraFee() {
        DeliveryRequest urgent = new DeliveryRequest(
                DeliveryPriority.URGENT, 6000, 60, false
        );

        assertEquals(3250.0, RouteFilter.getTotalCost(b, urgent), 0.001);
    }

    // 6. Eco strategy
    @Test
    void ecoSelectsCorrectRoute() {
        Route result = new EcoFriendlyRouteStrategy().selectRoute(routes, request);
        assertEquals("B", result.id());
    }

    // 7. Eco score
    @Test
    void ecoScoreIsCalculated() {
        EcoFriendlyRouteStrategy eco = new EcoFriendlyRouteStrategy();
        assertEquals(3.8, eco.calculateEcoScore(b), 0.001);
    }

    // 8. Balanced strategy
    @Test
    void balancedSelectsValidRoute() {
        BalancedRouteStrategy balanced =
                new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2);

        Route result = balanced.selectRoute(routes, request);
        assertEquals("B", result.id());
    }

    // 9. Invalid weight sum
    @Test
    void invalidWeightsThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new BalancedRouteStrategy(0.5, 0.5, 0.5, 0.5));
    }

    // 10. Negative weight
    @Test
    void negativeWeightThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new BalancedRouteStrategy(-0.1, 0.5, 0.3, 0.3));
    }

    // 11. Runtime switching
    @Test
    void runtimeSwitchingWorks() {
        RoutePlanner planner = new RoutePlanner(new FastestRouteStrategy());

        assertEquals("B", planner.plan(routes, request).id());

        planner.setStrategy(new DistanceFirstRouteStrategy());

        assertEquals("A", planner.plan(routes, request).id());
    }

    // 12. Automatic strategy selection
    @Test
    void selectorChoosesFastestForUrgent() {
        StrategySelector selector = new StrategySelector();

        DeliveryRequest urgent = new DeliveryRequest(
                DeliveryPriority.URGENT, 6000, 60, false
        );

        assertInstanceOf(FastestRouteStrategy.class,
                selector.selectStrategy(urgent));
    }

    // 13. Economy selection
    @Test
    void selectorChoosesCheapestForEconomy() {
        StrategySelector selector = new StrategySelector();

        DeliveryRequest economy = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 6000, 60, false
        );

        assertInstanceOf(CheapestRouteStrategy.class,
                selector.selectStrategy(economy));
    }

    // 14. Unavailable route
    @Test
    void unavailableRouteIsRejected() {
        List<Route> valid = RouteFilter.getValidRoutes(routes, request);

        assertFalse(valid.contains(c));
    }

    // 15. Budget restriction
    @Test
    void budgetRestrictionWorks() {
        DeliveryRequest lowBudget = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 2500, 60, false
        );

        assertTrue(RouteFilter.getValidRoutes(routes, lowBudget).isEmpty());
    }

    // 16. Deadline restriction
    @Test
    void deadlineRestrictionWorks() {
        DeliveryRequest shortDeadline = new DeliveryRequest(
                DeliveryPriority.URGENT, 6000, 20, false
        );

        assertTrue(RouteFilter.getValidRoutes(routes, shortDeadline).isEmpty());
    }

    // 17. Empty routes
    @Test
    void emptyRoutesThrowException() {
        assertThrows(NoSuitableRouteException.class,
                () -> new FastestRouteStrategy()
                        .selectRoute(List.of(), request));
    }

    // 18. No suitable route
    @Test
    void noSuitableRouteThrowsException() {
        DeliveryRequest impossible = new DeliveryRequest(
                DeliveryPriority.STANDARD, 100, 5, false
        );

        assertThrows(NoSuitableRouteException.class,
                () -> new CheapestRouteStrategy()
                        .selectRoute(routes, impossible));
    }

    // 19. Fifth strategy
    @Test
    void distanceFirstSelectsShortestValidRoute() {
        Route result = new DistanceFirstRouteStrategy()
                .selectRoute(routes, request);

        assertEquals("A", result.id());
    }

    // 20. Negative distance
    @Test
    void negativeDistanceThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Route(
                        "Invalid", start, end,
                        -10, 20, 1000, 1.0, 0, true
                ));
    }
}