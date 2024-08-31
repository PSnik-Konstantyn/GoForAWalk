package org.example;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        double startLat = 46.9707233223874;
        double startLon = 31.998582187722192;

        String category = "museum";
        double minDistance = 3;
        double maxDistance = 5;

        OverpassAPIClient apiClient = new OverpassAPIClient();
        try {
            List<Place> places = apiClient.getPlaces(startLat, startLon, category);

            RoutePlanner planner = new RoutePlanner();
            String routeDetails = planner.planRoute(places, startLat, startLon, minDistance, maxDistance);

            System.out.println("Planned Route:");
            System.out.println(routeDetails);
        } catch (IOException e) {
            System.out.println("Error fetching places: " + e.getMessage());
        }
    }
}
