package com.dc2f.backend.gwt.client.editor;

import java.util.ArrayList;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class JSONComparator {
	
	
	
	public static JSONValue compare(final JSONValue first, final  JSONValue second) {
		if (first instanceof JSONObject && second instanceof JSONObject) {
			return compareObjects((JSONObject) first, (JSONObject) second);
		}
		//TODO add ability to compare other JSON types
		return null;
	}
	
	public static JSONObject compareObjects(final JSONObject first, final JSONObject second) {
		JSONObject difference = new JSONObject();
		ArrayList<String> checkedAttributes = new ArrayList<String>();
		for (String attributeName : first.keySet()) {
			JSONValue firstAttribute = first.get(attributeName);
			JSONValue secondAttribute = second.get(attributeName);
			if (secondAttribute == null || !firstAttribute.equals(secondAttribute)) {
				difference.put(attributeName, first.get(attributeName));
			}
			checkedAttributes.add(attributeName);
		}
		
		
		return difference;
	}
}
