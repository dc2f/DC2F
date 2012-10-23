package com.dc2f.contentrepository.nodetypedefinition;

import java.util.Arrays;

import com.dc2f.contentrepository.AttributeDefinition;
import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.exception.InvalidAttributeTypeException;
import com.dc2f.contentrepository.exception.UnknownAttributeException;

public class MapAttributeDefinition extends MapNode implements AttributesDefinition {
	public MapAttributeDefinition(KeyValuePair... entries) {
		super(entries);
	}

	public AttributeDefinition getAttributeDefinition(String propertyName) {
		Node attrDefinition = (Node) get(propertyName);
		if (attrDefinition == null) {
			throw new UnknownAttributeException("Unknown property {" + propertyName + "} for MapAttributeDefinnition. Valid Attributes: " + Arrays.toString(getAttributeNames()), null);
		}
		return new AttributeDefinitionImpl(attrDefinition);
	}

	public String[] getAttributeNames() {
		return internalMap.keySet().toArray(new String[internalMap.size()]);
	}
}
