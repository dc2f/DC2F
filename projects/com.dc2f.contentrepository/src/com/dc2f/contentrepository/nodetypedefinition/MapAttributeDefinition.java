package com.dc2f.contentrepository.nodetypedefinition;

import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.Node;

public class MapAttributeDefinition extends MapNode implements AttributesDefinition {
	public MapAttributeDefinition(KeyValuePair... entries) {
		super(entries);
	}

	@Override
	public Node getAttributeDefinition(String propertyName) {
		return (Node) get(propertyName);
	}

	@Override
	public String[] getAttributeNames() {
		return internalMap.keySet().toArray(new String[internalMap.size()]);
	}
}
