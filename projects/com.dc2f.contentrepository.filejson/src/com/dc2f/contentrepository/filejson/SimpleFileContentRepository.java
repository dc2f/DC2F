package com.dc2f.contentrepository.filejson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.contentrepository.Authentication;
import com.dc2f.contentrepository.CRSession;
import com.dc2f.contentrepository.ContentRepository;

public class SimpleFileContentRepository implements ContentRepository {
	private static final Logger logger = Logger.getLogger(SimpleFileContentRepository.class.getName());
	private static final int BUFFER_SIZE = 1024;
	
	private static final Charset CHARSET = Charset.forName("UTF-8");

	private File crdir;

	public SimpleFileContentRepository(File crdir) throws FileNotFoundException {
		if (!crdir.exists() || !crdir.isDirectory()) {
			throw new FileNotFoundException(
					"Error while initiating file content repository - crdir is not a valid directory {"
							+ crdir.getAbsolutePath() + "}");
		}
		this.crdir = crdir;
	}
	
	protected File getCrdir() {
		return crdir;
	}
	
	protected String loadFile(InputStream inputStream) {
		try {
			InputStreamReader reader = new InputStreamReader(inputStream, CHARSET);
			StringBuilder builder = new StringBuilder();//(int) f.length());
			int c;
			for (char[] buf = new char[BUFFER_SIZE] ; (c = reader.read(buf, 0, BUFFER_SIZE)) > 0 ; ) {
				builder.append(buf, 0, c);
			}
			return builder.toString();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading from input stream.", e);
		}
		return null;
	}
	
	protected String loadRepositoryFile(File file) {
		try {
			return loadFile(new FileInputStream(new File(crdir, file.getPath())));
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Error while loading file {" + file.getAbsolutePath() + "}", e);
			return null;
		}
	}
	
	protected FileInputStream getInputStreamForRepositoryFile(File file) {
		try {
			return new FileInputStream(new File(crdir, file.getPath()));
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Error while loading file {" + file.getAbsolutePath() + "}", e);
			return null;
		}
	}
	
	protected JSONObject loadJSON(File f) {
		try {
			return loadJSON(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "Unable to find file for node {" + f.getAbsolutePath() + "}", e);
			return null;
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error while parsing file {" + f.getAbsolutePath() + "}", e);
			return null;
		}
	}
	
	protected JSONObject loadJSON(InputStream inputStream) throws JSONException {
		String str = loadFile(inputStream);
		if (str == null) {
			return null;
		}
		return new JSONObject(str);
	}
	
	protected JSONObject loadJSONFromPath(String path, String appendFileName) {
		if (path.startsWith("classpath:")) {
			// FIXME: umm.. do it the ugly way (for now?)
			if (appendFileName != null) {
				path = path + "/" + appendFileName;
			}
			path = path.substring("classpath:".length());
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			InputStream stream = this.getClass().getResourceAsStream(path);
			if (stream == null) {
				logger.severe("Unable to find resource in classpath: " + path + " === " + path.substring("classpath:".length()));
				return null;
			}
			try {
				return loadJSON(stream);
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "Error while parsing file {" + path + "}", e);
				return null;
			}
		}
		if (appendFileName != null) {
			return loadJSON(new File(new File(crdir, path), appendFileName));
		} else {
			return loadJSON(new File(crdir, path));
		}
	}


	@Override
	public CRSession authenticate(Authentication auth) {
		return new SimpleCRSession(this, auth);
	}
	
	

}
