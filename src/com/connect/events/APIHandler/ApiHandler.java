package com.connect.events.APIHandler;

import static com.connect.events.APIHandler.AppConstants.API_KEY;
import static com.connect.events.APIHandler.AppConstants.LOCATION_KEY;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hw.serpapi.GoogleSearch;
import org.json.JSONObject;

public class ApiHandler {

    public static String[] fetchLocation() throws Exception {
        String apiUrl = "https://ipinfo.io/json?token=" + LOCATION_KEY;

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Error: Server returned response code " + responseCode);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            System.out.println("Error response: " + errorResponse.toString());
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject json = new JSONObject(response.toString());
        String city = json.getString("city");
        String country = json.getString("country");
        String loc = json.getString("loc");
        String pincode = json.getString("postal");

        return new String[]{pincode, city, country, loc};
    }


    public static Map<String, Map<String, String>> fetchMovies() throws Exception {
        Map<String, String> parameter = new HashMap<>();
        String[] location_array = fetchLocation();
        String city = location_array[1];
        String query = "Movies+in+" + city;
        parameter.put("q", query);
        parameter.put("location", city);
        parameter.put(GoogleSearch.API_KEY_NAME, API_KEY);

        // Perform the Google Search
        GoogleSearch search = new GoogleSearch(parameter);
        JsonObject rawJson = search.getJson();

        // Create a HashMap to store movie details
        Map<String, Map<String, String>> movieDetails = new HashMap<>();

        // Check if the response contains the key "movies_playing"
        if (rawJson.has("knowledge_graph")) {
            JsonObject knowledgeGraph = rawJson.getAsJsonObject("knowledge_graph");

            if (knowledgeGraph.has("movies_playing")) {
                JsonArray moviesPlaying = knowledgeGraph.getAsJsonArray("movies_playing");

                // Iterate through the movies and extract the required details
                for (int i = 0; i < moviesPlaying.size(); i++) {
                    JsonObject movie = moviesPlaying.get(i).getAsJsonObject();

                    String name = movie.has("name") ? movie.get("name").getAsString() : "";
                    String link = movie.has("link") ? movie.get("link").getAsString() : "";
                    String serpapiLink = movie.has("serpapi_link") ? movie.get("serpapi_link").getAsString() : "";
                    String image = movie.has("image") ? movie.get("image").getAsString() : "";

                    // Create a map to store individual movie details
                    Map<String, String> movieInfo = new HashMap<>();
                    movieInfo.put("link", link);
                    movieInfo.put("serpapi_link", serpapiLink);
                    movieInfo.put("image", image);

                    // Add movie info to the main HashMap with the movie name as the key
                    movieDetails.put(name, movieInfo);
                }
            }
        }

        return movieDetails;
    }

    public static Map<String, Map<String, String>> fetchEvents() throws Exception {
        Map<String, String> parameter = new HashMap<>();
        String[] location_array = fetchLocation();
        String city = location_array[1];
        String query = "Events+in+" + city;
        parameter.put("q", query);
        parameter.put("location", city);
        parameter.put(GoogleSearch.API_KEY_NAME, API_KEY);

        // Perform the Google Search
        GoogleSearch search = new GoogleSearch(parameter);
        JsonObject rawJson = search.getJson();

            // Create a HashMap to store event details
        Map<String, Map<String, String>> eventDetails = new HashMap<>();

        // Check if the response contains the key "events_results"
        if (rawJson.has("events_results")) {
            JsonArray eventsResults = rawJson.getAsJsonArray("events_results");

            // Iterate through the events and extract the required details
            for (int i = 0; i < eventsResults.size(); i++) {
                JsonObject event = eventsResults.get(i).getAsJsonObject();

                String title = event.has("title") && event.get("title").isJsonPrimitive() ? event.get("title").getAsString() : "";
                String address = event.has("address") && event.get("address").isJsonPrimitive() ? event.get("address").getAsString() : "";
                String link = event.has("link") && event.get("link").isJsonPrimitive() ? event.get("link").getAsString() : "";
                String thumbnail = event.has("thumbnail") && event.get("thumbnail").isJsonPrimitive() ? event.get("thumbnail").getAsString() : "";


                String startDate = "";
                String when = "";
                if (event.has("date") && event.get("date").isJsonObject()) {
                    JsonObject dateObject = event.getAsJsonObject("date");
                    startDate = dateObject.has("start_date") && dateObject.get("start_date").isJsonPrimitive()
                            ? dateObject.get("start_date").getAsString() : "";
                    when = dateObject.has("when") && dateObject.get("when").isJsonPrimitive()
                            ? dateObject.get("when").getAsString() : "";
                }

                // Create a map to store individual event details
                Map<String, String> eventInfo = new HashMap<>();
                eventInfo.put("start_date", startDate);
                eventInfo.put("when", when);
                eventInfo.put("address", address);
                eventInfo.put("link", link);
                eventInfo.put("thumbnail", thumbnail);

                // Add event info to the main HashMap with the event title as the key
                eventDetails.put(title, eventInfo);
            }
        }


        return eventDetails;
    }
    public static void main(String[] args) throws Exception {
        ApiHandler apiHandler = new ApiHandler();
        Map<String, Map<String, String>> movies = apiHandler.fetchMovies();
        Map<String, Map<String, String>> events = apiHandler.fetchEvents();

        System.out.println("Movies Data: " + movies);
        System.out.println("Events Data: " + events);

        // Write to text files
        try (FileWriter myWriter = new FileWriter("movies.txt");
             FileWriter myWriter2 = new FileWriter("events.txt")) {
            myWriter.write(movies.toString());
            myWriter2.write(events.toString());
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

