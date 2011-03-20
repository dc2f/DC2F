package com.dc2f.renderer.test;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.datastore.impl.simple.SimpleFileContentRepository;
import com.dc2f.renderer.ContentRenderResponse;
import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.NodeRendererFactory;
import com.dc2f.renderer.impl.ContentRenderRequestImpl;

public class RenderTest {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		NodeRendererFactory factory = NodeRendererFactory.getInstance();
		NodeRenderer renderer = factory.getRenderer("com.dc2f.renderer.web");
		
		File crdir = new File(System.getProperty("crdir", "../../design/example"));
		if (crdir == null || !crdir.exists()) {
			System.out.println("Please specify a crdir :) ( -Dcrdir=xxx)");
			System.exit(1);
		}
		ContentRepository cr = new SimpleFileContentRepository(crdir);
		
		Node node = cr.getNode("/cmsblog/articles/my-first-article");
		
		
		final Writer writer = new CharArrayWriter();
		final OutputStream stream = new ByteArrayOutputStream();
		
		renderer.renderNode(new ContentRenderRequestImpl(node), new ContentRenderResponse() {
			
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
