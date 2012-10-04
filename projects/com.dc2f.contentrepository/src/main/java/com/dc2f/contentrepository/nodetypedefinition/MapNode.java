package com.dc2f.contentrepository.nodetypedefinition;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;

public class MapNode implements Node {
	protected Map<String, Object> internalMap;
	
	public MapNode(KeyValuePair... entries) {
		internalMap = new LinkedHashMap<String, Object>(entries.length);
		for (KeyValuePair entry : entries) {
			internalMap.put(entry.getKey(), entry.getValue());
		}
	}

	public String getName() {
		return null;
	}

	public NodeType getNodeType() {
		return null;
	}

	public String getPath() {
		return null;
	}

	public Object get(String attributeName) {
		return internalMap.get(attributeName);
	}
	
	@Override
	public String toString() {
		return "{MapNode:" + internalMap.toString() + "}";
	}
}
