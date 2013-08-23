package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.lang.*;
import java.util.*;

import org.apache.log4j.*;

public class StoreFactory {
	private static Logger log = Logger.getLogger(StoreFactory.class);

	private static final String DATA_PROPERTIES = "data.properties";
	private static final String DATA_HOST = "data.host";
	private static final String DATA_PORT = "data.port";
	private static final String DATA_DATABASE = "data.database";
	private static final String DATA_COLLECTION_ASSETS = "data.collection.assets";
	private static final String DATA_COLLECTION_CATEGORIES = "data.collection.categories";
	private static final String DATA_COLLECTION_USERS = "data.collection.users";
	private static final String DATA_PHOTO_BUFFER_SIZE = "data.photo.buffer_size";
	private static final String DATA_PHOTO_BASE_PATH = "data.photo.base_path";
	private static final String DATA_PHOTO_BASE_URL = "data.photo.base_url";

	public static CategoryStore getCategoryStore() {
		CategoryStoreMongo cs = null;

		try {
			Properties properties = loadDataProperties();
			String host = properties.getProperty(DATA_HOST);
			String value = properties.getProperty(DATA_PORT);
			int port = Integer.parseInt(value);
			String database = properties.getProperty(DATA_DATABASE);
			String collection = properties.getProperty(DATA_COLLECTION_CATEGORIES);
			
			cs = new CategoryStoreMongo(host, port, database, collection);
			cs.initialize();
		} catch (NumberFormatException e) {
			// TODO: handle exception better
		} catch (IOException e) {
			// TODO: handle exception better
		}
		
		return cs;
	}

	public static AssetStore getAssetStore() {
		AssetStoreMongo as = null;

		try {
			Properties properties = loadDataProperties();
			String host = properties.getProperty(DATA_HOST);
			String value = properties.getProperty(DATA_PORT);
			int port = Integer.parseInt(value);
			String database = properties.getProperty(DATA_DATABASE);
			String collection = properties.getProperty(DATA_COLLECTION_ASSETS);
			
			as = new AssetStoreMongo(host, port, database, collection);
			as.initialize();
		} catch (NumberFormatException e) {
			log.debug(e);
			// TODO: handle exception better
		} catch (IOException e) {
			log.debug(e);
			// TODO: handle exception better
		}

		return as;
	}

	public static UserStore getUserStore() {
		UserStoreMongo us = null;

		try {
			Properties properties = loadDataProperties();
			String host = properties.getProperty(DATA_HOST);
			String value = properties.getProperty(DATA_PORT);
			int port = Integer.parseInt(value);
			String database = properties.getProperty(DATA_DATABASE);
			String collection = properties.getProperty(DATA_COLLECTION_USERS);
			
			us = new UserStoreMongo(host, port, database, collection);
			us.initialize();
		} catch (NumberFormatException e) {
			log.debug(e);
			// TODO: handle exception better
		} catch (IOException e) {
			log.debug(e);
			// TODO: handle exception better
		}

		return us;
	}

	public static PhotoStore getPhotoStore() {
		PhotoStoreDisk ps = null;

		try {
			Properties properties = loadDataProperties();
			String basePath = properties.getProperty(DATA_PHOTO_BASE_PATH);
			String baseUrl = properties.getProperty(DATA_PHOTO_BASE_URL);
			String value = properties.getProperty(DATA_PHOTO_BUFFER_SIZE);
			int bufferSize = Integer.parseInt(value);

			ps = new PhotoStoreDisk(basePath, baseUrl, bufferSize);
		} catch (NumberFormatException e) {
			log.debug(e);
			// TODO: handle exception better
		} catch (IOException e) {
			log.debug(e);
			// TODO: handle exception better
		}
		
		return ps;
	}

	private static Properties loadDataProperties() throws IOException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		InputStream is = cl.getResourceAsStream(DATA_PROPERTIES);
		properties.load(is);

		return properties;
	}
}
