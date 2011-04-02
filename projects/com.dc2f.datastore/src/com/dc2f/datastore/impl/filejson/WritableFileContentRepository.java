package com.dc2f.datastore.impl.filejson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.dc2f.datastore.ContentRepositoryException;
import com.dc2f.datastore.Node;
import com.dc2f.datastore.WritableContentRepository;

public class WritableFileContentRepository extends SimpleFileContentRepository
		implements WritableContentRepository {

	private Set<Node> writeBuffer = Collections.synchronizedSet(new HashSet<Node>());
	
	public WritableFileContentRepository(File crdir)
			throws FileNotFoundException {
		super(crdir);
		// TODO Auto-generated constructor stub
	}
	
	public void close() {
		for(Node node : writeBuffer) {
			writeNodeToRepository(node);
		}
	}

	private void writeNodeToRepository(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() {
		writeBuffer.clear();
	}

	@Override
	public void writeNode(Node node) throws ContentRepositoryException {
		writeBuffer.add(node);
	}

}
