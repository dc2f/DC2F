package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.client.services.DC2FContentServiceAsync;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

/**
 * Base class for node editors.
 */
public abstract class Editor extends Composite {

	/**
	 * name of the editor.
	 */
	private String name;

	/**
	 * handler which should get notified for changes.
	 */
	private ChangeHandler changeHandler = new NodeChangedChangeHandler();

	/**
	 * @return service to retrieve additional nodes.
	 */
	public DC2FContentServiceAsync getContentService() {
		return editorProvider.getContentService();
	}

	/**
	 * editor provider which is used to show the editor in the GUI.
	 */
	private DC2FEditorProviderUIBinder editorProvider;

	/**
	 * initialize the new editor.
	 * 
	 * @param dc2fEditorProviderUIBinder - editor provider to use when the editor is shown
	 */
	public Editor(final DC2FEditorProviderUIBinder dc2fEditorProviderUIBinder) {
		editorProvider = dc2fEditorProviderUIBinder;
	}

	/**
	 * @return name of the editor.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the editor, this should be called in the constructor.
	 * 
	 * @param name - name of the editor
	 */
	protected void setName(final String name) {
		this.name = name;
	}

	/**
	 * register this editor to open when the button is clicked.
	 * 
	 * @param button - button to open the editor
	 */
	public void loadEditorOnButtonClick(final Button button) {
		button.addClickHandler(new ClickHandler() {

			public void onClick(final ClickEvent event) {
				loadNode(editorProvider.getActualNode());
				editorProvider.showEditor(getEditor());
			}
		});
	}

	/**
	 * helper method to pass the editor to the onClick handler.
	 * 
	 * @return the editor itself.
	 */
	private Editor getEditor() {
		return this;
	}

	/**
	 * Method is called when the editor is activated and before the editor is loaded into the GUI.
	 * 
	 * @param node - editable {@link DTOEditableNode}.
	 */
	public abstract void loadNode(DTOEditableNode node);

	/**
	 * get a change handler for the given attribute.
	 * 
	 * @param attributeName - name of the attribute to get the change handler for
	 * @return change handler for this attribute
	 */
	protected ChangeHandler getChangeHandler(final String attributeName) {
		return getChangeHandler();
	}

	/**
	 * @return change handler to use when anything in the node has changed. if you know the specific changed attribute better use {@link #getChangeHandler(String)}.
	 */
	protected ChangeHandler getChangeHandler() {
		return changeHandler;
	}

	/**
	 * determines if the editor is suitable for the given node.
	 * 
	 * @param actualNode the node to check usability.
	 * @return true if this editor can be used for the given node.
	 */
	public boolean isUsableFor(final DTOEditableNode actualNode) {
		// TODO add proper check for using this editor
		return true;
	}

	/**
	 * handler which will receive change notifications.
	 */
	protected class NodeChangedChangeHandler implements ChangeHandler {

		/** {@inheritDoc} */
		public void onChange(final ChangeEvent event) {
			editorProvider.nodeHasChanged(event);
		}
	}

}
