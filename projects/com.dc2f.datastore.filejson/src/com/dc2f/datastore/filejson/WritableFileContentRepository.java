package com.dc2f.datastore.filejson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.dc2f.datastore.ChangeableNode;
import com.dc2f.datastore.ContentRepositoryException;
import com.dc2f.datastore.WritableContentRepository;

public class WritableFileContentRepository extends SimpleFileContentRepository
		implements WritableContentRepository {

	private Set<ChangeableNode> writeBuffer = Collections.synchronizedSet(new HashSet<ChangeableNode>());
	
	public WritableFileContentRepository(File crdir)
			throws FileNotFoundException {
		super(crdir);
		// TODO Auto-generated constructor stub
	}
	
	public void close() {
		for(ChangeableNode node : writeBuffer) {
			writeNodeToRepository(node);
		}
	}

	private void writeNodeToRepository(ChangeableNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() {
		writeBuffer.clear();
	}

	@Override
	public void saveNode(ChangeableNode node) throws ContentRepositoryException {
		writeBuffer.add(node);
	}

}
