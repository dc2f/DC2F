package com.dc2f.datastore;

import java.util.Map;
import java.util.ServiceLoader;

public class ContentRepositoryFactory {
	private static ContentRepositoryFactory instance;
	private static ServiceLoader<ContentRepositoryProvider> providers = ServiceLoader.load(ContentRepositoryProvider.class);

	private ContentRepositoryFactory() {
	}
	
	public static ContentRepositoryFactory getInstance() {
		if (instance == null) {
			instance = new ContentRepositoryFactory();
		}
		return instance;
	}
	
	
	public ContentRepository getContentRepository(String contentRepositoryType, Map<String, Object> config) {
		// find the first content repository and return it ..
		for (ContentRepositoryProvider provider: providers) {
			ContentRepository cr = provider.getContentRepository(contentRepositoryType, config);
			if (cr != null) {
				return cr;
			}
		}
		return null;
	}

}
