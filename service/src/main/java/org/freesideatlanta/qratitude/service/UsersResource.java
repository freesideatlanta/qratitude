package org.freesideatlanta.qratitude.service;

import java.io.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.log4j.*;
import org.codehaus.jackson.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

@Path("/users")
public class UsersResource {
	private static Logger log = Logger.getLogger(UsersResource.class);

	@POST
	public Response createUser(String json) {
		// TODO: validate JSON
		if (json == null) {
			log.debug("json = null");
		} else if (json.isEmpty()) {
			log.debug("json is empty");
		}

		log.debug(json);
		Response response = null;

		try {
			// TODO: check to see if the caller has permissions to create a user
			UserStore store = StoreFactory.getUserStore();

			log.debug("creating the user from JSON: " + json);
			User user = new User();
			user.fromJson(json);

			log.debug("checking to see if the user already exists");
			String username = user.getUsername();
			UserQuery query = new UserUsernameQuery(username);
			Collection<User> matches = store.read(query);

			if (matches.size() == 0) {
				log.debug("inserting the user into the database");
				user = store.create(user);

				String password = user.getPassword();
				log.debug("computing the salted hash of the password for user, user.username = " + user.getUsername());
				String hash = CryptUtil.getSaltedHash(password);
				user.setPassword(hash);

				log.debug("updating user, user.id = " + user.getId());
				store.update(user);
				String userJson = user.toJson();

				log.debug("responding with HTTP status CREATED (201) with user JSON: " + userJson);

				response = Response
					.status(Response.Status.CREATED)
					.entity(userJson)
					.type(MediaType.APPLICATION_JSON)
					.build();
			} else {
				response = Response
					.status(Response.Status.CONFLICT)
					.entity("Requested username already exists")
					.type(MediaType.TEXT_PLAIN)
					.build();
			}
		} catch (IOException e) {
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		log.debug(response.toString());
		return response;
	}

	@GET
	public Response readUsers(
		@QueryParam("s") String searchText) {

		Response response = null;
		try {
			UserStore store = StoreFactory.getUserStore();
			UserQuery query = new UserSearchQuery(searchText);
			Collection<User> users = store.read(query);

			String json = User.toJson(users);
			response = Response
				.status(Response.Status.OK)
				.entity(json)
				.type(MediaType.APPLICATION_JSON)
				.build();
		} catch (IOException e) {
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return response;
	}

	@PUT
	public Response updateUsers(String json) {
		// TODO: try and figure out if it makes sense to automagically update all the users
		return Response.status(501).build();
	}

	@GET
	@Path("/{id}")
	public Response readUser(@PathParam("id") String id) {
		Response response = null;
		try {
			UserStore store = StoreFactory.getUserStore();
			User user = store.read(id);
			String json = user.toJson();
			response = Response
				.status(Response.Status.OK)
				.entity(json)
				.type(MediaType.APPLICATION_JSON)
				.build();
		} catch (Exception e) {
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return response;
	}

	@PUT
	@Path("/{id}")
	public Response updateUser(@PathParam("id") String id, String json) {
		Response response = null;
		try {
			UserStore store = StoreFactory.getUserStore();
			User original = store.read(id);
			if (original != null) {
				User user = new User();
				user.fromJson(json);

				String password = user.getPassword();

				if (password.isEmpty()) {
					user.setPassword(original.getPassword());
				} else {
					String hash = CryptUtil.getSaltedHash(password);
					user.setPassword(hash);			
				}

				store.update(user);

				response = Response
					.status(Response.Status.OK)
					.entity(json)
					.type(MediaType.APPLICATION_JSON)
					.build();
			} else {
				log.debug("user id: " + id + " not found");
				response = Response.status(Response.Status.NOT_FOUND).build();
			}
		} catch (IOException e) {
			log.debug(e);
		} catch (Exception e) {
			log.debug(e);
		}

		return response;
	}

	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") String id) {
		UserStore store = StoreFactory.getUserStore();
		store.delete(id);

		return Response.status(Response.Status.OK).build();
	}
}
