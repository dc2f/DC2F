package com.dc2f.contentrepository.filejson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.adapters.SourceWriteAccessAdapter;
import com.dc2f.contentrepository.adapters.WriteAccessAdapter;

public class SimpleJsonWriteAccess implements WriteAccessAdapter, SourceWriteAccessAdapter {

	
	private static final Logger logger = Logger.getLogger(SimpleJsonWriteAccess.class.getName());
	
	/**
	 * Holds our access object to the repository.
	 */
	private SimpleBranchAccess craccess;
	
	public SimpleJsonWriteAccess(SimpleBranchAccess access) {
		craccess = access;
	}

	@Override
	public boolean saveNode(Node node) {
		//TODO convert the Node to vaild json.
		return false;
		//return saveNode(node, source);
	}

	@Override
	public boolean saveNode(Node node, String source) {
		File crdir = craccess.getContentRepository().getCrdir();
		File nodeFile = new File(crdir, "_core.json");
		try {
			IOUtils.copy(new StringReader(source), new FileOutputStream(nodeFile), "");
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "Somehow the file for the node " + node + " (" + nodeFile + ") cannot be written to the filesystem.");
			return false;
		} catch (IOException e) {
			logger.log(Level.WARNING, "Somehow the file for the node " + node + " (" + nodeFile + ") cannot be written to the filesystem.");
			return false;
		}
		return true;
	}

}
