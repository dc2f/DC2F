package com.dc2f.publish.files;

import java.io.File;
import java.util.Collections;
import java.util.logging.Logger;

import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRSession;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.ContentRepositoryFactory;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.NodeRendererFactory;

/**
 * publisher which will write out static files from a content repository.
 * 
 * @author herbert
 */
public class FilePublisher {
	Logger logger = Logger.getLogger(FilePublisher.class.getName());
	private BranchAccess craccess;
	
	public FilePublisher() {
	}
	
	public void run() {
		NodeRendererFactory factory = NodeRendererFactory.getInstance();
		NodeRenderer renderer = factory.getRenderer("com.dc2f.renderer.web");
		File crdir = new File(System.getProperty("crdir", "../design/example"));
		if (crdir == null || !crdir.exists()) {
			System.out.println("Please specify a crdir :) ( -Dcrdir=xxx) - not found: " + crdir.getAbsolutePath());
		}
		ContentRepository cr = ContentRepositoryFactory.getInstance().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)crdir.getAbsolutePath()));
		CRSession conn = cr.authenticate(null);
		this.craccess = conn.openTransaction(null);

		
		File outputdir = new File("out/publish");
		outputdir.mkdirs();
		
		String rootProjectPath = "/cmsblog";
		
		renderNodeRecursive(craccess.getNode(rootProjectPath));
	}
	
	protected void renderNodeRecursive(Node node) {
		logger.info("Rendering node " + node.getPath());
		
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
