package com.dc2f.backend.gwt.client.editor;

import java.util.ArrayList;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * compare json objects.
 */
public final class JSONComparator {

	/**
	 * utility classes must not be instantiated.
	 */
	private JSONComparator() {
	}

	/**
	 * compares two json objects.
	 * 
	 * @param first first object to compare to
	 * @param second second object (duh!)
	 * @return the difference of the two objects, or null if either one of the two values were not valid json objects.
	 */
	public static JSONValue compare(final JSONValue first, final JSONValue second) {
		if (first instanceof JSONObject && second instanceof JSONObject) {
			return compareObjects((JSONObject) first, (JSONObject) second);
		}
		// TODO add ability to compare other JSON types
		return null;
	}

	/**
	 * compares two json objects.
	 * 
	 * @param first first object to compare to
	 * @param second second object (duh!)
	 * @return the difference of the two objects
	 */
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
