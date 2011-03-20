package com.dc2f.renderer.impl;

import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;

public class ContentRenderRequestImpl implements ContentRenderRequest {

	private Node node;

	public ContentRenderRequestImpl(Node node) {
		this.node = node;
	}

	@Override
	public Node getNode() {
		return node;
	}
}
