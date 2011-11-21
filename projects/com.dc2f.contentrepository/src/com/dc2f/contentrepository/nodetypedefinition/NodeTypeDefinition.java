package com.dc2f.contentrepository.nodetypedefinition;

import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.BaseNodeType;

public class NodeTypeDefinition extends BaseNodeType {

	@Override
	public AttributesDefinition getAttributeDefinitions() {
		return new MapAttributeDefinition(
				new KeyValuePair("_comment", new MapNode(new KeyValuePair("type", "String"))),
				new KeyValuePair(
						"attributes", new MapNode(
								new KeyValuePair("type", "Node"),
								new KeyValuePair("typeofnode", "classpath:com/dc2f/contentrepository/nodetypedefinition/AttributesNodeType")
								/*,
								new KeyValuePair("typeofnode", "classpath:com/dc2f/contentrepository/DefaultNodeType")*/) ),
				new KeyValuePair("class", new MapNode(new KeyValuePair("type", "String"))),
				new KeyValuePair("freeattributes", new MapNode(new KeyValuePair("type", "Boolean"))),
				new KeyValuePair("valuetype", new MapNode(new KeyValuePair("type", "String"))),
				new KeyValuePair("valuenodetype", new MapNode(new KeyValuePair("type", "String"))));
	}
	
}
