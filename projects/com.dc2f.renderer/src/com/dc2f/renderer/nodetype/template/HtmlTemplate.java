package com.dc2f.renderer.nodetype.template;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;

public class HtmlTemplate extends TemplateNodeType {
	private static final Logger logger = Logger.getLogger(HtmlTemplate.class
			.getName());

	@Override
	public String renderTemplate(ContentRenderRequest request, Node template) {
		String source = (String) template.get("source");
		Node context = (Node) template.get("context");

		// For now we simply look for all placeholders and then fetch them from
		// the context
		// it might better to prepare the context? or at least cache the results
		// of the context.

		Pattern placeholderLookup = Pattern.compile("\\$([A-Za-z0-9]+)");
		logger.info("rendering template with source {" + source + "}");
		Matcher matcher = placeholderLookup.matcher(source);

		StringBuffer sb = new StringBuffer(source.length());
		Context contextNodeType = (Context) context.getNodeType();
		while (matcher.find()) {
			String var = matcher.group(1);
			String replacement = String.valueOf(contextNodeType.resolveFromContext(context, request, var));
			matcher.appendReplacement(sb, replacement);
		}
		matcher.appendTail(sb);

		return sb.toString();
	}

}
