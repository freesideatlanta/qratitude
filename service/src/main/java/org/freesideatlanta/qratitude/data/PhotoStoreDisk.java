package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;

public class PhotoStoreDisk implements PhotoStore {

	private static final int DEFAULT_BUFFER_SIZE = 4096;

	private int bufferSize;
	private String basePath;
	private String baseUrl;

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
		String filename = this.basePath + id + "." + extension;
		File file = new File(filename);
		FileOutputStream os = new FileOutputStream(file);

		byte[] buffer = new byte[this.bufferSize];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();

		String url = this.baseUrl + filename;
		URI uri = URI.create(url);

		return uri;
	}

	private String createId() {
		// TODO: generate a unique filename (id + extension)
		String id = "4048675309";
		return id;
	}
}

