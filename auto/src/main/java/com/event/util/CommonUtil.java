package com.event.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.event.model.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	
	public static List<Event> assembleJsonToEvents(JsonArray places) throws Exception {
		List<Event> result = new ArrayList<>();
		for (int i = 0; i < places.size(); i++) {
			Event evnt = new Event();
			JsonObject place = places.get(i).getAsJsonObject();
			evnt.setname(place.has("position") ? place.get("position").toString().replace("\"", "") : "");
			result.add(evnt);
		}
		return result;
	}

}
