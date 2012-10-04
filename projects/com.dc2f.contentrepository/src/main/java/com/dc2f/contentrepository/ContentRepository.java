package com.dc2f.contentrepository;




public interface ContentRepository {

	/**
	 * returns a CR session for the given authenticated user. pass in null for an anonymous session.
	 */
	CRSession authenticate(Authentication auth);
	
}
