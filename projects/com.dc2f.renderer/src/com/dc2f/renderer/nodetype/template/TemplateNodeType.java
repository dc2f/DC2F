package com.dc2f.renderer.nodetype.template;

import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.nodetype.RenderableNodeType;

public abstract class TemplateNodeType extends RenderableNodeType {
	public abstract String renderTemplate(ContentRenderRequest request, Node template);
}
