package com.dc2f.renderer.nodetype;

import com.dc2f.datastore.Node;
import com.dc2f.datastore.NodeType;
import com.dc2f.renderer.ContentRenderRequest;

/**
 * A node type which is responsible for rendering content referenced in the 'context' 
 * of a template.
 * 
 * @author herbert
 */
public interface ContextRendererNodeType extends NodeType {
	public String renderNode(Node configNode, ContentRenderRequest request,
			Node context, Object value);
}
