package com.dc2f.backend.gwt.client.editor.attributes;

import com.dc2f.backend.gwt.client.editor.NodeChangeHandler;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * editor for string values.
 * 
 * @author herbert
 */
public class StringAttributeValueEditor extends AbstractTextAttributeValueEditor implements AttributeValueEditor {

	@Override
	public Widget getValueEditorWidget(final DTOEditableNode node, final String attributeName,
			final Object value, final NodeChangeHandler changeHandler) {
		TextBox attributeValue = new TextBox();
		prepareTextBox(node, attributeName, value, changeHandler, attributeValue);
		// attributeValue.addChangeHandler(getChangeHandler(attributeName));
		return attributeValue;
	}

}
