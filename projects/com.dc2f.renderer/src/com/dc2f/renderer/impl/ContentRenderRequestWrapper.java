package com.dc2f.renderer.impl;

import java.util.List;

import com.dc2f.contentrepository.CRAccess;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.url.URLMapper;

public class ContentRenderRequestWrapper implements ContentRenderRequest {
	
	private ContentRenderRequest wrappedRequest;
	private Node[] newNodePath;

	public ContentRenderRequestWrapper(ContentRenderRequest wrappedRequest, Node[] newNodePath) {
		this.wrappedRequest = wrappedRequest;
		this.newNodePath = newNodePath;
	}
	

	@Override
	public Object getAttribute(String key) {
		return wrappedRequest.getAttribute(key);
	}

	@Override
	public CRAccess getContentRepositoryTransaction() {
		return wrappedRequest.getContentRepositoryTransaction();
	}

	@Override
	public Node getCurrentNodeContext() {
		return wrappedRequest.getCurrentNodeContext();
	}

	@Override
	public Node getNode() {
		if (newNodePath != null) {
			return newNodePath[newNodePath.length-1];
		}
		return wrappedRequest.getNode();
	}

	@Override
	public Node[] getNodesInPath() {
		if (newNodePath != null) {
			return newNodePath;
		}
		return wrappedRequest.getNodesInPath();
	}

	@Override
	public List<Node> getRenderContextStack() {
		return wrappedRequest.getRenderContextStack();
	}

	@Override
	public Node popFromNodeStack() {
		return wrappedRequest.popFromNodeStack();
	}

	@Override
	public Node popNodeContext() {
		return wrappedRequest.popNodeContext();
	}

	@Override
	public Node popRenderContext() {
		return wrappedRequest.popRenderContext();
	}

	@Override
	public void pushNodeContext(Node node) {
		wrappedRequest.pushNodeContext(node);
	}

	@Override
	public void pushRenderContext(Node addToContext) {
		wrappedRequest.pushRenderContext(addToContext);
	}

	@Override
	public Object setAttribute(String key, Object value) {
		return wrappedRequest.setAttribute(key, value);
	}


	@Override
	public URLMapper getURLMapper() {
		return wrappedRequest.getURLMapper();
	}


	@Override
	public ContentRepository getContentRepository() {
		return wrappedRequest.getContentRepository();
	}

}
