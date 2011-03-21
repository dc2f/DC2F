package com.dc2f.datastore.nodetypedefinition;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.datastore.Node;

public class NodeTypeDefinition extends BaseNodeType {

	@Override
	public Node getAttributeDefinitions() {
		return new MapNode(
				new KeyValuePair(
						"attributes", new MapNode(
								new KeyValuePair("type", "Node"),
								new KeyValuePair("typeofnode", "classpath:com/dc2f/datastore/nodetypedefinition/AttributesNodeType")
								/*,
								new KeyValuePair("typeofnode", "classpath:com/dc2f/datastore/DefaultNodeType")*/) ),
				new KeyValuePair("class", new MapNode(new KeyValuePair("type", "String"))),
				new KeyValuePair("freeattributes", new MapNode(new KeyValuePair("type", "Boolean"))),
				new KeyValuePair("valuetype", new MapNode(new KeyValuePair("type", "String"))),
				new KeyValuePair("valuenodetype", new MapNode(new KeyValuePair("type", "String"))));
	}

}
