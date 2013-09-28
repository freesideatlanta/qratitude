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

public class UserStoreTest {
	private static Logger log = Logger.getLogger(UserStoreTest.class);

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "qratitude_local";
	private static final String NAME = "users";

	private static MongoClient client;
	private static DB db;
	private static DBCollection collection;

	private User testUser;

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
			// load the user.json resource
			InputStream is = UserStoreTest.class.getClassLoader().getResourceAsStream("user.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();

			// restore test user from json
			String json = sb.toString();

			// store the test user in the database
			UserStore us = StoreFactory.getUserStore();
			assertTrue(us != null);
			this.testUser = us.create();
			this.testUser.fromJson(json);
			assertTrue(this.testUser != null);
			us.update(this.testUser);

			long count = collection.count();
			assertTrue(count == 1);

		} catch (IOException e) {
			fail(e.toString());
		} catch (Exception e) {
			fail(e.toString());
		}
	}	

	@After
	public void after() {
		this.collection.drop();
	}

	@Test 
	public void testReadAll() {
		UserStore us = StoreFactory.getUserStore();
		Collection<User> results = us.read();
		assertTrue(results != null);
		assertTrue(results.size() == 1);

		long count = collection.count();
		assertTrue(count == 1);
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			DBObject dbo = (DBObject)cursor.next();
			User found = UserStoreMongo.fromDbo(dbo);
			// make sure the original matches
			assertTrue(found != null);
			String foundId = found.getId();
			String testId = this.testUser.getId();
			log.debug("foundId: " + foundId);
			log.debug("testId: " + testId);
			assertTrue(foundId.equals(testId));
		}
	}

	@Test
	public void testReadQueryExists() {
		UserStore us = StoreFactory.getUserStore();
		UserQuery query = new UserUsernameQuery("orange_julius");

		Collection<User> matches = us.read(query);
		assertTrue(matches != null);
		assertTrue(matches.size() == 1);
		User match = matches.iterator().next();

		assertTrue(match != null);
		assertTrue(match.getId().equals(this.testUser.getId()));
		assertTrue(match.getUsername().equals(this.testUser.getUsername()));
	}

	@Test
	public void testReadQueryNotExists() {
		UserStore us = StoreFactory.getUserStore();
		UserQuery query = new UserUsernameQuery("orange_julius2");

		Collection<User> matches = us.read(query);
		assertTrue(matches != null);
		assertTrue(matches.size() == 0);
	}

	@Test
	public void testCreate() {
		UserStore us = StoreFactory.getUserStore();
		String id = this.testUser.getId();

		User match = us.read(id);
		assertTrue(match != null);
	}

	@Test
	public void testRead() {
		UserStore us = StoreFactory.getUserStore();
		String id = this.testUser.getId();

		User match = us.read(id);
		assertTrue(match != null);
	}

	@Test
	public void testUpdate() {
		try {
			UserStore us = StoreFactory.getUserStore();
			this.testUser.setUsername("blue_julius");
			
			us.update(this.testUser);

			String id = this.testUser.getId();
			User match = us.read(id);
			
			assertTrue(match != null);
			assertTrue(match.getId().equals(id));
			assertEquals("blue_julius", match.getUsername());
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testDelete() {
		UserStore as = StoreFactory.getUserStore();
		String id = this.testUser.getId();
		log.debug("id: " + id);
		as.delete(id);

		User match = as.read(id);
		assertTrue(match == null);

		long count = collection.count();
		assertTrue(count == 0);
	}

}
