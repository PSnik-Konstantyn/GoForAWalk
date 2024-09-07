package org.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        double startLat = 46.95521608496431;
        double startLon = 32.02635617253932;

        double minDistance = 3;
        double maxDistance = 5;

        List<String> preferredCategories = Arrays.asList("museum", "artwork", "park");

        OverpassAPIClient apiClient = new OverpassAPIClient();
        try {
            List<Map.Entry<String, String>> categories = apiClient.createCategories();

            List<Place> places = apiClient.getPlaces(startLat, startLon, categories);

            RoutePlanner planner = new RoutePlanner();
            String routeDetails = planner.planRoute(places, startLat, startLon, minDistance, maxDistance, preferredCategories);

            System.out.println("Planned Route:");
            System.out.println(routeDetails);
        } catch (IOException e) {
            System.out.println("Error fetching places: " + e.getMessage());
        }
    }
}
