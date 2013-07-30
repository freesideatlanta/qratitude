package org.freesideatlanta.qratitude.service;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.sun.jersey.core.header.*;
import com.sun.jersey.multipart.*;
import org.apache.commons.io.*;
import org.codehaus.jackson.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

@Path("/category")
public class CategoryResource {

	@POST
	public Response createCategories(String json) {
		Response response = null;
		String output = null;

		try {
			CategoryStore store = StoreFactory.getCategoryStore();
			Collection<String> categories = fromJson(json);
			categories = store.create(categories);
			output = toJson(categories);

			response = Response.status(Response.Status.CREATED).entity(output).build();
		} catch (IOException e) {
			// TODO: handle exception better
		}

		return response;
	}

	@GET
	public Response readCategories() {
		Response response = null;
		String json = null;

		try {
			CategoryStore store = StoreFactory.getCategoryStore();
			Collection<String> categories = store.read();
			json = toJson(categories);
			System.out.println(json);

			response = Response.status(Response.Status.OK).entity(json).build();
		} catch (IOException e) {
			// TODO: handle exception better
		}

		return response;
	}

	@PUT
	public Response updateCategories(String json) {
		Response response = null;
		String output = null;

		try {
			CategoryStore store = StoreFactory.getCategoryStore();
			Collection<String> categories = fromJson(json);
			store.update(categories);
			output = toJson(categories);

			response = Response.status(Response.Status.OK).entity(json).build();
		} catch (IOException e) {
			// TODO: handle exception better
		}

		return response;
	}

	@DELETE
	public Response deleteCategories() {
		Response response = null;

		CategoryStore store = StoreFactory.getCategoryStore();
		store.delete();

		response = Response.status(Response.Status.OK).build();

		return response;
	}

	private Collection<String> fromJson(String json) throws IOException {
		Collection<String> categories = new ArrayList<String>();
		
		JsonFactory f = new JsonFactory();
		JsonParser p = f.createJsonParser(json);

		while (p.nextToken() != JsonToken.END_OBJECT) {
			String field = p.getCurrentName();
			if ("categories".equals(field)) {
				p.nextToken(); // [
				while (p.nextToken() != JsonToken.END_ARRAY) {
					String category = p.getText();
					categories.add(category);
				}
			}
		}

		p.close();
	
		return categories;
	}

	private String toJson(Collection<String> categories) throws IOException {
		StringWriter sw = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(sw);

		g.writeStartObject();
		g.writeFieldName("categories");
		g.writeStartArray();
		for (String category : categories) {
			System.out.println("category: " + category);
			g.writeString(category);
		}
		g.writeEndArray();
		g.writeEndObject();
		g.close();

		String json = sw.toString();
		System.out.println("sw: " + sw);
		return json;
	}
}
