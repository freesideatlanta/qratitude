package org.freesideatlanta.qratitude.service;

import java.io.*;
import java.net.*;
import java.util.*;

import com.mongodb.*;
import org.apache.log4j.*;
import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;
import org.junit.runner.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

public class AssetResourceTest {
	private static Logger log = Logger.getLogger(AssetStoreTest.class);

	private static String API_URL = "http://localhost:8080/qratitude-service/api";

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "qratitude_local";
	private static final String NAME = "assets";

	private static MongoClient client;
	private static DB db;
	private static DBCollection collection;

	private String testAssetJson;

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
			InputStream is = AssetResourceTest.class.getClassLoader().getResourceAsStream("asset.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();

			// restore test asset from json
			this.testAssetJson = sb.toString();

		} catch (IOException e) {
			fail(e.toString());
		}
	}	

	@After
	public void after() {
		this.collection.drop();
	}

	// /asset tests

	@Test
	public void testCreateAsset() {
	}

	@Test
	public void testReadAssets() {

	}

	@Test
	public void testUpdateAssets() {

	}

	@Test
	public void testDeleteAssets() {

	}

	// /asset/{id} tests
	
	@Test
	public void testReadAsset() {

	}

	@Test
	public void testUpdateAsset() {

	}

	@Test
	public void testDeleteAsset() {

	}
}
