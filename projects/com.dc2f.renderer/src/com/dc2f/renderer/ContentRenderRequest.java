package com.dc2f.renderer;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;

public interface ContentRenderRequest {
	public Node getNode();

	public ContentRepository getContentRepository();

	public Object setAttribute(String key, Object value);

	public Object getAttribute(String key);
}
