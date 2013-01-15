package com.dc2f.backend.gwt.client.editor;

import java.util.HashMap;
import java.util.Map;

import com.dc2f.backend.gwt.client.editor.attributes.AttributeValueEditor;
import com.dc2f.backend.gwt.client.editor.attributes.ClobAttributeValueEditor;
import com.dc2f.backend.gwt.client.editor.attributes.StringAttributeValueEditor;
import com.dc2f.backend.gwt.client.editor.attributes.UnsupportedAttributeValueEditor;
import com.dc2f.backend.gwt.shared.DTOAttributeDefinition;
import com.dc2f.backend.gwt.shared.DTOAttributeType;
import com.dc2f.backend.gwt.shared.DTOAttributesDefinition;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * node editor which simply displays one input field for each attribute.
 */
public class AttributeEditor extends Editor {

	/**
	 * node which is currently edited.
	 */
	@SuppressWarnings("unused")
	private DTOEditableNode loadedNode;

	/**
	 * panels which are used for editing.
	 */
	private VerticalPanel attributeList = new VerticalPanel();
	
	/**
	 * editors for the default attribute types.
	 */
	private Map<DTOAttributeType, AttributeValueEditor> defaultEditors = new HashMap<DTOAttributeType, AttributeValueEditor>();

	/**
	 * create a new attribute editor.
	 * 
	 * @param dc2fEditorProviderUIBinder binder
	 */
	public AttributeEditor(final DC2FEditorProviderUIBinder dc2fEditorProviderUIBinder) {
		super(dc2fEditorProviderUIBinder);
		setupDefaultEditors();
		setName("Attributes");
		final DockPanel main = new DockPanel();
		main.getElement().setId("DC2FAttributeEditor");
		main.add(attributeList, DockPanel.CENTER);
		initWidget(main);
	}

	/**
	 * configures the default editors.
	 */
	private void setupDefaultEditors() {
		defaultEditors.clear();
		defaultEditors.put(DTOAttributeType.STRING, new StringAttributeValueEditor());
		defaultEditors.put(DTOAttributeType.CLOB, new ClobAttributeValueEditor());
		
		for (DTOAttributeType type : DTOAttributeType.values()) {
			if (!defaultEditors.containsKey(type)) {
				defaultEditors.put(type, new UnsupportedAttributeValueEditor(type));
			}
		}
	}

	@Override
	public void loadNode(final DTOEditableNode node) {
		loadedNode = node;
		attributeList.clear();
		DTOAttributesDefinition attrsDef = node.getNodeType().getAttributesDefinition();
		for (String attributeName : attrsDef.getAttributeNames()) {
			DTOAttributeDefinition attrDef = attrsDef.getAttributeDefinition(attributeName);
			DTOAttributeType type = attrDef.getAttributeType();
			
			AttributeValueEditor editor = defaultEditors.get(type);
			if (editor == null) {
				// no editor defined.. this shouldn't actually happen.
				continue;
			}
			Widget attributeValue = editor.getValueEditorWidget(node, attributeName, node.get(attributeName), getChangeHandler(attributeName));

			if (attributeValue == null) {
				// no widget was returned for editor? ignore attribute.
				continue;
			}

			HorizontalPanel attributePanel = new HorizontalPanel();
			Label attributeLabel = new Label(attributeName);
			attributePanel.add(attributeLabel);
			
			
			attributePanel.add(attributeValue);
			attributeList.add(attributePanel);
		}
	}
}
