package org.example;

public class Place {
    private String name;
    private double lat;
    private double lon;
    private String category;

    public Place(String name, double lat, double lon, String category) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name + " (" + lat + ", " + lon + ") - Category: " + category;
    }
}
