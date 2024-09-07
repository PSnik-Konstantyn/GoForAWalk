package org.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        double startLat = 46.9707233223874;
        double startLon = 31.998582187722192;


        Map<String, List<String>> categories = new HashMap<>();
        categories.put("tourism", Arrays.asList("museum", "artwork"));
        categories.put("leisure", Arrays.asList("park"));
        categories.put("amenity", Arrays.asList("cafe"));

        Map<String, Integer> preferredCategories = new HashMap<>();
//        preferredCategories.put("park", 1);
       preferredCategories.put("cafe", 1);

        double minDistance = 3;
        double maxDistance = 5;

        OverpassAPIClient apiClient = new OverpassAPIClient();
        try {

            List<Place> places = apiClient.getPlaces(startLat, startLon, categories);

            RoutePlanner planner = new RoutePlanner();

            String routeDetails = planner.planRoute(places, startLat, startLon, minDistance, maxDistance, preferredCategories);

            System.out.println("Запланований маршрут:");
            System.out.println(routeDetails);
        } catch (IOException e) {
            System.out.println("Помилка отримання місць: " + e.getMessage());
        }
    }
}
