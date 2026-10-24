package com.sayazhan;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class StrategySelector {

    private final Map<DeliveryPriority, Supplier<RouteStrategy>> strategies =
            new EnumMap<>(DeliveryPriority.class);

    public StrategySelector() {
        register(DeliveryPriority.URGENT, FastestRouteStrategy::new);
        register(DeliveryPriority.ECONOMY, CheapestRouteStrategy::new);
        register(DeliveryPriority.ECO, EcoFriendlyRouteStrategy::new);

        register(DeliveryPriority.STANDARD,
                () -> new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2));
    }

    public void register(
            DeliveryPriority priority,
            Supplier<RouteStrategy> strategy
    ) {
        strategies.put(
                Objects.requireNonNull(priority),
                Objects.requireNonNull(strategy)
        );
    }

    public RouteStrategy selectStrategy(DeliveryRequest request) {

        Objects.requireNonNull(request, "Delivery request cannot be null");

        Supplier<RouteStrategy> strategy = strategies.get(request.priority());

        if (strategy == null) {
            throw new IllegalArgumentException("Unknown delivery priority");
        }

        return Objects.requireNonNull(strategy.get());
    }
}