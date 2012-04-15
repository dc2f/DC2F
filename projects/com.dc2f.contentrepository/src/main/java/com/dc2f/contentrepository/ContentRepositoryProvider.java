package com.dc2f.contentrepository;

import java.util.Map;

/**
 * A public interface which can be implemented by provider for renderers.
 * Classes should be registered in a META-INF/services file.
 * 
 * @author herbert
 * @see http
 *      ://download.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html
 */
public interface ContentRepositoryProvider {
	/**
	 * Should return a ContentRepository for the given contentRepositoryType with the given config
	 * 
	 * @param contentRepositoryType
	 * @param config - implementation (CR type) specific configuration
	 * @return a stateless ContentRepository
	 */
	public ContentRepository getContentRepository(String contentRepositoryType,
			Map<String, Object> config);
}
