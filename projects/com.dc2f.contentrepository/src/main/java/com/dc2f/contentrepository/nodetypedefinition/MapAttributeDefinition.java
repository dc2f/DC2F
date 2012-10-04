package com.dc2f.contentrepository.nodetypedefinition;

import com.dc2f.contentrepository.AttributeDefinition;
import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.Node;

public class MapAttributeDefinition extends MapNode implements AttributesDefinition {
	public MapAttributeDefinition(KeyValuePair... entries) {
		super(entries);
	}

	public AttributeDefinition getAttributeDefinition(String propertyName) {
		return new AttributeDefinitionImpl((Node) get(propertyName));
	}

	public String[] getAttributeNames() {
		return internalMap.keySet().toArray(new String[internalMap.size()]);
	}
}
