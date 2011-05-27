package com.dc2f.renderer;

import java.util.List;

import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.url.URLMapper;

public interface ContentRenderRequest {
	public Node getNode();
	/**
	 * Returns all nodes which were part of the request.
	 */
	public Node[] getNodesInPath();
	
	public Node popFromNodeStack();

	public ContentRepository getContentRepository();

	public Object setAttribute(String key, Object value);

	public Object getAttribute(String key);
	public void pushNodeContext(Node node);
	public Node popNodeContext();
	public Node getCurrentNodeContext();
	public void pushRenderContext(Node addToContext);
	public Node popRenderContext();
	public List<Node> getRenderContextStack();
	public URLMapper getURLMapper();
}
