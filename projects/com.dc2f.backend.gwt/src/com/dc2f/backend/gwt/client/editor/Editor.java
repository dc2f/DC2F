package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

public abstract class Editor extends Composite {

	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void bindToButton(Button button, final DC2FEditorProvider editorProvider) {
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loadNode(editorProvider.getActualNode());
				editorProvider.showEditor(getEditor());
			}
		});
	}
	
	protected Editor getEditor() {
		return this;
	}

	public abstract void loadNode(ContentNode node);

}
