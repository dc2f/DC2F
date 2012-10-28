package com.dc2f.contentrepository.filejson;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRAdapter;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.adapters.WriteAccessAdapter;
import com.dc2f.contentrepository.adapters.XPathSearchAdapter;

public class SimpleBranchAccess implements BranchAccess {
	
	private static final Logger logger = Logger.getLogger(SimpleBranchAccess.class.getName());
	
	private SimpleCRSession session;
	private String branchName;
	private SimpleFileContentRepository cr;

	public SimpleBranchAccess(SimpleCRSession simpleCRSession, String branchName) {
		this.session = simpleCRSession;
		this.cr = simpleCRSession.getContentRepository();
		this.branchName = branchName;
	}

	@Override
	public Node getNode(String path) {
		JSONObject jsonObject = cr.loadJSONFromPath(path, "_core.json");
		NodeType nodeType = null;
		try {
			nodeType = getNodeType((String) jsonObject.get("nodetype"));
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error while fetching nodetype property from json object", e);
		}
		return new ChangeableJsonNode(this, path, jsonObject, nodeType);
	}

	@Override
	public NodeType getNodeType(String path) {
		JSONObject jsonObject = cr.loadJSONFromPath(path + ".json", null);
		if (jsonObject != null) {
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
		} else {
			logger.log(Level.FINE, "Cannot get node type for path " + path);
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
		File[] files = new File(cr.getCrdir(), node.getPath()).listFiles();
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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends CRAdapter> T getAdapter(Class<T> adapterInterface) {
		if (adapterInterface.isAssignableFrom(XPathSearchAdapter.class)) {
			return (T) new SimpleXPathSearchAdapter(this);
		} else if (adapterInterface.isAssignableFrom(WriteAccessAdapter.class)) {
			return (T) new SimpleJsonWriteAccess(this);
		}
		return null;
	}

	public SimpleFileContentRepository getContentRepository() {
		return cr;
	}
}
