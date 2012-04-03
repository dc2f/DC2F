package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AttributeEditor extends Editor {

	ContentNode loadedNode;
	
	VerticalPanel attributeList = new VerticalPanel();
	
	public AttributeEditor(DC2FEditorProvider editorProvider) {
		super(editorProvider);
		setName("Attributes");
		final DockPanel main = new DockPanel();
		main.add(attributeList, DockPanel.CENTER);
		initWidget(main);
	}

	@Override
	public void loadNode(ContentNode node) {
		loadedNode = node;
		attributeList.clear();
		for(String attributeName : node.getAttributeNames()) {
			HorizontalPanel attributePanel = new HorizontalPanel();
			Label attributeLabel = new Label(attributeName);
			attributePanel.add(attributeLabel);
			TextBox attributeValue = new TextBox();
			attributeValue.setText(node.get(attributeName));
			attributeValue.addChangeHandler(getChangeHandler(attributeName));
			attributePanel.add(attributeValue);
			attributeList.add(attributePanel);
		}
	}
	
	@Override
	protected ChangeHandler getChangeHandler(final String attributeName) {
		final ChangeHandler handler = super.getChangeHandler(attributeName);
		return new ChangeHandler() {
			
			public void onChange(ChangeEvent event) {
				ValueBoxBase<?> widget = (ValueBoxBase<?>) event.getSource();
				String value = widget.getText();
				loadedNode.set(attributeName, value);
				handler.onChange(event);
			}
		};
	}
}
