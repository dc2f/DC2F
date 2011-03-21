package com.dc2f.datastore;

import java.util.logging.Logger;

import com.dc2f.datastore.nodetypedefinition.KeyValuePair;
import com.dc2f.datastore.nodetypedefinition.MapNode;


public abstract class BaseNodeType implements NodeType {
	private static final Logger logger = Logger.getLogger(BaseNodeType.class.getName());

	private NodeTypeInfo nodeTypeInfo;

	public void init(NodeTypeInfo nodeTypeInfo) {
		this.nodeTypeInfo = nodeTypeInfo;
	}
	
	public NodeTypeInfo getNodeTypeInfo() {
		return nodeTypeInfo;
	}
	
	@Override
	public Node getAttributeDefinitions() {
		if (nodeTypeInfo == null) {
			logger.severe("node type was not initialized! {" + this.getClass().getName() + "}");
		}
		Boolean b = (Boolean) nodeTypeInfo.getProperty("freeattributes");
		if (b != null && b.booleanValue()) {
			final String valueType = (String)nodeTypeInfo.getProperty("valuetype");
			final String valueNodeType = (String) nodeTypeInfo.getProperty("valuenodetype");
			return new Node() {
				
				@Override
				public Object getProperty(String propertyName) {
					return new MapNode(new KeyValuePair("type", valueType), new KeyValuePair("typeofnode", valueNodeType));
				}
				
				@Override
				public String getPath() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public NodeType getNodeType() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return null;
				}
			};
		}
		Node attrDefinitions = (Node) nodeTypeInfo.getProperty("attributes");
		if (attrDefinitions == null) {
			logger.severe("Node has no attribute defintions {" + this
					+ "} (class:" + this.getClass().getName() + "}");
		}
		return attrDefinitions;
	}
}
