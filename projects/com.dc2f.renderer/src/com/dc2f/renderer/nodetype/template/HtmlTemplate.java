package com.dc2f.renderer.nodetype.template;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.nodetype.ContextRendererNodeType;

public class HtmlTemplate extends TemplateNodeType {
	private static final Logger logger = Logger.getLogger(HtmlTemplate.class
			.getName());

	@Override
	public String renderTemplate(ContentRenderRequest request, Node template) {
		Node node = request.getNode();
		String source = (String) template.getProperty("source");
		Node context = (Node) template.getProperty("context");

		// For now we simply look for all placeholders and then fetch them from
		// the context
		// it might better to prepare the context? or at least cache the results
		// of the context.

		Pattern placeholderLookup = Pattern.compile("\\$([A-Za-z0-9]+)");
		Matcher matcher = placeholderLookup.matcher(source);

		StringBuffer sb = new StringBuffer(source.length());
		while (matcher.find()) {
			String var = matcher.group(1);
			Node value = (Node) context.getProperty(var);

			String replacement = "";
			if (value != null) {
				if (value.getNodeType() instanceof ContextRendererNodeType) {
					
					String ref = (String) value.getProperty("ref");
					// FIXME we need to do some cool property lookup right here..
					Object renderValue = null;
					if (ref.startsWith(".@")) {
						renderValue = request.getNode().getProperty(ref.substring(2));
					}
					
					replacement = ((ContextRendererNodeType) value.getNodeType()).renderNode(
							request, renderValue);
					if (replacement == null) {
						replacement = "";
					}
				} else {
					replacement = "" + value.getNodeType();
				}
			} else {
				logger.severe("Unable to resolve context variable {" + var
						+ "}");
			}
			matcher.appendReplacement(sb, replacement);
		}
		matcher.appendTail(sb);

		return sb.toString();
	}

}
