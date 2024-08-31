package org.example;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OverpassAPIClient {
    private static final String OVERPASS_API_URL = "http://overpass-api.de/api/interpreter";

    public List<Place> getPlaces(double lat, double lon, String category) throws IOException {
        String query = String.format("[out:json];node[\"tourism\"=\"%s\"](around:5000,%f,%f);out;", category, lat, lon);
        String url = OVERPASS_API_URL + "?data=" + URLEncoder.encode(query, StandardCharsets.UTF_8);

        List<Place> places = new ArrayList<>();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            String response = EntityUtils.toString(client.execute(request).getEntity());
            JSONObject jsonObject = new JSONObject(response);
            JSONArray elements = jsonObject.getJSONArray("elements");

            for (int i = 0; i < elements.length(); i++) {
                JSONObject element = elements.getJSONObject(i);
                double placeLat = element.getDouble("lat");
                double placeLon = element.getDouble("lon");
                String name = element.optString("tags").isEmpty() ? "Unknown" : element.getJSONObject("tags").optString("name", "Unknown");
                places.add(new Place(name, placeLat, placeLon));
            }
        }

        return places;
    }
}
