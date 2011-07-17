package com.dc2f.contentrepository.filejson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.ContentRepositoryProvider;

public class JsonContentRepositoryProvider implements
		ContentRepositoryProvider {

	@Override
	public ContentRepository getContentRepository(String contentRepositoryType,
			Map<String, Object> config) {
		try {
			if ("simplejsonfile".equals(contentRepositoryType)) {
				return new SimpleFileContentRepository(new File((String) config.get("directory")));
			} else if("writeablejsonfile".equals(contentRepositoryType)) {
				return new WritableFileContentRepository(new File((String) config.get("directory")));
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error while instantiating content repository", e);
		}
		return null;
	}

}
