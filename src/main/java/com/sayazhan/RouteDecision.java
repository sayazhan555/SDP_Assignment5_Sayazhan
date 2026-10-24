package com.sayazhan;

import java.util.List;

public record RouteDecision(
        Route selectedRoute,
        String strategyName,
        String reason,
        List<String> rejectedRoutes
) {
    public RouteDecision {
        rejectedRoutes = List.copyOf(rejectedRoutes);
    }

    public void printDecision() {

        System.out.println();
        System.out.println("ROUTE DECISION");

        System.out.println("Selected: " + selectedRoute.id());
        System.out.println("Strategy: " + strategyName);

        System.out.println();
        System.out.println("Reason:");
        System.out.println(reason);

        System.out.println();
        System.out.println("Rejected routes:");

        if (rejectedRoutes.isEmpty()) {
            System.out.println("None");
        } else {
            for (String rejected : rejectedRoutes) {
                System.out.println(rejected);
            }
        }
    }
}