package com.dc2f.contentrepository.nodetypedefinition;

import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.BaseNodeType;

/**
 * a static NodeType which represents an actual NodeType definition. (Since we can't define the
 * structure of a NodeType - Node using a Node.. we have to hardcode it..)
 * 
 * @author herbert
 */
public class NodeTypeDefinition extends BaseNodeType {

	@Override
	public AttributesDefinition getAttributeDefinitions() {
		return new MapAttributeDefinition(
				new KeyValuePair("_comment", new MapNode(new KeyValuePair("type", "String"))),
				new KeyValuePair(
						"attributes",
						new MapNode(
								new KeyValuePair("type", "Node"),
								new KeyValuePair("typeofnode",
										"classpath:com/dc2f/contentrepository/nodetypedefinition/AttributesNodeType")
						)),
				new KeyValuePair("class", new MapNode(new KeyValuePair("type", "String"))),
				new KeyValuePair("freeattributes", new MapNode(new KeyValuePair("type", "Boolean"))),
				new KeyValuePair("valuetype", new MapNode(new KeyValuePair("type", "String"))),
				new KeyValuePair("valuenodetype", new MapNode(new KeyValuePair("type", "String"))));
	}

}
