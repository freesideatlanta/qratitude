package org.freesideatlanta.qratitude.data;

import java.net.*;
import java.util.*;

import com.mongodb.*;
import org.bson.types.*;
import org.junit.*;
import static org.junit.Assert.*;

import org.freesideatlanta.qratitude.model.*;

public class AssetStoreTest {

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "qratitude_local";
	private static final String NAME = "assets";

	private MongoClient client;
	private DB db;
	private DBCollection collection;

	private Collection<String> categories;

	@Before
	public void before() {
		try {
			this.client = new MongoClient(HOST, PORT);
			this.db = this.client.getDB(DATABASE);
			this.collection = this.db.getCollection(NAME);
			this.collection.drop();
		} catch (UnknownHostException e) {
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
		Collection<Asset> assets = new ArrayList<Asset>();
		Asset a1 = as.create();
		assets.add(a1);
		Asset a2 = as.create();
		assets.add(a2);
		Asset a3 = as.create();
		assets.add(a3);
		Collection<Asset> results = as.read();

		DBCursor cursor = this.collection.find();
		while (cursor.hasNext()) {
			boolean match = false;
			DBObject dbo = (DBObject)cursor.next();
			Asset found = AssetStoreMongo.fromDbo(dbo);
			// make sure the original matches
			for (Asset asset : assets) {
				String id = asset.getId();
				if (id.equals(found.getId())) {
					match = true;
				}
			}
			// make sure the result matches
			for (Asset result : results) {
				String id = result.getId();
				if (id.equals(found.getId())) {
					match &= true;
				}
			}
			assertTrue(match);
		}
	}
}
