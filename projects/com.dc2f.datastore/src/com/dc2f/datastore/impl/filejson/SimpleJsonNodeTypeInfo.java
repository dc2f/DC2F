package com.dc2f.datastore.impl.filejson;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.NodeType;
import com.dc2f.datastore.NodeTypeInfo;
import com.dc2f.datastore.exception.ClassNotFoundForNodeTypeException;
import com.dc2f.datastore.nodetypedefinition.NodeTypeDefinition;

public class SimpleJsonNodeTypeInfo extends SimpleJsonNode implements NodeTypeInfo {
	private static final Logger logger = Logger.getLogger(SimpleJsonNodeTypeInfo.class.getName());

	private JSONObject jsonObject;

	private NodeType parentNodeType;

	public SimpleJsonNodeTypeInfo(ContentRepository repository, NodeType parentNodeType, String path, JSONObject jsonObject) {
		super(repository, path, jsonObject, parentNodeType != null ? parentNodeType : new NodeTypeDefinition());
		this.parentNodeType = parentNodeType != null ? parentNodeType : new NodeTypeDefinition();
		this.jsonObject = jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends NodeType> Class<T> getNodeTypeClass() {
		String className = (String) get("class");
		if (className != null) {
			try {
				return (Class<T>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				logger.log(Level.SEVERE, "Unable to find class for node type {" + this + "}", e);
				throw new ClassNotFoundForNodeTypeException("Unable to find class for node type {" + this + "}", e);
			}
		} else {
			logger.finer("Requested class for node type without defined class: {" + this + "}");
			return (Class<T>) NodeType.DEFAULT_NODETYPE;
		}
	}

	@Override
	protected Object internalGet(String propertyName) {
		try {
			return jsonObject.get(propertyName);
		} catch (JSONException e) {
			if (jsonObject.has(propertyName)) {
				logger.log(Level.SEVERE, "Error while getting property {" + propertyName
						+ "} of node type {" + getPath() + "}", e);
			}
			if (parentNodeType != null && parentNodeType.getNodeTypeInfo() instanceof SimpleJsonNodeTypeInfo) {
				return ((SimpleJsonNodeTypeInfo)parentNodeType.getNodeTypeInfo()).internalGet(propertyName);
			}
			return null;
		}
	}

	
//	@Override
//	public String toString() {
//		return "{SimpleJsonNodeTypeInfo:" + jsonObject.toString() + "}";
//	}

	@Override
	public NodeType getParentNodeType() {
		return parentNodeType;
	}

	@Override
	public String[] getAttributeNames() {
		SimpleJsonNode attributes = (SimpleJsonNode) get("attributes");
		if (attributes != null) {
			return JSONObject.getNames(attributes.getJsonObject());
		}
		return null;
	}
}
