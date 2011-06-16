package com.dc2f.backend.gwt.server;

import java.io.File;
import java.util.Collections;

import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRSession;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.filejson.SimpleJsonContentRepositoryProvider;

public class DC2FAccessManager {

	static BranchAccess crAccess;
	
	public static BranchAccess getAccess() {
		if (crAccess == null) {
			//NodeRendererFactory factory = NodeRendererFactory.getInstance();
			//NodeRenderer renderer = factory.getRenderer("com.dc2f.renderer.web");
			File crdir = new File(System.getProperty("crdir", "/Users/bigbear3001/Documents/dc2f/design/example"));
			//ContentRepository cr = ContentRepositoryFactory.getInstance().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)crdir.getAbsolutePath()));
			ContentRepository cr = new SimpleJsonContentRepositoryProvider().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)crdir.getAbsolutePath()));
			CRSession conn = cr.authenticate(null);
			crAccess = conn.openTransaction(null);
		}
		return crAccess;
	}

}
