package com.dc2f.cr;

import java.io.File;
import java.util.List;

import com.dc2f.core.Node;

public class SimpleFileContentRepository implements ContentRepository {
	private File dir;

	public SimpleFileContentRepository(File directory) {
		this.dir = directory;
	}

	public Node getNode(String path) {
		return null;
	}

	public List<Node> getSubNodes(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Node> getSubNodes(Node node) {
		// TODO Auto-generated method stub
		return null;
	}
}
