package com.dc2f.renderer.test;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

import com.dc2f.core.Node;
import com.dc2f.cr.ContentRepository;
import com.dc2f.cr.DummyFileContentRepository;
import com.dc2f.renderer.ContentRenderResponse;
import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.NodeRendererFactory;

public class RenderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeRendererFactory factory = NodeRendererFactory.getInstance();
		NodeRenderer renderer = factory.getRenderer("com.dc2f.renderer.web");
		
		String crdir = System.getProperty("crdir");
		if (crdir == null) {
			System.out.println("Please specify a crdir :) ( -Dcrdir=xxx)");
			System.exit(1);
		}
		ContentRepository cr = new DummyFileContentRepository(new File(crdir));
		
		Node node = cr.getNode("/cmsblog/articles/my-first-article");
		
		
		final Writer writer = new CharArrayWriter();
		final OutputStream stream = new ByteArrayOutputStream();
		
		renderer.renderNode(null, new ContentRenderResponse() {
			
			public Writer getWriter() {
				return writer;
			}
			
			public OutputStream getOutputStream() {
				return stream;
			}
		});
		
		System.out.println("We rendered something: " + writer.toString());
	}

}
