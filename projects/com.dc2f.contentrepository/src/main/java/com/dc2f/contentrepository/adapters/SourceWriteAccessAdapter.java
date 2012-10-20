package com.dc2f.contentrepository.adapters;

import com.dc2f.contentrepository.CRAdapter;
import com.dc2f.contentrepository.Node;

/**
 * This adapter allows you to overwrite the source of a Node with a string. Be carefull.
 * @author bigbear3001
 *
 */
public interface SourceWriteAccessAdapter extends CRAdapter {
	/**
	 * Overwrite the given node with the given source
	 * @param node - node to overwrite
	 * @param source - source to overwrite the node with
	 * @return <code>true</code> if saving was successful.
	 */
	boolean saveNode(Node node, String source);
}
