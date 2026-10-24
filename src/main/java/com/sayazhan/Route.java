package com.sayazhan;

public record Route(
        String id,
        Location origin,
        Location destination,
        double distanceKm,
        int estimatedMinutes,
        double cost,
        double carbonEmission,
        int trafficLevel,
        boolean available
) {
    public Route {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be empty");
        }

        if (origin == null || destination == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }

        if (!Double.isFinite(distanceKm) || distanceKm < 0 ||
                estimatedMinutes < 0 ||
                !Double.isFinite(cost) || cost < 0 ||
                !Double.isFinite(carbonEmission) || carbonEmission < 0) {
            throw new IllegalArgumentException("Route values cannot be negative or invalid");
        }

        if (trafficLevel < 0 || trafficLevel > 5) {
            throw new IllegalArgumentException("Traffic level must be between 0 and 5");
        }
    }
}