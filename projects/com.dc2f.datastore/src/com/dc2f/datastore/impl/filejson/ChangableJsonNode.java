package com.dc2f.datastore.impl.filejson;

import org.json.JSONObject;

import com.dc2f.datastore.ChangableNode;
import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.NodeType;

public class ChangableJsonNode extends SimpleJsonNode implements ChangableNode {

	public ChangableJsonNode(ContentRepository repository, String path,
			JSONObject jsonObject, NodeType nodeType) {
		super(repository, path, jsonObject, nodeType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void set(String propertyName, Object propertyValue) {
	}

}
