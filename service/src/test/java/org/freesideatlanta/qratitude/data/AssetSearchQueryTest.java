package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;
import java.util.*;

import com.mongodb.*;
import org.apache.log4j.*;
import org.bson.types.*;
import org.junit.*;
import static org.junit.Assert.*;

import org.freesideatlanta.qratitude.model.*;

public class AssetSearchQueryTest {
	private static Logger log = Logger.getLogger(AssetSearchQueryTest.class);

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "qratitude_local";
	private static final String NAME = "assets";

	private static MongoClient client;
	private static DB db;
	private static DBCollection collection;

	private Asset testAsset;
	private AssetStore store;

	@BeforeClass
	public static void beforeClass() {
		try {
			client = new MongoClient(HOST, PORT);
			db = client.getDB(DATABASE);
			collection = db.getCollection(NAME);
			collection.drop();
		} catch (UnknownHostException e) {
			fail(e.toString());
		}
	}

	@Before
	public void before() {
		try {
			// load the asset.json resource
			InputStream is = AssetStoreTest.class.getClassLoader().getResourceAsStream("asset.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();

			// restore test asset from json
			String json = sb.toString();

			// store the test asset in the database
			this.store = StoreFactory.getAssetStore();
			this.testAsset = this.store.create();
			this.testAsset.fromJson(json);
			this.store.update(this.testAsset);

			long count = collection.count();
			assertTrue(count == 1);

		} catch (IOException e) {
			fail(e.toString());
		}
	}	

	@After
	public void after() {
		this.collection.drop();
	}

	@Test
	public void testCodeQueryExists() {
		AssetCodeQuery query = new AssetCodeQuery("4048675309");
		Collection<Asset> matches = this.store.read(query);

		int count = matches.size();
		assertTrue(count == 1);

		for (Asset asset : matches) {
			assertTrue(asset.getName().equals("TJI Joists"));
		}
	}

	@Test
	public void testCodeQueryDoesNotExist() {
		AssetCodeQuery query = new AssetCodeQuery("404");
		Collection<Asset> matches = this.store.read(query);

		int count = matches.size();
		assertTrue(count == 0);
	}

	@Test
	public void testExistsByText() {
		AssetSearchQuery query = new AssetSearchQuery("TJI", null);
		Collection<Asset> matches = this.store.read(query);

		int count = matches.size();
		assertTrue(count == 1);

		for (Asset asset : matches) {
			assertTrue(asset.getCode().equals("4048675309"));
		}
	}

	@Test
	public void testExistsByTag() {
		Collection<String> tags = new ArrayList<String>();
		tags.add("Flooring");
		AssetSearchQuery query = new AssetSearchQuery(null, tags);
		Collection<Asset> matches = this.store.read(query);

		int count = matches.size();
		assertTrue(count == 1);

		for (Asset asset : matches) {
			assertTrue(asset.getCode().equals("4048675309"));
		}
	}

	@Test
	public void testExistsByTextAndTag() {
		Collection<String> tags = new ArrayList<String>();
		tags.add("Flooring");
		AssetSearchQuery query = new AssetSearchQuery("TJI", tags);
		Collection<Asset> matches = this.store.read(query);

		int count = matches.size();
		assertTrue(count == 1);

		for (Asset asset : matches) {
			assertTrue(asset.getCode().equals("4048675309"));
		}
	}

	@Test
	public void testSearchQueryDoesNotExist() {
		AssetSearchQuery query = new AssetSearchQuery("TJ iJoists", null);
		Collection<Asset> matches = this.store.read(query);

		int count = matches.size();
		assertTrue(count == 0);
	}
}
