package com.dc2f.nodetype.renderable.template;

import com.dc2f.datastore.Node;
import com.dc2f.nodetype.renderable.RenderableNodeType;

public abstract class TemplateNodeType extends RenderableNodeType {
	public abstract String renderTemplate(Node node);
}
