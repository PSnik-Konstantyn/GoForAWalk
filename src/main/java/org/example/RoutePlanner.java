package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RoutePlanner {

    private static final Random random = new Random();

    public String planRoute(List<Place> places, double startLat, double startLon, double minDistance, double maxDistance, Map<String, Integer> preferredCategories) {
        List<Place> route = new ArrayList<>();
        double currentLat = startLat;
        double currentLon = startLon;
        double totalDistance = 0;
        StringBuilder routeDetails = new StringBuilder();

        for (Place place : places) {
            System.out.println(place.toString());
        }

        places = filterPlacesByCategory(places, preferredCategories);

        int numPlaces = random.nextInt(5) + 3;
        double distancePerPlace = maxDistance / numPlaces;

        for (int i = 0; i < numPlaces && !places.isEmpty() && totalDistance < maxDistance; i++) {
            double remainingDistance = maxDistance - totalDistance;
            double searchRadius = remainingDistance / (numPlaces - i);

            Place selectedPlace = findPlaceWithinRadius(places, currentLat, currentLon, searchRadius);
            if (selectedPlace == null) {
                selectedPlace = findRandomPlace(places, currentLat, currentLon);
            }

            double distanceToPlace = calculateDistance(currentLat, currentLon, selectedPlace.getLat(), selectedPlace.getLon());

            if (totalDistance + distanceToPlace <= maxDistance) {
                route.add(selectedPlace);
                totalDistance += distanceToPlace;
                routeDetails.append(selectedPlace.toString()).append(" - ").append(String.format("%.2f", distanceToPlace)).append(" км - ");
                currentLat = selectedPlace.getLat();
                currentLon = selectedPlace.getLon();
                places.remove(selectedPlace);
            } else {
                break;
            }
        }

        double returnDistance = calculateDistance(currentLat, currentLon, startLat, startLon);
        totalDistance += returnDistance;
        routeDetails.append("Повернення - ").append(String.format("%.2f", returnDistance)).append(" км\n");
        routeDetails.append("Загальна довжина маршруту: ").append(String.format("%.2f", totalDistance)).append(" км");

        return routeDetails.toString();
    }

    private List<Place> filterPlacesByCategory(List<Place> places, Map<String, Integer> preferredCategories) {
        List<Place> filteredPlaces = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : preferredCategories.entrySet()) {
            String category = entry.getKey();
            int limit = entry.getValue();
            long count = places.stream().filter(place -> place.getCategory().equals(category)).count();
            places.stream().filter(place -> place.getCategory().equals(category))
                    .limit(Math.min(limit, count)).forEach(filteredPlaces::add);
        }
        Collections.shuffle(filteredPlaces);
        return filteredPlaces;
    }

    private Place findPlaceWithinRadius(List<Place> places, double lat, double lon, double radius) {
        for (Place place : places) {
            double distance = calculateDistance(lat, lon, place.getLat(), place.getLon());
            if (distance <= radius) {
                return place;
            }
        }
        return null;
    }

    private Place findRandomPlace(List<Place> places, double lat, double lon) {
        int index = random.nextInt(places.size());
        return places.get(index);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                   + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                     * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
