package com.connect.events.APIHandler;

import com.google.gson.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
	
	public static void createFileAndAddText(String text) throws Exception {
		Path source = Path.of(AppConstants.PATH+AppConstants.EXT);
		Files.deleteIfExists(Paths.get(AppConstants.PATH+AppConstants.EXT));
		Files.createFile(source);
		addTextToFile(text);
	}
	
	public static void addTextToFile(String text) throws Exception {
		Files.write(Paths.get(AppConstants.PATH+AppConstants.EXT), (text+ "\n").getBytes() , StandardOpenOption.APPEND);
	}
	
	public static String getPrettyJson(String text) throws Exception {
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
         JsonParser jp = new JsonParser();
         JsonElement je = jp.parse(text);
         return gson.toJson(je);
	}

	public static List<Event> assembleMoviesToEvents(JsonArray places) throws Exception {
		List<Event> result = new ArrayList<>();
		for (int i = 0; i < places.size(); i++) {
			Event evnt = new Event();
			JsonObject place = places.get(i).getAsJsonObject();
			evnt.setName(place.has("name") ? place.get("name").toString().replace("\"", "") : "");
			evnt.setLink(place.has("link") ? place.get("link").toString().replace("\"", "") : "");
			evnt.setSerpapi_link(place.has("serpapi_link") ? place.get("serpapi_link").toString().replace("\"", "") : "");
			evnt.setImage(place.has("image") ? place.get("image").toString().replace("\"", "") : "");
			result.add(evnt);
		}
		return result;
	}
}
