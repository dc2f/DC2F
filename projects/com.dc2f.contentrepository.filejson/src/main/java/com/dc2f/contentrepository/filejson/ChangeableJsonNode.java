package com.dc2f.contentrepository.filejson;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.contentrepository.ChangeableNode;
import com.dc2f.contentrepository.NodeType;

public class ChangeableJsonNode extends SimpleJsonNode implements ChangeableNode {

	private static final Logger logger = Logger.getLogger(ChangeableJsonNode.class.getName());
	
	public ChangeableJsonNode(SimpleBranchAccess branchAccess, String path,
			JSONObject jsonObject, NodeType nodeType) {
		super(branchAccess, path, jsonObject, nodeType);
	}

	@Override
	public void set(String attributeName, Object attributeValue) {
		try {
			if(attributeValue instanceof String) {
				getJsonObject().put(attributeName, attributeValue);
			} else {
				logger.severe("FIXME: Not Implemented: cannot set property of class " + attributeValue.getClass());
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Cannot set property {" + attributeName + "}.", e);
		}
	}

}
