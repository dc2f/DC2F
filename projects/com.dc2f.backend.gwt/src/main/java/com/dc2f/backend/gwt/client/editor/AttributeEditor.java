package com.dc2f.backend.gwt.client.editor;

import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * node editor which simply displays one input field for each attribute.
 */
public class AttributeEditor extends Editor {

	/**
	 * node which is currently edited.
	 */
	private DTOEditableNode loadedNode;

	/**
	 * panels which are used for editing.
	 */
	private VerticalPanel attributeList = new VerticalPanel();

	/**
	 * create a new attribute editor.
	 * @param dc2fEditorProviderUIBinder binder
	 */
	public AttributeEditor(final DC2FEditorProviderUIBinder dc2fEditorProviderUIBinder) {
		super(dc2fEditorProviderUIBinder);
		setName("Attributes");
		final DockPanel main = new DockPanel();
		main.getElement().setId("DC2FAttributeEditor");
		main.add(attributeList, DockPanel.CENTER);
		initWidget(main);
	}

	@Override
	public void loadNode(final DTOEditableNode node) {
		loadedNode = node;
		attributeList.clear();
		for (String attributeName : node.getNodeType().getAttributesDefinition().getAttributeNames()) {
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

			public void onChange(final ChangeEvent event) {
				ValueBoxBase<?> widget = (ValueBoxBase<?>) event.getSource();
				String value = widget.getText();
				loadedNode.set(attributeName, value);
				handler.onChange(event);
			}
		};
	}
}
