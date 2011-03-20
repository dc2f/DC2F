package com.dc2f.datastore.impl.filejson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.nodetype.NodeType;

public class SimpleFileContentRepository implements ContentRepository {
	private static final Logger logger = Logger.getLogger(SimpleFileContentRepository.class.getName());
	private static final int BUFFER_SIZE = 1024;

	private File crdir;

	public SimpleFileContentRepository(File crdir) throws FileNotFoundException {
		if (!crdir.exists() || !crdir.isDirectory()) {
			throw new FileNotFoundException(
					"Error while initiating file content repository - crdir is not a valid directory {"
							+ crdir.getAbsolutePath() + "}");
		}
		this.crdir = crdir;
	}
	
	protected String loadFile(File f) {
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
			StringBuilder builder = new StringBuilder((int) f.length());
			int c;
			for (char[] buf = new char[BUFFER_SIZE] ; (c = reader.read(buf, 0, BUFFER_SIZE)) > 0 ; ) {
				builder.append(buf, 0, c);
			}
			return builder.toString();
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "Unable to find file for node {" + f.getAbsolutePath() + "}", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading from file for node {" + f.getAbsolutePath() + "}", e);
		}
		return null;
	}
	
	protected JSONObject loadJSON(File f) {
		String str = loadFile(f);
		if (str == null) {
			return null;
		}
		try {
			return new JSONObject(str);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error while parsing file for node {" + f.getAbsolutePath() + "}", e);
			return null;
		}
	}

	@Override
	public Node getNode(String path) {
		JSONObject jsonObject = loadJSON(new File(new File(crdir, path), "_core.json"));
		return new SimpleJsonNode(path, jsonObject, null);
	}

	@Override
	public NodeType getNodeType(String path) {
		return null;
	}

}
