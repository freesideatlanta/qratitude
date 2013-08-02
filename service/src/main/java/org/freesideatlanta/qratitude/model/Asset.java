package org.freesideatlanta.qratitude.model;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;

import com.mongodb.*;
import com.mongodb.util.*;
import org.apache.log4j.*;
import org.bson.types.*;
import org.codehaus.jackson.*;

public class Asset {
	private static Logger log = Logger.getLogger(Asset.class);

	public static String toJson(Collection<Asset> assets) throws IOException {
		StringWriter sw = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(sw);

		g.writeStartObject();

		g.writeFieldName("assets");
		g.writeStartArray();
		for (Asset asset : assets) {
			asset.write(g);
		}
		g.writeEndArray();

		g.writeEndObject();
		g.close();

		String json = sw.toString();

		return json;
	}

	public static Set<URI> getPhotosToRemove(Asset original, Asset updated) {
		Set<URI> toRemove = new HashSet<URI>();

		for (URI uri : original.getPhotos()) {
			if (!updated.getPhotos().contains(uri)) {
				toRemove.add(uri);
			}
		}

		return toRemove;
	}

	private String id;
	private String name;
	private Set<String> tags;
	private Map<String, String> attributes;
	private Set<URI> photos;

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getTags() {
		return this.tags;
	}

	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	public Set<URI> getPhotos() {
		return this.photos;
	}

	public Asset() {
		this.tags = new HashSet<String>();
		this.attributes = new HashMap<String, String>();
		this.photos = new HashSet<URI>();
	}

	public void addPhoto(String url) throws NullPointerException, IllegalArgumentException {
		URI uri = URI.create(url);
		this.photos.add(uri);
	}

	public void fromDbo(DBObject dbo) {
		// TODO: validation
		ObjectId id = (ObjectId)dbo.get("_id");
		this.id = id.toString();
		this.name = (String)dbo.get("name");

		this.tags.clear();
		BasicDBList dboTags = (BasicDBList)dbo.get("tags");
		for (int index = 0; index < dboTags.size(); index++) {
			String tag = (String)dboTags.get(index);
			this.tags.add(tag);
		}
		
		this.attributes.clear();
		DBObject dboAttributes = (DBObject)dbo.get("attributes");
		Map attributes = dboAttributes.toMap();
		Set<Map.Entry> entries = attributes.entrySet();
		for (Map.Entry entry : entries) {
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			this.attributes.put(key, value);
		}

		this.photos.clear();
		BasicDBList dboPhotos = (BasicDBList)dbo.get("photos");
		for (int index = 0; index < dboPhotos.size(); index++) {
			String url = (String) dboPhotos.get(index);
			this.addPhoto(url);
		}
	}

	public DBObject toDbo() throws IOException {
		String json = this.toJson();
		DBObject dbo = (DBObject)JSON.parse(json);
		dbo.removeField("id");

		ObjectId id = new ObjectId(this.id);
		dbo.put("_id", id);

		return dbo;
	}

	public void fromJson(String json) throws IOException {
		// TODO: validation
		this.attributes.clear();
		this.photos.clear();

		JsonFactory f = new JsonFactory();
		JsonParser p = f.createJsonParser(json);

		int breakpoint = 100;

		while (p.nextToken() != JsonToken.END_OBJECT) {
			String field = p.getCurrentName();
			log.debug("field: " + field);
			if ("id".equals(field)) {
				p.nextToken();
				String id = p.getText();
				log.debug("id: " + id);
				this.id = id;
			} else if ("name".equals(field)) {
				p.nextToken();
				String name = p.getText();
				log.debug("name: " + name);
				this.name = name;
			} else if ("tags".equals(field)) {
				p.nextToken(); // [
				while (p.nextToken() != JsonToken.END_ARRAY) {
					String tag = p.getText();
					log.debug("tag: " + tag);
					this.tags.add(tag);
				}
			} else if ("attributes".equals(field)) {
				p.nextToken(); // {
				while (p.nextToken() != JsonToken.END_OBJECT) {
					String attribute = p.getCurrentName();
					p.nextToken();
					String value = p.getText();
					log.debug("attribute: " + attribute);
					log.debug("value: " + value);
					this.attributes.put(attribute, value);
				}
			} else if ("photos".equals(field)) {
				p.nextToken(); // [
				while (p.nextToken() != JsonToken.END_ARRAY) {
					String url = p.getText();
					log.debug("url: " + url);
					this.addPhoto(url);
				}
			} else {
				log.debug("unmatched field");
				// TODO: throw some kind of error; or ignore
			}
			
			breakpoint--;
			if (breakpoint == 0) break;
		}
	}

	public String toJson() throws IOException {
		StringWriter sw = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(sw);

		this.write(g);
		g.close();

		String json = sw.toString();
		return json;
	}

	private void write(JsonGenerator g) throws IOException {
		g.writeStartObject();
		g.writeStringField("id", this.id);
		g.writeStringField("name", this.name);

		g.writeFieldName("tags");
		g.writeStartArray();
		for (String tag: this.tags) {
			g.writeString(tag);
		}
		g.writeEndArray();

		g.writeObjectFieldStart("attributes");
		for (String key : this.attributes.keySet()) {
			String value = this.attributes.get(key);
			g.writeStringField(key, value);
		}
		g.writeEndObject();

		g.writeFieldName("photos");
		g.writeStartArray();
		for (URI uri : this.photos) {
			String path = uri.toString();
			g.writeString(path);
		}
		g.writeEndArray();

		g.writeEndObject();
	}
}
