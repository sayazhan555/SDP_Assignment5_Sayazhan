package com.sayazhan;

import java.util.List;
import java.util.Scanner;

public class DeliveryMenu {

    private final List<Route> routes;
    private final Scanner scanner = new Scanner(System.in);

    private final RoutePlanner planner =
            new RoutePlanner(new FastestRouteStrategy());

    private final List<RouteStrategy> strategies = List.of(
            new FastestRouteStrategy(),
            new CheapestRouteStrategy(),
            new EcoFriendlyRouteStrategy(),
            new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2),
            new DistanceFirstRouteStrategy()
    );

    public DeliveryMenu(List<Route> routes) {
        this.routes = routes;
    }

    public void start() {

        while (true) {
            System.out.println();
            System.out.println("SMART DELIVERY MENU");
            System.out.println("1. Show all routes");
            System.out.println("2. Find fastest route");
            System.out.println("3. Find cheapest route");
            System.out.println("4. Find eco-friendly route");
            System.out.println("5. Find balanced route");
            System.out.println("6. Compare all strategies");
            System.out.println("7. Custom delivery request");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> showRoutes();
                case "2" -> findRoute(strategies.get(0), defaultRequest());
                case "3" -> findRoute(strategies.get(1), defaultRequest());
                case "4" -> findRoute(strategies.get(2), defaultRequest());
                case "5" -> findRoute(strategies.get(3), defaultRequest());
                case "6" -> compareRoutes(defaultRequest());
                case "7" -> customRequest();

                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private DeliveryRequest defaultRequest() {
        return new DeliveryRequest(
                DeliveryPriority.STANDARD, 6000, 45, false
        );
    }

    private void showRoutes() {
        System.out.println();
        System.out.println("AVAILABLE ROUTES");

        for (Route route : routes) {
            System.out.println(
                    route.id() +
                            " | " + route.distanceKm() + " km" +
                            " | " + RouteFilter.getFinalTime(route) + " min" +
                            " | Available: " + route.available()
            );
        }
    }

    private void findRoute(RouteStrategy strategy, DeliveryRequest request) {
        try {
            planner.setStrategy(strategy);

            Route selected = planner.plan(routes, request);

            System.out.println();
            System.out.println("Strategy: " + strategy.getStrategyName());
            System.out.println("Selected: " + selected.id());
            System.out.println("Time: " +
                    RouteFilter.getFinalTime(selected) + " min");
            System.out.println("Cost: " +
                    RouteFilter.getTotalCost(selected, request) + " KZT");
            System.out.println("Distance: " + selected.distanceKm() + " km");

        } catch (NoSuitableRouteException e) {
            System.out.println("No suitable route found.");
        }
    }

    private void compareRoutes(DeliveryRequest request) {
        StrategyComparison comparison = new StrategyComparison(strategies);
        comparison.compare(routes, request);
    }

    private void customRequest() {
        try {
            System.out.print("Enter maximum budget (KZT): ");
            double budget = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter maximum delivery time (minutes): ");
            int minutes = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Choose priority:");
            System.out.println("1. URGENT");
            System.out.println("2. ECONOMY");
            System.out.println("3. ECO");
            System.out.println("4. STANDARD");
            System.out.print("Priority: ");

            int priorityChoice = Integer.parseInt(scanner.nextLine().trim());

            DeliveryPriority priority = switch (priorityChoice) {
                case 1 -> DeliveryPriority.URGENT;
                case 2 -> DeliveryPriority.ECONOMY;
                case 3 -> DeliveryPriority.ECO;
                case 4 -> DeliveryPriority.STANDARD;
                default -> throw new IllegalArgumentException("Invalid priority");
            };

            DeliveryRequest request = new DeliveryRequest(
                    priority, budget, minutes,
                    priority == DeliveryPriority.ECO
            );

            StrategySelector selector = new StrategySelector();
            RouteStrategy strategy = selector.selectStrategy(request);

            findRoute(strategy, request);

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
