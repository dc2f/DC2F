package com.dc2f.backend.gwt.client.cell;

import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * cell which knows how to render a node in the tree.
 * 
 * @author herbert
 */
public class NodeCell extends AbstractCell<DTONodeInfo> {

	/**
	 * The HTML templates used to render the cell.
	 */
	interface Templates extends SafeHtmlTemplates {
		/**
		 * The template for this Cell, which includes styles and a value.
		 * 
		 * @param styles the styles to include in the style attribute of the div
		 * @param value the safe value. Since the value type is {@link SafeHtml}, it will not be escaped before including it in the template. Alternatively, you could make the value type String, in
		 *            which case the value would be escaped.
		 * @return a {@link SafeHtml} instance
		 */
		@SafeHtmlTemplates.Template("<div style=\"{0}\">{1}</div>")
		SafeHtml cell(SafeStyles styles, SafeHtml value);
	}

	/**
	 * Create a singleton instance of the templates used to render the cell.
	 */
	private static Templates templates = GWT.create(Templates.class);

	/** **/
	public NodeCell() {
	}

	@Override
	public void render(final com.google.gwt.cell.client.Cell.Context context, final DTONodeInfo value, final SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}

		// If the value comes from the user, we escape it to avoid XSS attacks.
		SafeHtml safeValue = SafeHtmlUtils.fromString(value.getName());

		// Use the template to create the Cell's html.
		SafeStyles styles = SafeStylesUtils.forTrustedColor(safeValue.asString());
		SafeHtml rendered = templates.cell(styles, safeValue);
		sb.append(rendered);
	}

}
