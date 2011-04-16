package com.dc2f.renderer;

import java.io.OutputStream;
import java.io.Writer;

import com.dc2f.renderer.url.URLMapper;

public interface ContentRenderResponse {
	public OutputStream getOutputStream();
	public Writer getWriter();
	public URLMapper getURLMapper();
}
