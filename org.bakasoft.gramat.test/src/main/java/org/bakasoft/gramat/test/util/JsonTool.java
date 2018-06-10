package org.bakasoft.gramat.test.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class JsonTool {

	public static Map<String, Object> parse(String json) {
		JsonElement root = new JsonParser().parse(json);
		
		if (!root.isJsonObject()) {
			throw new RuntimeException();
		}

		return convertToMap(root.getAsJsonObject());
	}

	private static LinkedHashMap<String, Object> convertToMap(JsonObject obj) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		
		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			String key = entry.getKey();
			JsonElement element = entry.getValue();
			
			map.put(key, convertToValue(element));
		}
		
		return map;
	}

	private static Object convertToValue(JsonElement element) {
		if (element == null || element.isJsonNull()) {
			return null;
		}
		else if (element.isJsonObject()) {
			return convertToMap(element.getAsJsonObject());
		} 
		else if (element.isJsonArray()) {
			return convertToList(element.getAsJsonArray());
		} 
		else if (element.isJsonPrimitive()) {
			return convertToPrimitive(element.getAsJsonPrimitive());
		} 
		else {
			throw new UnsupportedOperationException();
		}
	}

	private static ArrayList<Object> convertToList(JsonArray array) {
		ArrayList<Object> list = new ArrayList<>();
		
		for (int i = 0; i < array.size(); i++) {
			JsonElement element = array.get(i);
			
			list.add(convertToValue(element));
		}
		
		return list;
	}
	
	private static Object convertToPrimitive(JsonPrimitive prim) {
		if (prim.isString()) {
			return prim.getAsString();
		} 
		else if (prim.isBoolean()) {
			return prim.getAsBoolean();
		} 
		else if (prim.isNumber()) {
			return prim.getAsBigDecimal();
		}
		else {
			throw new UnsupportedOperationException();
		}
	}
	
}
