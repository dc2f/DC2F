package com.dc2f.renderer.impl;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;

public class ContentRenderRequestImpl implements ContentRenderRequest {

	private Node node;
	private ContentRepository contentRepository;

	public ContentRenderRequestImpl(ContentRepository contentRepository, Node node) {
		this.contentRepository = contentRepository;
		this.node = node;
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public ContentRepository getContentRepository() {
		return contentRepository;
	}
}
