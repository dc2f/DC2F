package com.dc2f.publish.files;

import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.Node;
import com.dc2f.nodetype.BinaryNodeType;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.url.URLMapper;

/**
 * FIXME unite this class with ServletURLMapper
 */
public class FilePublisherURLMapper implements URLMapper {
	
	private Node renderedNode;
	private BranchAccess craccess;
	private Node[] path;

	public FilePublisherURLMapper(BranchAccess craccess, Node renderedNode) {
		this.craccess = craccess;
		this.renderedNode = renderedNode;
		this.path = craccess.getNodesInPath(renderedNode.getPath());
	}

	public String getRenderURL(Node node) {
		StringBuffer url = new StringBuffer();
		Node[] targetPath = craccess.getNodesInPath(node.getPath());
		
		int startPath = 0;
		for (int i = path.length - 1 ; i >= 0 ; i--) {
			if (targetPath.length > i) {
				if (targetPath[i].equals(path[i])) {
					startPath = i;
					break;
				}
			}
			url.append("../");
		}
		for (int i = startPath ; i < targetPath.length - 1 ; i++) {
			url.append(targetPath[i].getName()).append('/');
		}
		
		url.append(node.getName());
		String fileExtension = ".html";
		if (node.getNodeType() instanceof BinaryNodeType) {
			String mimeType = ((BinaryNodeType)node.getNodeType()).getMimeType(node);
			fileExtension = FilePublisherRenderResponse.getFileExtensionForMimeType(mimeType);
		}
		url.append(fileExtension);
		return url.toString();
	}

}
