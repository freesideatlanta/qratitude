package org.freesideatlanta.qratitude.model;

import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class UserTest {

	private String json;

	@Before
	public void before() {
		try {
			// load the user.json resource
			InputStream is = UserTest.class.getClassLoader().getResourceAsStream("user.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			this.json = sb.toString();
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@After
	public void after() {

	}

	@Test
	public void testFromJson() {
		try {
			User user = new User();
			user.fromJson(this.json);

			assertEquals("orange_julius", user.getUsername());
			assertTrue(user.getAttributes().containsKey("email"));
			assertEquals("Orange Julius", user.getAttributes().get("name"));
		} catch (IOException e) {
			fail(e.toString());
		} catch (URISyntaxException e) {
			fail(e.toString());
		} catch (Exception e) {
			fail(e.toString());
		}

	}

	@Test
	public void testToJson() {
		try {
			User user = new User();
			user.fromJson(this.json);
			String gen = user.toJson();

			User reparsed = new User();
			reparsed.fromJson(gen);

			assertEquals(user.getUsername(), reparsed.getUsername());
			assertTrue(reparsed.getAttributes().containsKey("email"));
			assertEquals("12/29/1981", reparsed.getAttributes().get("dob"));
		} catch (IOException e) {
			fail(e.toString());
		} catch (URISyntaxException e) {
			fail(e.toString());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
