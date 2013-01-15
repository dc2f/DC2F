package com.dc2f.backend.gwt.client.editor;

/**
 * interface which is used to notify editor of changes to the node.
 * @author herbert
 */
public interface NodeChangeHandler {

	/**
	 * must be called when attributes or values change.
	 **/
	void onChange();

}
