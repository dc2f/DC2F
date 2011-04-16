package com.dc2f.datastore.impl.filejson;

import com.dc2f.datastore.Node;
import com.dc2f.datastore.adapters.XPathSearchAdapter;

public class SimpleXPathSearchAdapter implements XPathSearchAdapter {

	public SimpleXPathSearchAdapter(
			SimpleFileContentRepository simpleFileContentRepository) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node[] search(String xpathExpression, String[] sortOrder,
			int offset, int limit) {
		return null;
	}

}
