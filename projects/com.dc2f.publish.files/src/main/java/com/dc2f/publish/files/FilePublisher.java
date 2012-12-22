package com.dc2f.publish.files;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.RuntimeErrorException;

import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRSession;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.ContentRepositoryFactory;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.nodetype.BinaryNodeType;
import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.NodeRendererFactory;
import com.dc2f.renderer.impl.ContentRenderRequestImpl;
import com.dc2f.renderer.nodetype.RenderableNodeType;
import com.dc2f.renderer.nodetype.template.TemplateNodeType;

/**
 * publisher which will write out static files from a content repository.
 * 
 * @author herbert
 */
public class FilePublisher {
	static Logger logger = Logger.getLogger(FilePublisher.class.getName());
	
	
	private BranchAccess craccess;
	private NodeRendererFactory factory;
	private NodeRenderer renderer;


	private ContentRepository cr;


	private File outputbasedir;


	private FilePublisherURLMapper projectUrlMapper;
	private Node project;
	
	public FilePublisher() {
	}
	
	
	protected void init() {
		factory = NodeRendererFactory.getInstance();
		renderer = factory.getRenderer("com.dc2f.renderer.web");
		File crdir = new File(System.getProperty("crdir", "../../design/src/main/resources/example"));
		if (crdir == null || !crdir.exists()) {
			System.out.println("Please specify a crdir :) ( -Dcrdir=xxx) - not found: " + crdir.getAbsolutePath());
		}
		cr = ContentRepositoryFactory.getInstance().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)crdir.getAbsolutePath()));
		
		outputbasedir = new File("out/publish");
		outputbasedir.mkdirs();
	}
	
	public void run() {
		init();
		CRSession conn = cr.authenticate(null);
		this.craccess = conn.openTransaction(null);

		
		
		String rootProjectPath = "/cmsblog";
		rootProjectPath = System.getProperty("projectpath", rootProjectPath);
		
//		renderNodeRecursive(craccess.getNode(rootProjectPath));
		renderProject(craccess.getNode(rootProjectPath));
	}
	
	
	protected void renderProject(Node project) {
		// get all render configurations from the project
		this.project = project;
		projectUrlMapper = new FilePublisherURLMapper(craccess, project, null);

		@SuppressWarnings("unchecked")
		List<Node> renderConfigurations = (List<Node>) project.get("renderconfiguration");
		
		// look for all url mappings
		for (Node renderConfig : renderConfigurations) {
			
			@SuppressWarnings("unchecked")
			List<Node> urlMappingList = (List<Node>) renderConfig.get("urlmapping");
			
			if (urlMappingList != null && urlMappingList.size() > 0) {
				for (Node urlmapping : urlMappingList) {
					logger.info("We have a render configuration with a url mapper. " + urlmapping.get("url"));
					// render the url mapping
					Node rootNode = (Node) renderConfig.get("rootnode");
					renderChildNodes(renderConfig, urlmapping, rootNode);
				}
			}
		}
	}


	private void renderChildNodes(Node renderConfig, Node urlmapping,
			Node baseNode) {
		NodeType basenodetype = (NodeType) renderConfig.get("targetnodetype");
		Node[] children = craccess.getChildren(baseNode);
		if (children == null) {
			return;
		}
		for(Node child : children) {
			renderChildNodes(renderConfig, urlmapping, child);
			if (basenodetype != null) {
				if (!basenodetype.getClass().isAssignableFrom(child.getNodeType().getClass())) {
					continue;
				}
			}
			logger.info("Rendering " + child);
			try {
				renderNodeWithConfig(child, renderConfig, urlmapping);
			} catch (Throwable e) {
				throw new RuntimeException("Error while rendering " + child, e);
			}
		}
	}
	
	private void renderNodeWithConfig(Node child, Node renderConfig, Node urlmapping) {
		Node template = (Node) renderConfig.get("template");
		// find target url
		String path = projectUrlMapper.getRenderPath(child);
		// render node
		FilePublisherURLMapper urlMapper = new FilePublisherURLMapper(craccess, project, child);
		ContentRenderRequestImpl request = new ContentRenderRequestImpl(cr, craccess, craccess.getNodesInPath(child.getPath()), urlMapper);
		FilePublisherRenderResponse response = new FilePublisherRenderResponse(outputbasedir, urlMapper, child);
		renderer.renderNode(request, response);
		logger.info(" ... with template " + template + " into " + path);
		if (response.hadOutput()) {
			try {
				response.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while closing output file.", e);
			}
		}

	}


	/**
	 * will check all render configurations and render the node accordingly into the file system.
	 * @deprecated
	 */
	protected void renderNode(Node node) {
		logger.info("--- Rendering " + node.getPath() + " xxx " + node.getNodeType() + " is renderable? " + (node.getNodeType() instanceof RenderableNodeType));
		
		NodeType nodeType = node.getNodeType();
		
		if ((nodeType instanceof BinaryNodeType || nodeType instanceof RenderableNodeType)
				&& !(nodeType instanceof TemplateNodeType)) {
			FilePublisherURLMapper urlMapper = new FilePublisherURLMapper(craccess, null, node);
			ContentRenderRequestImpl request = new ContentRenderRequestImpl(cr, craccess, craccess.getNodesInPath(node.getPath()), urlMapper);
			FilePublisherRenderResponse response = new FilePublisherRenderResponse(outputbasedir, null, node);
			renderer.renderNode(request, response);
			if (response.hadOutput()) {
				try {
					response.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Error while closing output file.", e);
				}
			}
		}
	}
	
	/**
	 * will render the given node and all sub nodes.
	 * @deprecated
	 */
	protected void renderNodeRecursive(Node node) {
		renderNode(node);
		
		Node[] children = craccess.getChildren(node);
		for (Node child : children) {
			renderNodeRecursive(child);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FilePublisher().run();
	}

}
