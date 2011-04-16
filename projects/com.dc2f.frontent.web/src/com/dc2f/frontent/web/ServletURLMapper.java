package com.dc2f.frontent.web;

import javax.servlet.ServletRequest;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.renderer.url.URLMapper;

public class ServletURLMapper implements URLMapper {

	private ContentRepository contentRepository;
	
	public ServletURLMapper(ContentRepository cr) {
		contentRepository = cr;
	}

	public Node getNode(ServletRequest request) {
		return contentRepository.getNode("/cmsblog/articles/my-first-article");
	}

	@Override
	public String getRenderURL(Node node) {
		throw new RuntimeException("This method is not yet implemented.");
	}

}
