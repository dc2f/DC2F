package com.dc2f.backend.gwt.server;

import java.io.File;
import java.util.Collections;

import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRSession;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.filejson.JsonContentRepositoryProvider;

/**
 * wrapper around branch accesses.
 */
public final class DC2FAccessManager {
	/**
	 * readonly content repository access.
	 */
	private static BranchAccess readAccess;

	/**
	 * read/write content repository access.
	 */
	private static BranchAccess writeAccess;

	/**
	 * no instances allowed.
	 */
	private DC2FAccessManager() {
	}

	/**
	 * @return a content repository which can be used for reading.
	 */
	public static BranchAccess getAccess() {
		if (readAccess == null) {
			File crdir = getCrDirPath();
			ContentRepository cr = new JsonContentRepositoryProvider().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object) crdir.getAbsolutePath()));
			CRSession conn = cr.authenticate(null);
			readAccess = conn.openTransaction(null);
		}
		return readAccess;
	}

	/**
	 * trys to figure out the path to the content repository path. by default
	 * uses the example repository, except a system property crdir is given.
	 * @return path to a CR dir. (path is guaranteed to exist, but might not be a valid repository.)
	 */
	private static File getCrDirPath() {
		File crdir = null;
		String givenCrDir = System.getProperty("crdir");
		if (givenCrDir == null) {
			crdir = new File("../../../../design/src/main/resources/example");
			if (!crdir.exists()) {
				throw new IllegalArgumentException("Unable to find crdir. please set a 'crdir' system property to the path of a content repository.");
			}
		} else {
			crdir = new File(givenCrDir);
			if (!crdir.exists()) {
				throw new IllegalArgumentException("Invalid crdir given as system property {" + givenCrDir + "} {" + crdir.getAbsolutePath() + "}");
			}
		}
		return crdir;
	}

	/**
	 * @return writable content repository.
	 */
	public static BranchAccess getWriteAccess() {
		if (writeAccess == null) {
			File crdir = getCrDirPath();
			ContentRepository cr = new JsonContentRepositoryProvider().getContentRepository("writeablejsonfile", Collections.singletonMap("directory", (Object) crdir.getAbsolutePath()));
			CRSession conn = cr.authenticate(null);
			writeAccess = conn.openTransaction(null);
		}
		return writeAccess;
	}

}
