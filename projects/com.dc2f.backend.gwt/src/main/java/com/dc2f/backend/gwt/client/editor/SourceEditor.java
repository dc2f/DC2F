package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.client.services.DC2FContentServiceAsync;
import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
		DC2FContentServiceAsync contentService = getContentService();
		contentService.getSource(node, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				editor.setText("Failed to load source for Node.");
			}

			public void onSuccess(String result) {
				editor.setText(result);
			}
		});
		editor.addChangeHandler(getChangeHandler());
	}

}
