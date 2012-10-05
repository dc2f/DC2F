package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.TextArea;

public class SourceEditor extends Editor {

	private TextArea editor;
	
	public SourceEditor(DC2FEditorProviderUIBinder dc2fEditorProviderUIBinder) {
		super(dc2fEditorProviderUIBinder);
		setName("Source");
		final DockPanel main = new DockPanel();
		editor = new TextArea();
		main.add(editor, DockPanel.CENTER);
		initWidget(main);
	}

	@Override
	public void loadNode(ContentNode node) {
		editor.setText(node.getSource());
		editor.addChangeHandler(getChangeHandler());
	}

}
