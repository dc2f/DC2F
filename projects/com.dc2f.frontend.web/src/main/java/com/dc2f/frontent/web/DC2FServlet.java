package com.dc2f.frontent.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collections;
import java.util.logging.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRSession;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.ContentRepositoryFactory;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.exception.DC2FRuntimeException;
import com.dc2f.renderer.ContentRenderResponse;
import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.NodeRendererFactory;
import com.dc2f.renderer.impl.ContentRenderRequestImpl;

public class DC2FServlet implements Servlet {

	private ServletConfig config;
	
	private static final Logger logger = Logger.getLogger(DC2FServlet.class.getName());
	
	public void destroy() {

	}

	public ServletConfig getServletConfig() {
		return config;
	}

	public String getServletInfo() {
		return null;
	}

	public void init(ServletConfig configuration) throws ServletException {
		config = configuration;

	}

	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		NodeRendererFactory factory = NodeRendererFactory.getInstance();
		NodeRenderer renderer = factory.getRenderer("com.dc2f.renderer.web");
		File crdir = new File(System.getProperty("crdir", "../../design/example"));
		if (crdir == null || !crdir.exists()) {
			System.out.println("Please specify a crdir :) ( -Dcrdir=xxx)");
		}
		ContentRepository cr = ContentRepositoryFactory.getInstance().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)crdir.getAbsolutePath()));
		CRSession conn = cr.authenticate(null);
		BranchAccess craccess = conn.openTransaction(null);
		//ContentRepository cr = new SimpleFileContentRepository(crdir);
		ServletURLMapper mapper = new ServletURLMapper(craccess, getServletConfig());
		Node node = mapper.getNode(request);
		logger.info("We got a node: {" + node + "}");
		long startTime = System.currentTimeMillis();
		
		renderer.renderNode(new ContentRenderRequestImpl(cr, craccess, craccess.getNodesInPath(node.getPath()), mapper),
					new ServletRenderResponse(response));
		long endTime = System.currentTimeMillis();
		logger.info("served request in " + (endTime-startTime) + "ms");
	}
	
	private class ServletRenderResponse implements ContentRenderResponse {

		private ServletResponse response;

		public ServletRenderResponse(ServletResponse response) {
			this.response = response;
		}

		public OutputStream getOutputStream() {
			try {
				return response.getOutputStream();
			} catch (IOException e) {
				throw new DC2FRuntimeException("Error while getting output stream", e);
			}
		}

		public Writer getWriter() {
			try {
				return response.getWriter();
			} catch (IOException e) {
				throw new DC2FRuntimeException("Error while getting writer", e);
			}
		}

		public void setMimeType(String mimeType) {
			response.setContentType(mimeType);
		}
		
	}

}
