package com.event.controller;

import static com.event.util.AppConstants.API_KEY;
import static com.event.util.AppConstants.LOCATION;
import static com.event.util.AppConstants.TYPE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.event.model.Event;
import com.event.model.EventResponse;
import com.event.util.CommonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hw.serpapi.GoogleSearch;
import com.hw.serpapi.SerpApiSearchException;


@Controller
public class EventWebController {
	
	@GetMapping({ "/home", "/" })
	public ModelAndView deleteAll() throws Exception {
		ModelAndView mav = new ModelAndView("home");
		// Parameters
		Map<String, String> parameter = new HashMap<>();
		parameter.put("q", TYPE);
		parameter.put("location", LOCATION);
		parameter.put(GoogleSearch.API_KEY_NAME, API_KEY);

		// Create search
		GoogleSearch search = new GoogleSearch(parameter);

		try {
			// Execute search
			JsonObject rawJson = search.getJson();
			JsonObject results = (JsonObject) rawJson.get("local_results");
			JsonArray places = (JsonArray) results.get("places");
			List<Event> events = CommonUtil.assembleJsonToEvents(places);
			
			//Set list of events for UI
			EventResponse eventResponse = new EventResponse();
			eventResponse.setEvents(events);
			mav.addObject("EVENTRESPONSE", eventResponse);
			
			// Backup Json
			String prettyJson = CommonUtil.getPrettyJson(rawJson.toString());
			CommonUtil.createFileAndAddText(prettyJson);
		} catch (SerpApiSearchException e) {
			System.out.println("oops exception detected!");
			e.printStackTrace();
		}

		
		return mav;
	}
	
	
	public static void main(String[] args) throws Exception {

		// parameters
		Map<String, String> parameter = new HashMap<>();
		parameter.put("q", TYPE);
		parameter.put("location", LOCATION);
		parameter.put(GoogleSearch.API_KEY_NAME, API_KEY);

		// Create search
		GoogleSearch search = new GoogleSearch(parameter);

		try {
			// Execute search
			JsonObject rawJson = search.getJson();
			JsonObject results = (JsonObject) rawJson.get("local_results");
			JsonArray places = (JsonArray) results.get("places");
			List<Event> events = CommonUtil.assembleJsonToEvents(places);

			// Backup Json
			String prettyJson = CommonUtil.getPrettyJson(rawJson.toString());
			CommonUtil.createFileAndAddText(prettyJson);
		} catch (SerpApiSearchException e) {
			System.out.println("oops exception detected!");
			e.printStackTrace();
		}
	}
	
	

}