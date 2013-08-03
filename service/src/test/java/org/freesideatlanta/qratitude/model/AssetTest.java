package org.freesideatlanta.qratitude.model;

import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class AssetTest {

	private String json;

	@Before
	public void before() {
		try {
			// load the asset.json resource
			InputStream is = AssetTest.class.getClassLoader().getResourceAsStream("asset.json");
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
			Asset asset = new Asset();
			asset.fromJson(this.json);

			//assertEquals("51fbcd3b24acc67a7aeaf1a2", asset.getId());
			assertEquals("TJI Joists", asset.getName());
			assertTrue(asset.getTags().contains("Flooring"));
			assertTrue(asset.getTags().contains("Featured"));
			assertTrue(asset.getPhotos().contains(new URI("http://inventory.lifecyclebuildingcenter.org/img/tji_joists_md.jpg")));
			assertTrue(asset.getAttributes().containsKey("Quantity"));
			assertEquals("Excellent", asset.getAttributes().get("Condition"));
		} catch (IOException e) {
			fail(e.toString());
		} catch (URISyntaxException e) {
			fail(e.toString());
		}

	}

	@Test
	public void testToJson() {
		try {
			Asset asset = new Asset();
			asset.fromJson(this.json);
			String gen = asset.toJson();

			Asset reparsed = new Asset();
			reparsed.fromJson(gen);

			//assertEquals(asset.getId(), reparsed.getId());
			assertEquals(asset.getName(), reparsed.getName());
			assertTrue(reparsed.getTags().contains("Featured"));
			assertTrue(reparsed.getPhotos().contains(new URI("http://inventory.lifecyclebuildingcenter.org/img/tji_joists_md.jpg")));
			assertTrue(reparsed.getAttributes().containsKey("Quantity"));
			assertEquals("Excellent", reparsed.getAttributes().get("Condition"));
		} catch (IOException e) {
			fail(e.toString());
		} catch (URISyntaxException e) {
			fail(e.toString());
		}
	}
}
