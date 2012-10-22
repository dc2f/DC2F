package com.dc2f.contentrepository.filejson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.adapters.SourceWriteAccessAdapter;
import com.dc2f.contentrepository.adapters.WriteAccessAdapter;

public class SimpleJsonWriteAccess implements WriteAccessAdapter, SourceWriteAccessAdapter {

	
	private static final Logger logger = Logger.getLogger(SimpleJsonWriteAccess.class.getName());

	/**
	 * use 4 kilobytes buffer for writing files.
	 */
	private static final int BUFFER_SIZE = 4096;
	
	/**
	 * Holds our access object to the repository.
	 */
	private SimpleBranchAccess craccess;
	
	public SimpleJsonWriteAccess(SimpleBranchAccess access) {
		craccess = access;
	}

	@Override
	public boolean saveNode(Node node) {
		if (node instanceof SimpleJsonNode) {
			JSONObject json = ((SimpleJsonNode) node).getJsonObject();
			File core = new File(new File(craccess.getContentRepository().getCrdir(), node.getPath()), "_core.json");
			try {
				StringReader reader = new StringReader(json.toString(4));
				OutputStreamWriter output = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(core)), SimpleFileContentRepository.CHARSET);
				int c;
				for (char[] buf = new char[BUFFER_SIZE] ; (c = reader.read(buf, 0, BUFFER_SIZE)) > 0 ; ) {
					output.write(buf, 0, c);
				}
				output.close();
				reader.close();
				return true;
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while reading from input stream.", e);
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "Error while creating JSON String from object {" + node.getPath() + "}", e);
			}
		} else {
			logger.severe("Cannot save node with unknown type {" + node.getClass() + "} only SimpleJsonNode is allowed.");
		}
		return false;
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
