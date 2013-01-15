package com.dc2f.backend.gwt.client.editor.attributes;

import com.dc2f.backend.gwt.client.editor.NodeChangeHandler;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.user.client.ui.Widget;

/**
 * generic attribute value editor.
 * 
 * @author herbert
 */
public interface AttributeValueEditor {
	/**
	 * @param node node which is edited
	 * @param attributeName attribute name which is edited
	 * @param value the initial value of this attribute.
	 * @param nodeChangeHandler change handler which must be called if the value changes.
	 * @return a widget used to edit a attribute value.
	 */
	Widget getValueEditorWidget(final DTOEditableNode node, final String attributeName,
			final Object value, final NodeChangeHandler nodeChangeHandler);
}
