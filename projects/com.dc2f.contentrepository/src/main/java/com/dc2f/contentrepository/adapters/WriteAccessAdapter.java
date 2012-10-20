package com.dc2f.contentrepository.adapters;

import com.dc2f.contentrepository.CRAdapter;
import com.dc2f.contentrepository.Node;

public interface WriteAccessAdapter extends CRAdapter {
	/**
	 * Save the given node into the repository.
	 * @param node - node to save into the repository
	 * @return <code>true</code> if saved successfully.
	 */
	boolean saveNode(Node node);
}
