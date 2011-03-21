package com.dc2f.datastore.impl.filejson;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.datastore.Node;
import com.dc2f.datastore.NodeType;

public class SimpleJsonNode implements Node {
	private static final Logger logger = Logger.getLogger(SimpleJsonNode.class.getName());

	private String path;
	private JSONObject jsonObject;
	private NodeType nodeType;

	public SimpleJsonNode(String path, JSONObject jsonObject, NodeType nodeType) {
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
	
	@Override
	public String toString() {
		return "{SimpleJsonNode:" + jsonObject.toString() + "}";
	}

}
