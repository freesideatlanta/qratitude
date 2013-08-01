package org.freesideatlanta.qratitude.service;

import java.net.*;
import java.util.*;

import com.mongodb.*;
import org.bson.types.*;
import org.junit.*;
import static org.junit.Assert.*;

import org.freesideatlanta.qratitude.data.*;

public class StoreFactoryTest {

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "qratitude_local";
	private static final String COLLECTION_CATEGORIES = "categories";
	private static final String COLLECTION_ASSETS = "assets";

	private MongoClient client;
	private DB db;
	private DBCollection collection;

	@Before
	public void before() {
		try {
			this.client = new MongoClient(HOST, PORT);
			this.db = this.client.getDB(DATABASE);
		} catch (UnknownHostException e) {
			fail(e.toString());
		}
	}	

	@After
	public void after() {
	}

	@Test
	public void testGetCategoryStore() {
		CategoryStore cs = StoreFactory.getCategoryStore();
		assertTrue(cs != null);

		CategoryStoreMongo csm = (CategoryStoreMongo)cs;
		assertTrue(HOST.equals(csm.getHost()));
		assertTrue(PORT == csm.getPort());
		assertTrue(DATABASE.equals(csm.getDatabase()));
		assertTrue(COLLECTION_CATEGORIES.equals(csm.getName()));
	}

	@Test
	public void testGetAssetStore() {
		AssetStore cs = StoreFactory.getAssetStore();
		assertTrue(cs != null);

		AssetStoreMongo csm = (AssetStoreMongo)cs;
		assertTrue(HOST.equals(csm.getHost()));
		assertTrue(PORT == csm.getPort());
		assertTrue(DATABASE.equals(csm.getDatabase()));
		assertTrue(COLLECTION_ASSETS.equals(csm.getName()));
	}

	@Test
	public void testGetPhotoStore() {
		// TODO: testing disk noise!
	}

}
