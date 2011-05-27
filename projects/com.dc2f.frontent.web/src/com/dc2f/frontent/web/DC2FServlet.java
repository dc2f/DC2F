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

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.ContentRepositoryFactory;
import com.dc2f.datastore.Node;
import com.dc2f.datastore.exception.DC2FRuntimeException;
import com.dc2f.datastore.filejson.SimpleFileContentRepository;
import com.dc2f.renderer.ContentRenderResponse;
import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.NodeRendererFactory;
import com.dc2f.renderer.impl.ContentRenderRequestImpl;

public class DC2FServlet implements Servlet {

	private ServletConfig config;
	
	private static final Logger logger = Logger.getLogger(DC2FServlet.class.getName());
	
	@Override
	public void destroy() {

	}

	@Override
	public ServletConfig getServletConfig() {
		return config;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void init(ServletConfig configuration) throws ServletException {
		config = configuration;

	}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		NodeRendererFactory factory = NodeRendererFactory.getInstance();
		NodeRenderer renderer = factory.getRenderer("com.dc2f.renderer.web");
		File crdir = new File(System.getProperty("crdir", "../../design/example"));
		if (crdir == null || !crdir.exists()) {
			System.out.println("Please specify a crdir :) ( -Dcrdir=xxx)");
		}
		ContentRepository cr = ContentRepositoryFactory.getInstance().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)crdir.getAbsolutePath()));
		//ContentRepository cr = new SimpleFileContentRepository(crdir);
		ServletURLMapper mapper = new ServletURLMapper(cr, getServletConfig());
		Node node = mapper.getNode(request);
		logger.info("We got a node: {" + node + "}");
		
		renderer.renderNode(new ContentRenderRequestImpl(cr, cr.getNodesInPath(node.getPath()), mapper),
					new ServletRenderResponse(response));
	}
	
	private class ServletRenderResponse implements ContentRenderResponse {

		private ServletResponse response;

		public ServletRenderResponse(ServletResponse response) {
			this.response = response;
		}

		@Override
		public OutputStream getOutputStream() {
			try {
				return response.getOutputStream();
			} catch (IOException e) {
				throw new DC2FRuntimeException("Error while getting output stream", e);
			}
		}

		@Override
		public Writer getWriter() {
			try {
				return response.getWriter();
			} catch (IOException e) {
				throw new DC2FRuntimeException("Error while getting writer", e);
			}
		}

		@Override
		public void setMimeType(String mimeType) {
			response.setContentType(mimeType);
		}
		
	}

}
