package com.dc2f.contentrepository.adapters;

import com.dc2f.contentrepository.CRAdapter;
import com.dc2f.contentrepository.Node;

/**
 * Adapter which allows to search through a contentrepository by an xpath expression.
 * 
 * @author herbert
 */
public interface XPathSearchAdapter extends CRAdapter {
	Node[] search(String xpathExpression, String[] sortOrder, int offset, int limit);
}
