package com.dc2f.publish.files;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		
		renderNodeRecursive(craccess.getNode(rootProjectPath));
	}
	
	/**
	 * will check all render configurations and render the node accordingly into the file system.
	 */
	protected void renderNode(Node node) {
		logger.info("--- Rendering " + node.getPath() + " xxx " + node.getNodeType() + " is renderable? " + (node.getNodeType() instanceof RenderableNodeType));
		
		NodeType nodeType = node.getNodeType();
		
		if ((nodeType instanceof BinaryNodeType || nodeType instanceof RenderableNodeType)
				&& !(nodeType instanceof TemplateNodeType)) {
			FilePublisherURLMapper urlMapper = new FilePublisherURLMapper(craccess, node);
			ContentRenderRequestImpl request = new ContentRenderRequestImpl(cr, craccess, craccess.getNodesInPath(node.getPath()), urlMapper);
			FilePublisherRenderResponse response = new FilePublisherRenderResponse(outputbasedir, node);
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
