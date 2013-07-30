package org.freesideatlanta.qratitude.model;

import java.io.*;
import java.net.*;
import java.util.*;

import org.codehaus.jackson.*;

public class Asset {

	private String id;
	private String name;
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

	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	public Set<URI> getPhotos() {
		return this.photos;
	}

	public Asset() {
		this.attributes = new HashMap<String, String>();
		this.photos = new HashSet<URI>();
	}

	public void addPhoto(String url) throws NullPointerException, IllegalArgumentException {
		URI uri = URI.create(url);
		this.photos.add(uri);
	}

	public void fromJson(String json) throws IOException {
		this.attributes.clear();
		this.photos.clear();

		JsonFactory f = new JsonFactory();
		JsonParser p = f.createJsonParser(json);

		while (p.nextToken() != JsonToken.END_OBJECT) {
			String field = p.getCurrentName();
			if ("id".equals(field)) {
				p.nextToken();
				String id = p.getText();
				this.id = id;
			} else if ("name".equals(field)) {
				p.nextToken();
				String name = p.getText();
				this.name = name;
			} else if ("attributes".equals(field)) {
				while (p.nextToken() != JsonToken.END_OBJECT) {
					String attribute = p.getCurrentName();
					p.nextToken();
					String value = p.getText();
					this.attributes.put(attribute, value);
				}
			} else if ("photos".equals(field)) {
				p.nextToken() // [
				while (p.nextToken() != JsonToken.END_ARRAY) {
					String url = p.getText();
					this.addPhoto(url);
				}
			} else {
				// TODO: throw some kind of error; or ignore
			}
		}
	}

	public String toJson() throws IOException {
		StringWriter sw = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(sw);

		g.writeStartObject();
		g.writeStringField("id", this.id);
		g.writeStringField("name", this.name);

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
		g.close();

		String json = sw.toString();
		return json;
	}
}
