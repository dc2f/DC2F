package com.dc2f.datastore.filejson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.ContentRepositoryProvider;

public class SimpleJsonContentRepositoryProvider implements
		ContentRepositoryProvider {

	@Override
	public ContentRepository getContentRepository(String contentRepositoryType,
			Map<String, Object> config) {
		if (!"simplejsonfile".equals(contentRepositoryType)) {
			return null;
		}
		try {
			
			return new SimpleFileContentRepository(new File((String) config.get("directory")));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error while instantiating content repository", e);
		}
	}

}
