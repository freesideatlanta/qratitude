package org.freesideatlanta.qratitude.model;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;

import org.apache.log4j.*;
import org.codehaus.jackson.*;

import org.freesideatlanta.qratitude.data.*;

public class Credentials {
	private static Logger log = Logger.getLogger(Credentials.class);

	private String username;
	private String password;

	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public void fromJson(String json) throws IOException {
		// TODO: validation
		JsonFactory f = new JsonFactory();
		JsonParser p = f.createJsonParser(json);

		while (p.nextToken() != JsonToken.END_OBJECT) {
			String field = p.getCurrentName();
			if ("username".equals(field)) {
				p.nextToken();
				String username = p.getText();
				this.username = username;
			} else if ("password".equals(field)) {
				p.nextToken();
				String password = p.getText();
				this.password = password;
			} else {
				// TODO: throw some kind of error; or ignore
			}
		}
	}
}
