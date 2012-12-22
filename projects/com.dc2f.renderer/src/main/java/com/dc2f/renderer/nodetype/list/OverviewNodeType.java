package com.dc2f.renderer.nodetype.list;

import java.util.Arrays;
import java.util.Comparator;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.impl.ContentRenderRequestImpl;
import com.dc2f.renderer.impl.TemplateRenderer;
import com.dc2f.renderer.nodetype.ContextRendererNodeType;

public class OverviewNodeType extends BaseNodeType implements
		ContextRendererNodeType {

	public String renderNode(Node configNode, ContentRenderRequest request,
			Node context, Object value) {
		if (value instanceof Node) {
			String acceptedVariantsStr = (String) configNode.get("acceptedVariants");
			String acceptedVariants[] = null;
			if (acceptedVariantsStr != null) {
				acceptedVariants = acceptedVariantsStr.split(",");
			}
			Node[] children = request.getContentRepositoryTransaction().getChildren((Node) value);
			String sortby = (String) configNode.get("sortBy");
			if (sortby != null) {
				Arrays.sort(children, new NodeComparator(sortby));
			}
			StringBuffer buf = new StringBuffer();
			for (Node child : children) {
				ContentRenderRequestImpl req = new ContentRenderRequestImpl(request.getContentRepository(), request.getContentRepositoryTransaction(), new Node[]{child}, request.getURLMapper());
				req.setParentContentRenderRequest(request);
				req.setProjectNode(request.getProjectNode());
				buf.append(TemplateRenderer.internalRenderNode(req, null, "com.dc2f.rendertype.web.overview", acceptedVariants));
			}
			return buf.toString();
		}
		
		return "I think we have to render an overview :/ for " + context + " - " + value;
//		return null;
	}
	
	
	public static final class NodeComparator implements Comparator<Node> {
		private String cmpattribute;

		public NodeComparator(String cmpattribute) {
			this.cmpattribute = cmpattribute;
		}

		@SuppressWarnings("unchecked")
		@Override
		public int compare(Node o1, Node o2) {
			Object tmp1 = o1.get(cmpattribute);
			Object tmp2 = o2.get(cmpattribute);
			
			if (tmp1 instanceof Comparable<?> && tmp2 instanceof Comparable<?>) {
				return ((Comparable<Object>)tmp1).compareTo(tmp2);
			}
			return 0;
		}
	}

}
