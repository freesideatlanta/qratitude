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

public class PhotoStoreTest {
	private static Logger log = Logger.getLogger(PhotoStoreTest.class);

	private InputStream is;

	@Before
	public void before() {
		is = PhotoStoreTest.class.getClassLoader().getResourceAsStream("asset.jpg");
	}	

	@After
	public void after() {

	}

	@Test
	public void testCreate() {
		try {
			PhotoStore ps = StoreFactory.getPhotoStore();
			assertTrue(ps != null);

			URI uri = ps.create(is, "jpg");
			assertTrue(url != null);

			String url = uri.toString();
			log.debug(url);
			
		} catch (IOException e) {
			fail(e.toString());
		}
	}
}
