package com.dc2f.datastore.impl.filejson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.datastore.NodeType;

public class SimpleFileContentRepository implements ContentRepository {
	private static final Logger logger = Logger.getLogger(SimpleFileContentRepository.class.getName());
	private static final int BUFFER_SIZE = 1024;
	
	private static final Charset CHARSET = Charset.forName("UTF-8");

	private File crdir;

	public SimpleFileContentRepository(File crdir) throws FileNotFoundException {
		if (!crdir.exists() || !crdir.isDirectory()) {
			throw new FileNotFoundException(
					"Error while initiating file content repository - crdir is not a valid directory {"
							+ crdir.getAbsolutePath() + "}");
		}
		this.crdir = crdir;
	}
	
	protected File getCrdir() {
		return crdir;
	}
	
	protected String loadFile(InputStream inputStream) {
		try {
			InputStreamReader reader = new InputStreamReader(inputStream, CHARSET);
			StringBuilder builder = new StringBuilder();//(int) f.length());
			int c;
			for (char[] buf = new char[BUFFER_SIZE] ; (c = reader.read(buf, 0, BUFFER_SIZE)) > 0 ; ) {
				builder.append(buf, 0, c);
			}
			return builder.toString();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading from input stream.", e);
		}
		return null;
	}
	
	protected String loadRepositoryFile(File file) {
		try {
			return loadFile(new FileInputStream(new File(crdir, file.getPath())));
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Error while loading file {" + file.getAbsolutePath() + "}", e);
			return null;
		}
	}
	
	protected JSONObject loadJSON(File f) {
		try {
			return loadJSON(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "Unable to find file for node {" + f.getAbsolutePath() + "}", e);
			return null;
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error while parsing file {" + f.getAbsolutePath() + "}", e);
			return null;
		}
	}
	
	protected JSONObject loadJSON(InputStream inputStream) throws JSONException {
		String str = loadFile(inputStream);
		if (str == null) {
			return null;
		}
		return new JSONObject(str);
	}
	
	protected JSONObject loadJSONFromPath(String path, String appendFileName) {
		if (path.startsWith("classpath:")) {
			// FIXME: umm.. do it the ugly way (for now?)
			if (appendFileName != null) {
				path = path + "/" + appendFileName;
			}
			path = path.substring("classpath:".length());
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			InputStream stream = this.getClass().getResourceAsStream(path);
			if (stream == null) {
				logger.severe("Unable to find resource in classpath: " + path + " === " + path.substring("classpath:".length()));
				return null;
			}
			try {
				return loadJSON(stream);
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "Error while parsing file {" + path + "}", e);
				return null;
			}
		}
		if (appendFileName != null) {
			return loadJSON(new File(new File(crdir, path), appendFileName));
		} else {
			return loadJSON(new File(crdir, path));
		}
	}
	
	@Override
	public Node getNode(String path) {
		JSONObject jsonObject = loadJSONFromPath(path, "_core.json");
		NodeType nodeType = null;
		try {
			nodeType = getNodeType((String) jsonObject.get("nodetype"));
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error while fetching nodetype property from json object", e);
		}
		return new SimpleJsonNode(this, path, jsonObject, nodeType);
	}

	@Override
	public NodeType getNodeType(String path) {
		JSONObject jsonObject = loadJSONFromPath(path + ".json", null);
		String extendsNodeType = jsonObject.optString("extends", null);
		NodeType parentNodeType = null;
		if (extendsNodeType != null) {
			parentNodeType = getNodeType(extendsNodeType);
		}
		SimpleJsonNodeTypeInfo nodeTypeInfo = new SimpleJsonNodeTypeInfo(this, parentNodeType, path, jsonObject);
		Class<NodeType> nodeTypeClass = nodeTypeInfo.getNodeTypeClass();
		try {
			NodeType nodeType = nodeTypeClass.newInstance();
			nodeType.init(nodeTypeInfo);
			return nodeType;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while creating instance for node type {" + nodeTypeInfo + "}", e);
			return null;
		}
	}

	@Override
	public Node getParentNode(Node node) {
		logger.info("Getting parent for node {" + node.getPath() + "}");
		if ("/".equals(node.getPath())) {
			return null;
		}
		return getNode(new File(node.getPath()).getParent());
	}

	@Override
	public Node[] getNodesInPath(String path) {
		// FIXME: this is a very lazy way to find the nodes in the path.. we should split the path and walk through it.. (what if one node has more than one parent, or there are "symlinks", etc. in the path)
		ArrayList<Node> ret = new ArrayList<Node>();
		Node node = getNode(path);
		while (node != null) {
			ret.add(0, node);
			node = this.getParentNode(node);
		}
		return ret.toArray(new Node[ret.size()]);
	}

	@Override
	public Node resolveNode(Node currentNodeContext, String ref) {
		try {
			return getNode(new URI(currentNodeContext.getPath() + "/").resolve(ref).toString());
		} catch (URISyntaxException e) {
			logger.log(Level.SEVERE, "Error while resolving ref {" + ref + "}", e);
			return null;
		}
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean supportsTransaction() {
		return false;
	}

	@Override
	public Node[] getChildren(Node node) {
		File[] files = new File(crdir, node.getPath()).listFiles();
		List<Node> children = new ArrayList<Node>(files.length);
		for(File file : files) {
			if (file.isDirectory()) {
				Node child = getNode(node.getPath() + "/" + file.getName());
				if (child != null) {
					children.add(child);
				}
			}
		}
		return children.toArray(new Node[children.size()]);
	}

}
