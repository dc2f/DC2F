package com.dc2f.contentrepository;

public interface WritableContentRepository extends ContentRepository {

	/**
	 * Write a node to the ContentRepository
	 * @param node - {@link Node} to write into the ContentRepository
	 * @throws ContentRepositoryException in case the node cannot be written into the transaction
	 */
	void saveNode(ChangeableNode node) throws ContentRepositoryException;
	
	/**
	 * execute a rollback of the transaction so nothing is actually written to the filesystem.
	 */
	void rollback();
}
