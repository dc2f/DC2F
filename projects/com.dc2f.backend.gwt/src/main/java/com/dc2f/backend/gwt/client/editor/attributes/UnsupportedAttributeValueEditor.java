package com.dc2f.backend.gwt.client.editor.attributes;

import com.dc2f.backend.gwt.client.editor.NodeChangeHandler;
import com.dc2f.backend.gwt.shared.DTOAttributeType;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * value editor for unsupported attribute types.. simply displays the current value.
 * 
 * @author herbert
 */
public class UnsupportedAttributeValueEditor implements AttributeValueEditor {

	/**
	 * type for the attribute.
	 */
	private DTOAttributeType type;

	/**
	 * creates a new (unsupported) value editor for the given type.
	 * 
	 * @param type type of the attribute which we should display the value of.
	 */
	public UnsupportedAttributeValueEditor(final DTOAttributeType type) {
		this.type = type;
	}

	@Override
	public Widget getValueEditorWidget(final DTOEditableNode node, final String attributeName,
			final Object value, final NodeChangeHandler nodeChangeHandler) {
		Label label = new Label();
		label.setText(String.valueOf(value) + " (Unsupported type: {" + type.getName() + "})");
		return label;
	}

}
