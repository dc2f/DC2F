package com.dc2f.datastore.nodetypedefinition;

import com.dc2f.datastore.AttributesDefinition;
import com.dc2f.datastore.Node;

public class MapAttributeDefinition extends MapNode implements AttributesDefinition {
	public MapAttributeDefinition(KeyValuePair... entries) {
		super(entries);
	}

	@Override
	public Node getAttributeDefinition(String propertyName) {
		return (Node) getProperty(propertyName);
	}

	@Override
	public String[] getAttributeNames() {
		return internalMap.keySet().toArray(new String[internalMap.size()]);
	}
}
