package com.dc2f.backend.gwt.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.dc2f.backend.gwt.client.services.DC2FContentService;
import com.dc2f.backend.gwt.shared.DTOAttributesDefinition;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.dc2f.backend.gwt.shared.DTONodeType;
import com.dc2f.backend.gwt.shared.NotChangeableException;
import com.dc2f.contentrepository.AttributesDefinition;
import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.ChangeableNode;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.adapters.SourceWriteAccessAdapter;
import com.dc2f.contentrepository.adapters.WriteAccessAdapter;

/**
 *
 */
public class DC2FContentServiceImpl extends DC2FNavigationServiceImpl implements DC2FContentService {

	/** **/
	private Logger logger = Logger.getLogger(DC2FContentServiceImpl.class.getName());

	/**
	 * generated unique serialization id.
	 */
	private static final long serialVersionUID = 4683885240439796785L;

	/**
	 * content service constructor.
	 */
	public DC2FContentServiceImpl() {
		super();
	}

	/** {@inheritDoc} */
	public DTOEditableNode getEditableNodeForPath(final String path) {
		logger.info("Getting node for path {" + path + "}");
		com.dc2f.contentrepository.Node dc2fNode = getBranchAccess().getNode(path);
		DTOEditableNode node = convertNodeToDTO(dc2fNode, null);

		return node;
	}

	/**
	 * converts a dc2f node to a DTO node.
	 * 
	 * @param dc2fNode the node to convert to dto.
	 * @param simpleNodeCache just a hash map of already converted nodes - currently this is only required to prevent infinite recursions :)
	 * @return converted node.
	 */
	private static DTOEditableNode convertNodeToDTO(final Node dc2fNode, final Map<String, DTOEditableNode> simpleNodeCache) {
		Map<String, DTOEditableNode> mySimpleNodeCache = simpleNodeCache;
		if (mySimpleNodeCache == null) {
			mySimpleNodeCache = new HashMap<String, DTOEditableNode>();
		}
		DTOEditableNode tmp = mySimpleNodeCache.get(dc2fNode.getPath());
		if (tmp != null) {
			return tmp;
		}

		DTOEditableNode node = new DTOEditableNode();
		node.setName(dc2fNode.getName());
		node.setPath(dc2fNode.getPath());
		mySimpleNodeCache.put(dc2fNode.getPath(), node);
		NodeType type = dc2fNode.getNodeType();
		if (type != null) {
			AttributesDefinition attrsDefinition = type.getAttributeDefinitions();
			DTOAttributesDefinition dtoAttrsDef = new DTOAttributesDefinition(attrsDefinition.getAttributeNames());

			node.setNodeType(new DTONodeType(type.getNodeTypeInfo().getPath(), dtoAttrsDef));
			for (String attributeName : type.getAttributeDefinitions().getAttributeNames()) {
				Object attributeValue = dc2fNode.get(attributeName);
				if (attributeValue instanceof String) {
					node.set(attributeName, (String) attributeValue);
				}
				if (type instanceof AttributesDefinition) {
					// don't serialize attributes definition of attributes definition :)
					continue;
				}

				Node def = attrsDefinition.getAttributeDefinition(attributeName);
				dtoAttrsDef.setAttributeDefinition(attributeName, convertNodeToDTO(def, simpleNodeCache));
			}
		}
		return node;
	}

	/** {@inheritDoc} */
	public DTOEditableNode saveNode(final DTOEditableNode node) {
		BranchAccess craccess = getBranchAccess();
		com.dc2f.contentrepository.Node dc2fNode = craccess.getNode(node.getPath());
		if (dc2fNode instanceof ChangeableNode) {
			ChangeableNode dc2fChangeableNode = (ChangeableNode) dc2fNode;
			for (String attributeName : node.getAttributeNames()) {
				dc2fChangeableNode.set(attributeName, node.get(attributeName));
			}
			craccess.getAdapter(WriteAccessAdapter.class).saveNode(dc2fNode);
			return node;
		} else {
			throw new NotChangeableException("Node " + node.getPath() + " is not changeable.");
		}
	}

	/** {@inheritDoc} */
	public DTOEditableNode saveNode(final DTOEditableNode node, final String source) {
		BranchAccess craccess = getBranchAccess();
		com.dc2f.contentrepository.Node dc2fNode = craccess.getNode(node.getPath());
		if (craccess.getAdapter(SourceWriteAccessAdapter.class).saveNode(dc2fNode, source)) {
			craccess.getNode(node.getPath());
		}
		return node;
	}

	/** {@inheritDoc} */
	public String getSource(final DTOEditableNode node) {
		// TODO this should be provided by the repository instead of fetching the systemproperty
		String crDir = System.getProperty("crdir");
		File file = new File(crDir + node.getPath());
		if (file.isDirectory()) {
			file = new File(file, "_core.json");
		}
		if (file.canRead()) {
			try {
				StringWriter writer = new StringWriter();
				IOUtils.copy(new FileInputStream(file), writer, "UTF-8");
				return writer.toString();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "Couldn't get source.";
	}

}
