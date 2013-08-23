package org.freesideatlanta.qratitude.model;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;

import org.apache.log4j.*;
import org.codehaus.jackson.*;

public class User {
	private static Logger log = Logger.getLogger(User.class);

	public static String toJson(Collection<User> users) throws IOException {
		StringWriter sw = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(sw);

		g.writeStartObject();

		g.writeFieldName("users");
		g.writeStartArray();
		for (User user : users) {
			user.write(g);
		}
		g.writeEndArray();

		g.writeEndObject();
		g.close();

		String json = sw.toString();

		return json;
	}

	private String id;
	private String username;
	// NOTE: these are temporary placeholders for plaintext values that will get hashed
	private String password;
	private String token;
	private Map<String, String> attributes;

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	public User() {
		this.attributes = new HashMap<String, String>();
	}

	public void fromJson(String json) throws IOException {
		// TODO: validation
		this.attributes.clear();

		JsonFactory f = new JsonFactory();
		JsonParser p = f.createJsonParser(json);

		while (p.nextToken() != JsonToken.END_OBJECT) {
			String field = p.getCurrentName();
			if ("id".equals(field)) {
				p.nextToken();
				String id = p.getText();
				this.id = id;
			} else if ("username".equals(field)) {
				p.nextToken();
				String username = p.getText();
				this.username = username;
			} else if ("attributes".equals(field)) {
				p.nextToken(); // {
				while (p.nextToken() != JsonToken.END_OBJECT) {
					String attribute = p.getCurrentName();
					p.nextToken();
					String value = p.getText();
					this.attributes.put(attribute, value);
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

		this.write(g);
		g.close();

		String json = sw.toString();
		return json;
	}

	private void write(JsonGenerator g) throws IOException {
		g.writeStartObject();
		g.writeStringField("id", this.id);
		g.writeStringField("username", this.username);

		g.writeObjectFieldStart("attributes");
		for (String key : this.attributes.keySet()) {
			String value = this.attributes.get(key);
			g.writeStringField(key, value);
		}
		g.writeEndObject();

		g.writeEndObject();
	}
}
