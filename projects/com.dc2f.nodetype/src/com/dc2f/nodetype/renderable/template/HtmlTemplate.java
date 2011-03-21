package com.dc2f.nodetype.renderable.template;

import com.dc2f.datastore.Node;


public class HtmlTemplate extends TemplateNodeType {

	@Override
	public String renderTemplate(Node node) {
		String source = (String) node.getProperty("source");
		return source;
	}

}
