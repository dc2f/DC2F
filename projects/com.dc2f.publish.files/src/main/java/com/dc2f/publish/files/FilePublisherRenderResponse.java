package com.dc2f.publish.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderResponse;

public class FilePublisherRenderResponse implements ContentRenderResponse {
	static Logger logger = Logger.getLogger(FilePublisherRenderResponse.class.getName());

	private File outputbasedir;
	
	private static final String MIME_TYPE_HTML = "text/html";
	
	private static final Map<String, String> mimeTypeExtensionMapping = new HashMap<String, String>();
	
	static {
		mimeTypeExtensionMapping.put(MIME_TYPE_HTML, ".html");
		mimeTypeExtensionMapping.put("image/png", ".png");
		mimeTypeExtensionMapping.put("image/jpeg", ".jpg");
		mimeTypeExtensionMapping.put("image/jpg", ".jpg");
		mimeTypeExtensionMapping.put("image/gif", ".gif");
		mimeTypeExtensionMapping.put("text/css", ".css");
	}
	
	/**
	 * by default use HTML.
	 */
	private String mimeType = MIME_TYPE_HTML;
	
	
	OutputStream outputStream = null;
	Writer writer = null;

	private FilePublisherURLMapper urlMapper;

	private Node node;
	
	

	public FilePublisherRenderResponse(File outputbasedir, FilePublisherURLMapper urlMapper, Node node) {
		this.outputbasedir = outputbasedir;
		this.urlMapper = urlMapper;
		this.node = node;
	}
	
	public static String getFileExtensionForMimeType(String mimeType) {
		String fileExtension = null;
		// FIXME we need a more sophisticated mime type -> extension mapping?!
		fileExtension = mimeTypeExtensionMapping.get(mimeType);
		if (fileExtension == null) {
			fileExtension = ".unknown";
		}
		return fileExtension;
	}
	
	protected File getOutputFile() {
//		String fileExtension = FilePublisherRenderResponse.getFileExtensionForMimeType(mimeType);
		File f = new File(outputbasedir, urlMapper.getRenderPath(node));
		f.getParentFile().mkdirs();
		logger.finer("using file " + f.getAbsolutePath());
		return f;
	}

	public OutputStream getOutputStream() {
		if (outputStream == null) {
			if (writer != null) {
				throw new IllegalStateException("Requesting output stream, although writer was already requested.");
			}
			try {
				outputStream = new FileOutputStream(getOutputFile());
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Errorw hile opening output stream.", e);
			}
		}
		return outputStream;
	}

	public Writer getWriter() {
		if (writer == null) {
			if (outputStream != null) {
				throw new IllegalStateException("Requesting from writer, although an output stream was already requested.");
			}
			try {
				// FIXME encoding!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				writer = new FileWriter(getOutputFile());
			} catch (IOException e) {
				throw new RuntimeException("Error while opening file writer.", e);
			}
		}
		return writer;
	}
	
	public boolean hadOutput() {
		return (writer != null || outputStream != null);
	}
	
	public void close() throws IOException {
		if (writer != null) {
			writer.close();
		}
		if (outputStream != null) {
			outputStream.close();
		}
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
