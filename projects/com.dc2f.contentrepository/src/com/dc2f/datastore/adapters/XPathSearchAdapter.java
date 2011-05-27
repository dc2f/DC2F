package com.dc2f.datastore.adapters;

import com.dc2f.datastore.CRAdapter;
import com.dc2f.datastore.Node;

/**
 * Adapter which allows to search through a contentrepository by an xpath expression.
 * 
 * @author herbert
 */
public interface XPathSearchAdapter extends CRAdapter {
	Node[] search(String xpathExpression, String[] sortOrder, int offset, int limit);
}
