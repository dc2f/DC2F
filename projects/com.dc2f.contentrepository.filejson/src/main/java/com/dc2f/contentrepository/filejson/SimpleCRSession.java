package com.dc2f.contentrepository.filejson;

import com.dc2f.contentrepository.Authentication;
import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRSession;

public class SimpleCRSession implements CRSession {
	

	private SimpleFileContentRepository cr;
	private Authentication auth;

	public SimpleCRSession(
			SimpleFileContentRepository simpleFileContentRepository,
			Authentication auth) {
		this.cr = simpleFileContentRepository;
		this.auth = auth;
	}

	@Override
	public BranchAccess openTransaction(String branchName) {
		return new SimpleBranchAccess(this, branchName);
	}

	public SimpleFileContentRepository getContentRepository() {
		return cr;
	}

}
