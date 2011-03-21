package com.dc2f.renderer.nodetype.template;

import com.dc2f.datastore.Node;
import com.dc2f.nodetype.renderable.RenderableNodeType;
import com.dc2f.renderer.ContentRenderRequest;

public abstract class TemplateNodeType extends RenderableNodeType {
	public abstract String renderTemplate(ContentRenderRequest request, Node template);
}
