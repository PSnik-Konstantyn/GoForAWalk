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
import java.util.Map;

public class OverpassAPIClient {
    private static final String OVERPASS_API_URL = "https://overpass-api.de/api/interpreter";

    public List<Place> getPlaces(double lat, double lon, Map<String, List<String>> categories) throws IOException {
        List<Place> places = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
            String type = entry.getKey();
            List<String> subcategories = entry.getValue();

            for (String category : subcategories) {
                // Формуємо запит для кожної підкатегорії
                String query = String.format("[out:json];node[\"%s\"=\"%s\"](around:5000,%f,%f);out;", type, category, lat, lon);
                String url = OVERPASS_API_URL + "?data=" + URLEncoder.encode(query, StandardCharsets.UTF_8);

                try (CloseableHttpClient client = HttpClients.createDefault()) {
                    HttpGet request = new HttpGet(url);
                    String response = EntityUtils.toString(client.execute(request).getEntity());
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray elements = jsonObject.getJSONArray("elements");

                    // Обробляємо елементи, які повертає Overpass API
                    for (int i = 0; i < elements.length(); i++) {
                        JSONObject element = elements.getJSONObject(i);
                        double placeLat = element.getDouble("lat");
                        double placeLon = element.getDouble("lon");
                        String name = element.optJSONObject("tags") != null ? element.getJSONObject("tags").optString("name", "Невідомо") : "Невідомо";
                        places.add(new Place(name, placeLat, placeLon, category));
                    }
                }
            }
        }

        return places;
    }
}
