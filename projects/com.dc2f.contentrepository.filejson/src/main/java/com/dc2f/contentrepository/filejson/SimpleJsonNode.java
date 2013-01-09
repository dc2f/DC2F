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
import com.dc2f.contentrepository.DefaultNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.exception.UnknownAttributeException;

/**
 * Represents a {@link Node} from the {@link SimpleFileContentRepository}.
 */
public class SimpleJsonNode implements Node {
	/**
	 * Logger for debug and error messages.
	 */
	private static final Logger LOGGER =
		Logger.getLogger(SimpleJsonNode.class.getName());

	/**
	 * Branch access for fetching additional nodes.
	 */
	private SimpleBranchAccess branchAccess;
	
	/**
	 * Path of the current node. (More specifically the path in the filesystem
	 * where the _core.json file can be found that defines this node)
	 * This is not unique if one _core.json file defines more than one node (eg
	 * subnodes have the same path).
	 */
	private String path;
	/**
	 * Internal path to the object. additionally to the path this contains an
	 * identifier for internal nodes. this is unique even for mutliple subnodes
	 * within the same _core.json file.
	 */
	private String internalPath;
	
	/**
	 * json object for _core.json defining this node.
	 */
	private JSONObject jsonObject;
	
	/**
	 * type information for this node.
	 */
	private NodeType nodeType;

	/**
	 * Create a new Node.
	 * @param newBranchAccess - BranchAccess for fetching addtional nodes.
	 * @param newPath - path to the node
	 * @param newJsonObject - json object for this node
	 * @param newNodeType - type of this node
	 */
	public SimpleJsonNode(final SimpleBranchAccess newBranchAccess,
			final String newPath, final JSONObject newJsonObject,
			final NodeType newNodeType) {
		// "normalize" path :)
		branchAccess = newBranchAccess;
		path = newPath;
		if (path != null && path.endsWith("/") && !"/".equals(path)) {
			path = path.replaceAll("/+$", "");
		}
		internalPath = path;
		jsonObject = newJsonObject;
		nodeType = newNodeType;
	}
	
	/**
	 * @return the json object that defined this node.
	 */
	public final JSONObject getJsonObject() {
		return jsonObject;
	}

	@Override
	public final String getName() {
		return new File(path).getName();
	}

	@Override
	public final NodeType getNodeType() {
		return nodeType;
	}

	@Override
	public final String getPath() {
		return path;
	}
	/**
	 * @param name - name of the attribute to get
	 * @return the attribute with the given name from the json object
	 */
	protected Object internalGet(final String name) {
		try {
			return jsonObject.get(name);
		} catch (JSONException e) {
			if (jsonObject.has(name)) {
				LOGGER.log(Level.SEVERE, "Error while getting attribute {"
						+ name + "} of node type {" + path + "}", e);
			}
			return null;
		}
	}
	
	
	
	@Override
	public final Object get(final String attributeName) {
		Object obj = internalGet(attributeName);
		
		// FIXME do this the "clean way"
		if (obj instanceof String && ("class".equals(attributeName)
				|| "type".equals(attributeName))) {
			return obj;
		}
		if (getNodeType() == null) {
			throw new RuntimeException("Unable to find nodetype for node "
					+ getPath());
		}
		
		AttributesDefinition attrDefinitions =
			getNodeType().getAttributeDefinitions();
		LOGGER.finest(this.getName() + ": Getting attribute {" + attributeName
				+ "}: " + obj + " - attrDefinitions: " + attrDefinitions);
		AttributeDefinition attrDefinition =
			attrDefinitions.getAttributeDefinition(attributeName);
		if (attrDefinition == null) {
			throw new UnknownAttributeException(attributeName, this);
		}
		AttributeType attributeType = attrDefinition.getAttributeType();
		
		if (attributeType == AttributeType.NODE_REFERENCE && obj instanceof String) {
			String ref = (String) obj;
			LOGGER.fine("Trying to resolve object {" + ref + "}");
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
			obj = branchAccess.getContentRepository().loadRepositoryFile(new File(internalPath, attributeName + ".clob.property"));
		} else if (attributeType == AttributeType.BLOB && obj == null) {
			return branchAccess.getContentRepository().getInputStreamForRepositoryFile(new File(path, attributeName + ".blob.property"));
		}

		if (obj instanceof String) {
			return obj;
		}
		if (obj == null || obj == JSONObject.NULL) {
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
			SimpleJsonNode retNode = new SimpleJsonNode(branchAccess, path, (JSONObject) obj, currentSubNodeType);
			retNode.setInternalPath(internalPath + "/@" + attributeName);
			return retNode;
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
					LOGGER.log(Level.SEVERE, "Error while converting property into ListOfNodes", e);
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
			return Integer.valueOf(obj.toString());
		}
		LOGGER.info("getting property " + attributeName + " --- attrDefinitions: " + attrDefinitions + " attrDefinition: " + attrDefinition);
		LOGGER.severe("FIXME: Not Implemented: Unable to convert property {" + attributeName + "} of node type {" + getName() + "}: {" + obj.getClass().getName() + "} - attributeType: {" + attributeType + "}");
		return null;
	}

	private void setInternalPath(String internalPath) {
		this.internalPath = internalPath;
	}

	@Override
	public String toString() {
		return "{SimpleJsonNode:" + getPath() + "}";
		//return "{SimpleJsonNode:" + jsonObject.toString() + "}";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimpleJsonNode) {
			return internalPath.equals(((SimpleJsonNode)obj).internalPath);
//			return getPath().equals(((SimpleJsonNode)obj).getPath());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return internalPath.hashCode();
	}

}
