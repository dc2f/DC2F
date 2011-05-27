package com.dc2f.nodetype;

import java.io.InputStream;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;

public class File extends BaseNodeType implements BinaryNodeType {

	public InputStream getInputStream(Node node) {
		return (InputStream) node.get("data");
	}

	public String getMimeType(Node node) {
		return (String) node.get("mimeType");
	}


}
