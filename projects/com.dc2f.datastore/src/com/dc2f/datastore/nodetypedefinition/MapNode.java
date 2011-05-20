package com.dc2f.datastore.nodetypedefinition;

import java.util.HashMap;
import java.util.Map;

import com.dc2f.datastore.Node;
import com.dc2f.datastore.NodeType;

public class MapNode implements Node {
	protected Map<String, Object> internalMap;
	
	public MapNode(KeyValuePair... entries) {
		internalMap = new HashMap<String, Object>(entries.length);
		for (KeyValuePair entry : entries) {
			internalMap.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public NodeType getNodeType() {
		return null;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public Object get(String propertyName) {
		return internalMap.get(propertyName);
	}
	
	@Override
	public String toString() {
		return "{MapNode:" + internalMap.toString() + "}";
	}
}
