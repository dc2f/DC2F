package com.dc2f.datastore.impl.filejson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.DefaultNodeType;
import com.dc2f.datastore.Node;
import com.dc2f.datastore.NodeType;

public class SimpleJsonNode implements Node {
	private static final Logger logger = Logger.getLogger(SimpleJsonNode.class.getName());

	private ContentRepository repository;
	private String path;
	private JSONObject jsonObject;
	private NodeType nodeType;

	public SimpleJsonNode(ContentRepository repository, String path, JSONObject jsonObject, NodeType nodeType) {
		this.repository = repository;
		this.path = path;
		this.jsonObject = jsonObject;
		this.nodeType = nodeType;
	}

	@Override
	public String getName() {
		return new File(path).getName();
	}

	@Override
	public NodeType getNodeType() {
		return nodeType;
	}

	@Override
	public String getPath() {
		return path;
	}

	/*
	@Override
	public Object getProperty(String propertyName) {
		Object obj;
		try {
			obj = jsonObject.get(propertyName);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error while getting property {" + propertyName
					+ "} of node {" + path + "}", e);
			return null;
		}
		if (obj instanceof String) {
			return obj;
		}
		logger.severe("FIXME: Not Implemented: Unable to convert property {" + path + "} of node type {" + getName() + "}: {" + obj.getClass().getName() + "}");
		return null;
	}
	*/
	
	protected Object internalGetProperty(String propertyName) {
		try {
			return jsonObject.get(propertyName);
		} catch (JSONException e) {
			if (jsonObject.has(propertyName)) {
				logger.log(Level.SEVERE, "Error while getting property {" + propertyName
						+ "} of node type {" + path + "}", e);
			}
			return null;
		}
	}
	
	@Override
	public Object getProperty(String propertyName) {
		Object obj = internalGetProperty(propertyName);
		if (obj == null) {
			// FIXME check if property is required?!
			return null;
		}
		if (obj instanceof String) {
			return obj;
		}
		
		Node attrDefinitions = getNodeType().getAttributeDefinitions();
		logger.info("Getting property {" + propertyName + "}: " + obj + " - attrDefinitions: " + attrDefinitions);
		Node attrDefinition = (Node) attrDefinitions.getProperty(propertyName);
		String attributeType = (String) attrDefinition.getProperty("type");
		if ("Node".equals(attributeType)) {
			String typeofnode = (String) attrDefinition.getProperty("typeofnode");
			NodeType subNodeType = null;
			if (typeofnode instanceof String) {
				subNodeType = repository.getNodeType(typeofnode);
			}
			if (subNodeType == null) {
				subNodeType = new DefaultNodeType();
			}
			return new SimpleJsonNode(repository, path, (JSONObject) obj, subNodeType);
		} else if ("ListOfNodes".equals(attributeType)) {
			JSONArray array = (JSONArray) obj;
			String typeofsubnodes = (String) attrDefinition.getProperty("typeofsubnodes");
			NodeType subNodeType = repository.getNodeType(typeofsubnodes);
			List<Node> ret = new ArrayList<Node>();
			for (int i = 0 ; i < array.length() ; i++) {
				try {
					JSONObject arrayobj = (JSONObject) array.get(i);
					ret.add(new SimpleJsonNode(repository, path, arrayobj, subNodeType));
				} catch (JSONException e) {
					logger.log(Level.SEVERE, "Error while converting property into ListOfNodes", e);
				}
				
			}
			return ret;
		} else if ("Boolean".equals(attributeType)) {
			if (obj instanceof Boolean) {
				return (Boolean) obj;
			}
			return null;
		}
		logger.info("getting property " + propertyName + " --- attrDefinitions: " + attrDefinitions + " attrDefinition: " + attrDefinition);
		logger.severe("FIXME: Not Implemented: Unable to convert property {" + propertyName + "} of node type {" + getName() + "}: {" + obj.getClass().getName() + "}");
		return null;
	}
	
	@Override
	public String toString() {
		return "{SimpleJsonNode:" + jsonObject.toString() + "}";
	}

}
