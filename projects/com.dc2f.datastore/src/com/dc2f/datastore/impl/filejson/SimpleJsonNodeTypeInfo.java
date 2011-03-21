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

import com.dc2f.datastore.NodeType;
import com.dc2f.datastore.NodeTypeInfo;

public class SimpleJsonNodeTypeInfo implements NodeTypeInfo {
	private static final Logger logger = Logger.getLogger(SimpleJsonNodeTypeInfo.class.getName());

	private String path;
	private JSONObject jsonObject;

	private NodeType parentNodeType;

	public SimpleJsonNodeTypeInfo(NodeType parentNodeType, String path, JSONObject jsonObject) {
		this.parentNodeType = parentNodeType;
		this.path = path;
		this.jsonObject = jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends NodeType> Class<T> getNodeTypeClass() {
		String className = (String) getProperty("class");
		if (className != null) {
			try {
				return (Class<T>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				logger.log(Level.SEVERE, "Unable to find class for node type {" + this + "}", e);
			}
		} else {
			logger.finer("Requested class for node type without defined class: {" + this + "}");
			return (Class<T>) NodeType.DEFAULT_NODETYPE;
		}
		return null;
	}

	@Override
	public String getName() {
		return new File(path).getName();
	}

	@Override
	public Object getProperty(String propertyName) {
		Object obj;
		try {
			obj = jsonObject.get(propertyName);
		} catch (JSONException e) {
			if (jsonObject.has(propertyName)) {
				logger.log(Level.SEVERE, "Error while getting property {" + propertyName
						+ "} of node type {" + path + "}", e);
			}
			if (parentNodeType != null) {
				return parentNodeType.getNodeTypeInfo().getProperty(propertyName);
			}
			return null;
		}
		if (obj instanceof String) {
			return obj;
		}
		if ("renderconfiguration".equals(propertyName)) {
			logger.severe("FIXME: UUUUUGGGGLLLYYYY - special case for renderconfiguration - we need a way to define the data structure in the node type so it can be parsed correctly (?)");
			JSONArray array = (JSONArray) obj;
			List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
			for (int i = 0 ; i < array.length() ; i++) {
				try {
					JSONObject renderconfig = (JSONObject) array.get(i);
					Map<String, Object> retrender = new HashMap<String, Object>();
					for (Iterator iterator = renderconfig.keys() ; iterator.hasNext() ; ) {
						String key = (String) iterator.next();
						retrender.put(key, renderconfig.get(key));
					}
					ret.add(retrender);
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
				
			}
			return ret;
		}
		logger.severe("FIXME: Not Implemented: Unable to convert property {" + path + "} of node type {" + getName() + "}: {" + obj.getClass().getName() + "}");
		return null;
	}
	
	@Override
	public String toString() {
		return "{SimpleJsonNodeTypeInfo:" + jsonObject.toString() + "}";
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return path;
	}
}
