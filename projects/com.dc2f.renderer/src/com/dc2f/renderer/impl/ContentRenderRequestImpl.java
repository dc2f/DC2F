package com.dc2f.renderer.impl;

import java.util.HashMap;
import java.util.Map;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;

public class ContentRenderRequestImpl implements ContentRenderRequest {

	private Node node;
	private ContentRepository contentRepository;
	private Map<String, Object> attributes = new HashMap<String, Object>();

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

	@Override
	public Object setAttribute(String key, Object value) {
		return attributes.put(key, value);
	}
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
}
