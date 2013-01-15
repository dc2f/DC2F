package com.dc2f.backend.gwt.client.editor.attributes;

import com.dc2f.backend.gwt.client.editor.NodeChangeHandler;
import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * base class for text and clob editors.
 * @author herbert
 */
public abstract class AbstractTextAttributeValueEditor implements AttributeValueEditor {

	/**
	 * prepares the text box.
	 * @param node node to be edited.
	 * @param attributeName attribute which is edited.
	 * @param value value of the node of the attribute.
	 * @param changeHandler change handler which was passed by the caller
	 * @param attributeValue actual instance which is used for editing.
	 */
	protected void prepareTextBox(final DTOEditableNode node, final String attributeName, final Object value, final NodeChangeHandler changeHandler, final TextBoxBase attributeValue) {
		if (value == null) {
			attributeValue.setText("");
		} else {
			attributeValue.setText(String.valueOf(value));
		}
		attributeValue.addChangeHandler(new ChangeHandler() {
	
			@Override
			public void onChange(final ChangeEvent event) {
				ValueBoxBase<?> widget = (ValueBoxBase<?>) event.getSource();
				String value = widget.getText();
				node.set(attributeName, value);
				changeHandler.onChange();
			}
		});
	}

}
