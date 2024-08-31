package org.example;

import java.util.ArrayList;
import java.util.List;

public class RoutePlanner {

    public String planRoute(List<Place> places, double startLat, double startLon, double minDistance, double maxDistance) {
        List<Place> route = new ArrayList<>();
        double currentLat = startLat;
        double currentLon = startLon;
        double totalDistance = 0;
        StringBuilder routeDetails = new StringBuilder();

        while (!places.isEmpty() && totalDistance < maxDistance) {
            Place nearestPlace = findNearestPlace(places, currentLat, currentLon);
            double distanceToPlace = calculateDistance(currentLat, currentLon, nearestPlace.getLat(), nearestPlace.getLon());

            if (totalDistance + distanceToPlace <= maxDistance) {
                route.add(nearestPlace);
                totalDistance += distanceToPlace;
                routeDetails.append(nearestPlace.toString()).append(" - ").append(distanceToPlace).append(" km - ");
                currentLat = nearestPlace.getLat();
                currentLon = nearestPlace.getLon();
                places.remove(nearestPlace);
            } else {
                break;
            }
        }


        double returnDistance = calculateDistance(currentLat, currentLon, startLat, startLon);
        totalDistance += returnDistance;
        routeDetails.append("Return - ").append(returnDistance).append(" km\n");
        routeDetails.append("Total distance: ").append(totalDistance).append(" km");

        return routeDetails.toString();
    }

    private Place findNearestPlace(List<Place> places, double lat, double lon) {
        Place nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Place place : places) {
            double distance = calculateDistance(lat, lon, place.getLat(), place.getLon());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = place;
            }
        }

        return nearest;
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
