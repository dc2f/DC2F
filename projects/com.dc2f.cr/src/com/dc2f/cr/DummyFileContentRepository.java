package com.dc2f.cr;

import java.io.File;

import com.dc2f.core.Node;

public class DummyFileContentRepository implements ContentRepository {
	private File dir;

	public DummyFileContentRepository(File directory) {
		this.dir = directory;
	}

	public Node getNode(String path) {
		return null;
	}
}
