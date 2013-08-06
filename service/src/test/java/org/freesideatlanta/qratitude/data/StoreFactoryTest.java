package org.freesideatlanta.qratitude.data;

import java.net.*;
import java.util.*;

import com.mongodb.*;
import org.bson.types.*;
import org.junit.*;
import static org.junit.Assert.*;

public class StoreFactoryTest {

	// TODO: independently fetch this information from the resource file directly
	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "qratitude_local";
	private static final String COLLECTION_CATEGORIES = "categories";
	private static final String COLLECTION_ASSETS = "assets";

	private static final String BASE_PATH = "/srv/qratitude/photos/";
	private static final String BASE_URL = "http://localhost:8080/qratitude-service/photos/";

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
		AssetStore as = StoreFactory.getAssetStore();
		assertTrue(as != null);

		AssetStoreMongo asm = (AssetStoreMongo)as;
		assertTrue(HOST.equals(asm.getHost()));
		assertTrue(PORT == asm.getPort());
		assertTrue(DATABASE.equals(asm.getDatabase()));
		assertTrue(COLLECTION_ASSETS.equals(asm.getName()));
	}

	@Test
	public void testGetPhotoStore() {
		PhotoStore ps = StoreFactory.getPhotoStore();
		assertTrue(ps != null);

		PhotoStoreDisk psd = (PhotoStoreDisk)ps;
		assertTrue(BASE_PATH.equals(psd.getBasePath()));
		assertTrue(BASE_URL.equals(psd.getBaseUrl()));

	}
}
