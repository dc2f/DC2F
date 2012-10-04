package com.dc2f.renderer;

import java.io.OutputStream;
import java.io.Writer;

public interface ContentRenderResponse {
	public OutputStream getOutputStream();
	public Writer getWriter();
	public void setMimeType(String mimeType);
}
