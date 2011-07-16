package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.shared.ContentNode;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AttributeEditor extends Editor {

	VerticalPanel attributeList = new VerticalPanel();
	
	public AttributeEditor() {
		setName("Attribute Editor");
		final DockPanel main = new DockPanel();
		main.add(attributeList, DockPanel.CENTER);
		initWidget(main);
	}

	@Override
	public void loadNode(ContentNode node) {
		// TODO Auto-generated method stub
//		attributeList.clear();
		for(String attributeName : node.getAttributeNames()) {
			HorizontalPanel attributePanel = new HorizontalPanel();
			Label attributeLabel = new Label(attributeName);
			attributePanel.add(attributeLabel);
			TextBox attributeValue = new TextBox();
			attributeValue.setText(node.get(attributeName));
			attributePanel.add(attributeValue);
			attributeList.add(attributePanel);
		}
	}
}
