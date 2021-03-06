package com.dc2f.contentrepository.filejson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.NodeTypeInfo;
import com.dc2f.contentrepository.exception.ClassNotFoundForNodeTypeException;
import com.dc2f.contentrepository.nodetypedefinition.NodeTypeDefinition;

public class SimpleJsonNodeTypeInfo extends SimpleJsonNode implements NodeTypeInfo {
	private static final Logger logger = Logger.getLogger(SimpleJsonNodeTypeInfo.class.getName());

	private JSONObject jsonObject;

	private NodeType parentNodeType;

	public SimpleJsonNodeTypeInfo(SimpleBranchAccess simpleBranchAccess, NodeType parentNodeType, String path, JSONObject jsonObject) {
		super(simpleBranchAccess, path, jsonObject, parentNodeType != null ? parentNodeType : new NodeTypeDefinition());
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
		List<String> attributeNames = new ArrayList<String>();
		if (attributes != null) {
			String[] names = JSONObject.getNames(attributes.getJsonObject());
			if (names != null) {
				attributeNames.addAll(Arrays.asList(names));
			}
		}
		if (getParentNodeType() != null) {
			attributeNames.addAll(Arrays.asList(getParentNodeType().getAttributeDefinitions().getAttributeNames()));
		}
		return attributeNames.toArray(new String[attributeNames.size()]);
	}
}
