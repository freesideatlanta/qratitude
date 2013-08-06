package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;

import org.apache.log4j.*;

public class PhotoStoreDisk implements PhotoStore {
	private static Logger log = Logger.getLogger(PhotoStoreDisk.class);

	private static final int DEFAULT_BUFFER_SIZE = 4096;

	private int bufferSize;
	private String basePath;
	private String baseUrl;

	public String getBasePath() {
		return this.basePath;
	}
	public String getBaseUrl() {
		return this.baseUrl;
	}

	public PhotoStoreDisk(String basePath, String baseUrl) {
		this(basePath, baseUrl, DEFAULT_BUFFER_SIZE);
	}

	public PhotoStoreDisk(String basePath, String baseUrl, int bufferSize) {
		this.basePath = basePath;
		this.baseUrl = baseUrl;
		this.bufferSize = bufferSize;
	}

	@Override
	public URI create(InputStream is, String extension) throws IOException {
		String id = this.createId(); 
		String filename = id + "." + extension;
		log.debug("id: " + id);
		log.debug("filename: " + filename);
		String filepath = this.basePath + filename;
		File file = new File(filepath);
		FileOutputStream os = new FileOutputStream(file);

		byte[] buffer = new byte[this.bufferSize];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();

		String url = this.baseUrl + filename;
		log.debug("url: " + url);
		URI uri = URI.create(url);

		return uri;
	}

	private String createId() {
		// TODO: generate a unique filename (id + extension)
		String id = "4048675309";
		return id;
	}
}

