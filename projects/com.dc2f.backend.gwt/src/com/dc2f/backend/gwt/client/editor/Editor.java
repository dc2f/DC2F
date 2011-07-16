package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

public abstract class Editor extends Composite {

	/**
	 * name of the editor
	 */
	protected String name;
	
	private DC2FEditorProvider editorProvider;

	public Editor(DC2FEditorProvider dc2fEditorProvider) {
		editorProvider = dc2fEditorProvider;
	}
	
	/**
	 * @return name of the editor
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the editor, this should be called in the constructor.
	 * @param name - name of the editor
	 */
	protected void setName(String name) {
		this.name = name;
	}

	public void bindToButton(Button button) {
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loadNode(editorProvider.getActualNode());
				editorProvider.showEditor(getEditor());
			}
		});
	}
	
	/**
	 * helper method to pass the editor to the onClick handler. 
	 * @return the editor itself.
	 */
	private Editor getEditor() {
		return this;
	}

	/**
	 * Method is called when the editor is activated and before the editor is loaded into the GUI.
	 * @param node - editable {@link ContentNode}.
	 */
	public abstract void loadNode(ContentNode node);
	
	protected ChangeHandler getChangeHandler(String attributeName) {
		return new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				editorProvider.nodeHasChanged(event);
				
			}
		};
	}

}
