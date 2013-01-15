package com.dc2f.backend.gwt.client.editor.attributes;

import com.dc2f.backend.gwt.client.editor.NodeChangeHandler;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * clob editor.
 * 
 * @author herbert
 */
public class ClobAttributeValueEditor extends AbstractTextAttributeValueEditor {

	@Override
	public Widget getValueEditorWidget(final DTOEditableNode node, final String attributeName,
			final Object value, final NodeChangeHandler nodeChangeHandler) {
		TextArea textArea = new TextArea();
		prepareTextBox(node, attributeName, value, nodeChangeHandler, textArea);
		return textArea;
	}

}
