package com.dc2f.contentrepository;

public interface CRSession {
	BranchAccess openTransaction(String branchName);
}
