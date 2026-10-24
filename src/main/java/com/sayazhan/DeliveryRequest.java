package com.sayazhan;

public record DeliveryRequest(
        DeliveryPriority priority,
        double maxBudget,
        int maxDeliveryMinutes,
        boolean ecoPreferred
) {
    public DeliveryRequest {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }

        if (!Double.isFinite(maxBudget) || maxBudget < 0) {
            throw new IllegalArgumentException("Budget must be valid and non-negative");
        }

        if (maxDeliveryMinutes < 0) {
            throw new IllegalArgumentException("Deadline cannot be negative");
        }
    }
}