package com.dc2f.nodetype;

import java.io.InputStream;

import com.dc2f.contentrepository.Node;

/**
 * Interface of all node types which might contain some binary data.
 * 
 * @author herbert
 */
public interface BinaryNodeType {
	public String getMimeType(Node node);
	
	public InputStream getInputStream(Node node);
}
