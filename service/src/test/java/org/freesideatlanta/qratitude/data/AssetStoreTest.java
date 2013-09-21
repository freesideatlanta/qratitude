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

public class AssetStoreTest {
	private static Logger log = Logger.getLogger(AssetStoreTest.class);

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "qratitude_local";
	private static final String NAME = "assets";

	private static MongoClient client;
	private static DB db;
	private static DBCollection collection;

	private Asset testAsset;

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
			AssetStore as = StoreFactory.getAssetStore();
			this.testAsset = as.create();
			this.testAsset.fromJson(json);
			as.update(this.testAsset);

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
	public void testReadAll() {
		AssetStore as = StoreFactory.getAssetStore();
		Collection<Asset> results = as.read();
		assertTrue(results != null);
		assertTrue(results.size() == 1);

		long count = collection.count();
		assertTrue(count == 1);
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			DBObject dbo = (DBObject)cursor.next();
			Asset found = AssetStoreMongo.fromDbo(dbo);
			// make sure the original matches
			assertTrue(found != null);
			String foundId = found.getId();
			String testId = this.testAsset.getId();
			log.debug("foundId: " + foundId);
			log.debug("testId: " + testId);
			assertTrue(foundId.equals(testId));
		}
	}

	@Test
	public void testReadQueryCode() {
		AssetStore as = StoreFactory.getAssetStore();
		AssetQuery query = new AssetCodeQuery("4048675309");

		Collection<Asset> matches = as.read(query);
		assertTrue(matches != null);
		assertTrue(matches.size() == 1);
		Asset match = matches.iterator().next();

		assertTrue(match != null);
		assertTrue(match.getId().equals(this.testAsset.getId()));
		assertTrue(match.getName().equals(this.testAsset.getName()));
	}

	@Test
	public void testReadQueryFull() {
		AssetStore as = StoreFactory.getAssetStore();
		Collection<String> tags = new ArrayList<String>();
		tags.add("Flooring");
		AssetQuery query = new AssetSearchQuery("Joists", tags);

		Collection<Asset> matches = as.read(query);
		assertTrue(matches != null);
		assertTrue(matches.size() == 1);
		Asset match = matches.iterator().next();

		assertTrue(match != null);
		assertTrue(match.getId().equals(this.testAsset.getId()));
		assertTrue(match.getName().equals(this.testAsset.getName()));
	}

	@Test
	public void testReadQuerySearch() {
		AssetStore as = StoreFactory.getAssetStore();
		AssetQuery query = new AssetSearchQuery("Joists", null);

		Collection<Asset> matches = as.read(query);
		assertTrue(matches != null);
		assertTrue(matches.size() == 1);
		Asset match = matches.iterator().next();

		assertTrue(match != null);
		assertTrue(match.getId().equals(this.testAsset.getId()));
		assertTrue(match.getName().equals(this.testAsset.getName()));
	}

	@Test
	public void testReadQueryTags() {
		AssetStore as = StoreFactory.getAssetStore();
		Collection<String> tags = new ArrayList<String>();
		tags.add("Flooring");
		AssetQuery query = new AssetSearchQuery(null, tags);

		Collection<Asset> matches = as.read(query);
		assertTrue(matches != null);
		assertTrue(matches.size() == 1);
		Asset match = matches.iterator().next();

		assertTrue(match != null);
		assertTrue(match.getId().equals(this.testAsset.getId()));
		assertTrue(match.getName().equals(this.testAsset.getName()));
	}

	@Test
	public void testCreate() {
		AssetStore as = StoreFactory.getAssetStore();
		String id = this.testAsset.getId();

		Asset match = as.read(id);
		assertTrue(match != null);
	}

	@Test
	public void testRead() {
		AssetStore as = StoreFactory.getAssetStore();
		String id = this.testAsset.getId();

		Asset match = as.read(id);
		assertTrue(match != null);
	}

	@Test
	public void testUpdate() {
		try {
			AssetStore as = StoreFactory.getAssetStore();
			this.testAsset.setName("Joists 2");
			this.testAsset.getTags().add("Random");
			
			as.update(this.testAsset);

			String id = this.testAsset.getId();
			Asset match = as.read(id);
			
			assertTrue(match != null);
			assertTrue(match.getId().equals(id));
			assertEquals("Joists 2", match.getName());
			assertTrue(match.getTags().contains("Random"));
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testDelete() {
		AssetStore as = StoreFactory.getAssetStore();
		String id = this.testAsset.getId();
		log.debug("id: " + id);
		as.delete(id);

		Asset match = as.read(id);
		assertTrue(match == null);

		long count = collection.count();
		assertTrue(count == 0);
	}

	@Test
	public void testDeleteQuery() {
		AssetStore as = StoreFactory.getAssetStore();
		String code = this.testAsset.getCode();
		log.debug("code: " + code);
		AssetQuery query = new AssetCodeQuery(code);
		as.delete(query);

		Collection<Asset> matches = as.read(query);
		assertTrue(matches != null);
		assertTrue(matches.size() == 0);
	}
}
