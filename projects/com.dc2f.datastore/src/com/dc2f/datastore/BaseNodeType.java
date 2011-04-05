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
			logger.severe("node type was not initialized! {" + this.getClass().getName() + "} nodeTypeInfo: {" + nodeTypeInfo + "}");
		}
		final Node attrDefinitions = (Node) nodeTypeInfo.getProperty("attributes");
		final Boolean freeattributes = (Boolean) nodeTypeInfo.getProperty("freeattributes");
		final String valueType = (String)nodeTypeInfo.getProperty("valuetype");
		final String valueNodeType = (String) nodeTypeInfo.getProperty("valuenodetype");
		
		
		if (attrDefinitions == null) {
			logger.severe("Node has no attribute defintions {" + this
					+ "} (class:" + this.getClass().getName() + "} nodeTypeInfo: {" + nodeTypeInfo + "}");
		}
		
		Node tmpParentAttrDefinitions = null;
		if (nodeTypeInfo.getParentNodeType() != null) {
			NodeType parent = nodeTypeInfo.getParentNodeType();
			tmpParentAttrDefinitions = parent.getAttributeDefinitions();
		}
		final Node parentAttrDefinitions = tmpParentAttrDefinitions;


		return new Node() {

				@Override
				public Object getProperty(String propertyName) {
					if (freeattributes != null && freeattributes.booleanValue()) {
						return new MapNode(new KeyValuePair("type", valueType), new KeyValuePair("typeofnode", valueNodeType));
					}
					Object def = null;
					if (attrDefinitions != null) {
						def = attrDefinitions.getProperty(propertyName);
					}
					if (def == null && parentAttrDefinitions != null) {
						return parentAttrDefinitions.getProperty(propertyName);
					}
					return def;
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
				
				@Override
				public String toString() {
					return "{(" + BaseNodeType.this + ")" + String.valueOf(attrDefinitions) + " parent:(" + nodeTypeInfo.getParentNodeType() + ")" + String.valueOf(parentAttrDefinitions) + "}";
				}
			};
	}
	
	@Override
	public String toString() {
		return "{NodeType:"+getNodeTypeInfo()+"}";
	}
}
