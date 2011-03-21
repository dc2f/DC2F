package com.dc2f.renderer.nodetype.template;

import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;


public class HtmlTemplate extends TemplateNodeType {

	@Override
	public String renderTemplate(ContentRenderRequest request, Node template) {
		Node node = request.getNode();
		String source = (String) template.getProperty("source");
		return source;
	}

}
