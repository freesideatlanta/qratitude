package org.freesideatlanta.qratitude.model;

import java.io.*;
import java.net.*;
import java.util.*;

import org.codehaus.jackson.*;

public class Asset {

	private String id;
	private Map<String, String> attributes;
	private Set<URI> photos;

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
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

	public void fromJson(String json) {
		this.attributes.clear();
		this.photos.clear();

		// TODO: parse JSON and set values
	}

	public String toJson() throws IOException {
		StringWriter sw = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(sw);

		g.writeStartObject();
		g.writeStringField("id", this.id);

		for (String key : this.attributes.keySet()) {
			String value = this.attributes.get(key);
			g.writeStringField(key, value);
		}

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
