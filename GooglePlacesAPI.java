package Rent_Rover;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GooglePlacesAPI {

    private static final String API_KEY = "AIzaSyDT3s6SpAPFBAt48Ve03YjUIhmdr9NGd5E"; // put your key in quotes

    public static List<String> getSuggestions(String input) {
        List<String> suggestions = new ArrayList<>();
        try {
            String encodedInput = URLEncoder.encode(input, "UTF-8");
            String urlStr = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" 
                            + encodedInput + "&key=" + API_KEY;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            JSONObject json = new JSONObject(response.toString());

            // Check if API returned error
            if (!json.getString("status").equals("OK")) return suggestions;

            JSONArray predictions = json.getJSONArray("predictions");
            for (int i = 0; i < predictions.length(); i++) {
                suggestions.add(predictions.getJSONObject(i).getString("description"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return suggestions;
    }
}
