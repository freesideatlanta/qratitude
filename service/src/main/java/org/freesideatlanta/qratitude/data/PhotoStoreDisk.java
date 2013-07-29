package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;

public class PhotoStoreDisk implements PhotoStore {

	private static final int BUFFER_SIZE = 4096;
	// TODO: define the "base" path and url
	private static final String BASE_PATH = "/srv/qratitude/photos/";
	private static final String BASE_URL = "http://qratitude.freesideatlanta.org/photos/";

	@Override
	public URI create(InputStream is, String extension) throws IOException {
		String id = this.createId(); 
		String filename = BASE_PATH + id + "." + extension;
		File file = new File(filename);
		FileOutputStream os = new FileOutputStream(file);

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();

		String url = BASE_URL + filename;
		URI uri = URI.create(url);

		return uri;
	}

	private String createId() {
		// TODO: generate a unique filename (id + extension)
		String id = "4048675309";
		return id;
	}
}

