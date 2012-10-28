package com.dc2f.contentrepository.filejson;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.contentrepository.AttributeDefinition;
import com.dc2f.contentrepository.AttributeType;
import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.ChangeableNode;
import com.dc2f.contentrepository.DefaultNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.exception.UnknownAttributeException;

public class SimpleJsonNode implements Node {
	private static final Logger logger = Logger.getLogger(SimpleJsonNode.class.getName());

	private SimpleBranchAccess branchAccess;
	private String path;
	private JSONObject jsonObject;
	private NodeType nodeType;

	public SimpleJsonNode(SimpleBranchAccess branchAccess, String path, JSONObject jsonObject, NodeType nodeType) {
		// "normalize" path :)
		if (path != null && path.endsWith("/") && !"/".equals(path)) {
			path = path.replaceAll("/+$", "");
		}
		this.branchAccess = branchAccess;
		this.path = path;
		this.jsonObject = jsonObject;
		this.nodeType = nodeType;
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
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
	
	protected Object internalGet(String attributeName) {
		try {
			return jsonObject.get(attributeName);
		} catch (JSONException e) {
			if (jsonObject.has(attributeName)) {
				logger.log(Level.SEVERE, "Error while getting attribute {" + attributeName
						+ "} of node type {" + path + "}", e);
			}
			return null;
		}
	}
	
	
	
	@Override
	public Object get(String attributeName) {
		Object obj = internalGet(attributeName);
		
		// FIXME do this the "clean way"
		if (obj instanceof String && ("class".equals(attributeName) || "type".equals(attributeName))) {
		//if (obj instanceof String && ("class".equals(propertyName) )) {
			return obj;
		}
		
		AttributesDefinition attrDefinitions = getNodeType().getAttributeDefinitions();
		logger.finest(this.getName() + ": Getting attribute {" + attributeName + "}: " + obj + " - attrDefinitions: " + attrDefinitions);
		AttributeDefinition attrDefinition = attrDefinitions.getAttributeDefinition(attributeName);
		if (attrDefinition == null) {
			throw new UnknownAttributeException("Unknown attribute {" + attributeName + "} for {" + getNodeType() + "}", null);
		}
		AttributeType attributeType = attrDefinition.getAttributeType();
		
		if (attributeType == AttributeType.NODE_REFERENCE && obj instanceof String) {
			String ref = (String) obj;
			logger.fine("Trying to resolve object {" + ref + "}");
			if (!ref.startsWith("/")) {
				try {
					ref = new URI(getPath()).resolve(ref).toString();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return branchAccess.getNode(ref);
		} else if (attributeType == AttributeType.NODETYPE_REFERENCE && obj instanceof String) {
			return branchAccess.getNodeType((String) obj);
		} else if (attributeType == AttributeType.CLOB && obj == null) {
			obj = branchAccess.getContentRepository().loadRepositoryFile(new File(path, attributeName + ".clob.property"));
		} else if (attributeType == AttributeType.BLOB && obj == null) {
			return branchAccess.getContentRepository().getInputStreamForRepositoryFile(new File(path, attributeName + ".blob.property"));
		}

		if (obj instanceof String) {
			return obj;
		}
		if (obj == null) {
			// FIXME check if property is required?!
			return null;
		}


		if (attributeType == AttributeType.NODE || (obj instanceof JSONObject && attributeType == AttributeType.NODE_REFERENCE)) {

			String subNodeTypeName = ((JSONObject)obj).optString("nodetype", null);
			NodeType currentSubNodeType = null;
			if (subNodeTypeName != null) {
				currentSubNodeType = branchAccess.getNodeType(subNodeTypeName);
			}
			
			if (currentSubNodeType == null) {
				String typeofnode = (String) attrDefinition.get("typeofnode");
				if (typeofnode != null) {
					currentSubNodeType = branchAccess.getNodeType(typeofnode);
				}
			}
			if (currentSubNodeType == null) {
				currentSubNodeType = new DefaultNodeType();
			}
			return new SimpleJsonNode(branchAccess, path, (JSONObject) obj, currentSubNodeType);
		} else if (attributeType == AttributeType.LIST_OF_NODES) {
			JSONArray array = (JSONArray) obj;
			String typeofsubnodes = (String) attrDefinition.get("typeofsubnodes");
			NodeType subNodeType = null;
			if (typeofsubnodes != null) {
				subNodeType = branchAccess.getNodeType(typeofsubnodes);
			}
			List<Node> ret = new ArrayList<Node>();
			for (int i = 0 ; i < array.length() ; i++) {
				try {
					JSONObject arrayobj = (JSONObject) array.get(i);
					String subNodeTypeName = arrayobj.optString("nodetype", null);
					NodeType currentSubNodeType = subNodeType;
					if (subNodeTypeName != null) {
						currentSubNodeType = branchAccess.getNodeType(subNodeTypeName);
					}
					ret.add(new SimpleJsonNode(branchAccess, path, arrayobj, currentSubNodeType));
				} catch (JSONException e) {
					logger.log(Level.SEVERE, "Error while converting property into ListOfNodes", e);
				}
				
			}
			return ret;
		} else if (attributeType == AttributeType.BOOLEAN) {
			if (obj instanceof Boolean) {
				return (Boolean) obj;
			}
			return null;
		} else if (attributeType == AttributeType.INTEGER) {
			if (obj instanceof Integer) {
				return obj;
			}
			if (obj != null) {
				return new Integer(obj.toString());
			}
		}
		logger.info("getting property " + attributeName + " --- attrDefinitions: " + attrDefinitions + " attrDefinition: " + attrDefinition);
		logger.severe("FIXME: Not Implemented: Unable to convert property {" + attributeName + "} of node type {" + getName() + "}: {" + obj.getClass().getName() + "} - attributeType: {" + attributeType + "}");
		return null;
	}
	
	@Override
	public String toString() {
		return "{SimpleJsonNode:" + getPath() + "}";
		//return "{SimpleJsonNode:" + jsonObject.toString() + "}";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimpleJsonNode) {
			return getPath().equals(((SimpleJsonNode)obj).getPath());
		}
		return super.equals(obj);
	}

}
