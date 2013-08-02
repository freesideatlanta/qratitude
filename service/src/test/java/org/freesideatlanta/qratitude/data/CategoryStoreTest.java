package org.freesideatlanta.qratitude.data;

import java.net.*;
import java.util.*;

import com.mongodb.*;
import org.bson.types.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CategoryStoreTest {

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "qratitude_local";
	private static final String NAME = "categories";

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

			this.initialize();	
		} catch (UnknownHostException e) {
			fail(e.toString());
		}
	}	

	@After
	public void after() {
		this.initialize();
		// TODO: switch to this after developing a good bootstrapper
		//this.collection.drop();
	}

	@Test
	public void testGetCategoryStore() {
		CategoryStore cs = StoreFactory.getCategoryStore();
		assertTrue(cs != null);

		CategoryStoreMongo csm = (CategoryStoreMongo)cs;
		assertTrue(HOST.equals(csm.getHost()));
		assertTrue(PORT == csm.getPort());
		assertTrue(DATABASE.equals(csm.getDatabase()));
		assertTrue(NAME.equals(csm.getName()));
	}

	@Test
	public void testCreate() {
		CategoryStore cs = StoreFactory.getCategoryStore();
		cs.delete();
		Collection<String> results = cs.create(this.categories);

		assertTrue(results.contains("Appliances"));
		assertTrue(results.contains("Doors"));
		assertTrue(results.contains("Windows"));

		assertTrue(categories.size() == this.collection.count());
		DBCursor cursor = this.collection.find();

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			String category = (String)dbo.get("name");
			assertTrue(categories.contains(category));
		}
	}

	@Test
	public void testRead() {
		CategoryStore cs = StoreFactory.getCategoryStore();
		Collection<String> categories = cs.read();
		
		assertTrue(categories.size() == this.categories.size());

		for (String category : categories) {
			assertTrue(this.categories.contains(category));
		}
	}
	
	@Test
	public void testUpdate() {
		CategoryStore cs = StoreFactory.getCategoryStore();
		Collection<String> fruits = new ArrayList<String>();
		fruits.add("apples");
		fruits.add("oranges");
		fruits.add("kiwis");
		cs.update(fruits);

		assertTrue(fruits.size() == this.collection.count());
		DBCursor cursor = this.collection.find();

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			String category = (String)dbo.get("name");
			assertTrue(fruits.contains(category));
		}
	}

	@Test
	public void testDelete() {
		CategoryStore cs = StoreFactory.getCategoryStore();
		cs.delete();

		assertTrue(this.collection.count() == 0);
	}

	private void initialize() {
		this.categories = new ArrayList<String>();
		this.categories.add("Appliances");
		this.categories.add("Architecture");
		this.categories.add("Cabinets");
		this.categories.add("Ceiling");
		this.categories.add("Doors");
		this.categories.add("Electrical");
		this.categories.add("Flooring");
		this.categories.add("Hardware");
		this.categories.add("Life Safety");
		this.categories.add("Lighting");
		this.categories.add("Lumber / Wood");
		this.categories.add("Office Furniture");
		this.categories.add("Paint");
		this.categories.add("Plumbing");
		this.categories.add("Storage");
		this.categories.add("Windows");

		this.collection.drop();
		for (String category : categories) {
			BasicDBObject dbo = new BasicDBObject();
			dbo.put("name", category);
			this.collection.insert(dbo);
		}
	}
}
