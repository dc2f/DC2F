package com.dc2f.contentrepository.filejson;

import org.json.JSONObject;

import com.dc2f.contentrepository.ChangeableNode;
import com.dc2f.contentrepository.NodeType;

public class ChangeableJsonNode extends SimpleJsonNode implements ChangeableNode {

	public ChangeableJsonNode(SimpleBranchAccess branchAccess, String path,
			JSONObject jsonObject, NodeType nodeType) {
		super(branchAccess, path, jsonObject, nodeType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void set(String attributeName, Object attributeValue) {
	}

}
