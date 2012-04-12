package com.dc2f.renderer.nodetype;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.nodetype.Date;
import com.dc2f.renderer.ContentRenderRequest;

public class DateRenderer extends BaseNodeType implements
		ContextRendererNodeType {

	@Override
	public String renderNode(Node configNode, ContentRenderRequest request,
			Node context, Object value) {
		//Object timestamp = ((Node)value).get("timestamp");
		Date date = (Date) ((Node)value).getNodeType();
		return String.valueOf(date.getDate((Node)value));
	}

}
