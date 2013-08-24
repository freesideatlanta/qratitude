package org.freesideatlanta.qratitude.service;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.log4j.*;
import org.codehaus.jackson.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

@Path("/tokens")
public class TokensResource {
	private static Logger log = Logger.getLogger(TokensResource.class);

	@POST
	public Response createToken(String json) {
		log.debug(json);
		Response response = null;

		// TODO: SSL required throughout the site
		try {

			Credentials credentials = new Credentials();
			credentials.fromJson(json);
			String username = credentials.getUsername();
			String password = credentials.getPassword();

			User user = this.queryUser(username);
			
			boolean valid = false;
			if (user != null) {
				String hash = user.getPassword();
				valid = CryptUtil.check(password, hash);
			}

			if (valid) {
				String token = UUID.randomUUID().toString();
				String hash = CryptUtil.getSaltedHash(token);
				user.setToken(hash);
			
				UserStore us = StoreFactory.getUserStore();
				us.update(user);
				String userJson = user.toJson();

				response = Response
					.status(Response.Status.OK)
					.entity(userJson)
					.type(MediaType.APPLICATION_JSON)
					.build();
			} else {
				response = Response
					.status(Response.Status.FORBIDDEN)
					.build();
			}

		} catch (Exception e) {
			log.debug(e);
		}

		// NOTE: Android app will store the token in the AccountManager
		// NOTE: website may store the token in a cookie or browser

		return response;
	}

	@GET
	@Path("/{username}")
	public Response checkToken(
			@HeaderParam("token") String token, 
			@PathParam("username") String username) {
		Response response = null;

		try {
			User user = this.queryUser(username);
			boolean valid = this.checkToken(user, token);

			if (valid) {
				String json = user.toJson();

				response = Response
					.status(Response.Status.OK)
					.entity(json)
					.type(MediaType.APPLICATION_JSON)
					.build();
			} else {
				response = Response
					.status(Response.Status.FORBIDDEN)
					.build();
			}

		} catch (IOException e) {
			log.debug(e);
			response = Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.build();
		}

		return response;
	}

	@DELETE
	@Path("/{username}")
	public Response deleteToken(
			@HeaderParam("token") String token, 
			@PathParam("username") String username) {
		Response response = null;
		User user = this.queryUser(username);
		boolean valid = this.checkToken(user, token);

		if (valid) {
			user.setToken("");

			response = Response
				.status(Response.Status.OK)
				.build();
		} else {
			response = Response
				.status(Response.Status.FORBIDDEN)
				.build();
		}

		return response;
	}

	private User queryUser(String username) {
		UserStore us = StoreFactory.getUserStore();
		UserQuery query = new UserQuery(username);
		Collection<User> matches = us.read(query);

		User user = null;
		if (matches.size() == 0) {
			user = null;
		} else if (matches.size() == 1) {
			Iterator<User> iterator = matches.iterator();
			user = iterator.next();
		} else { // matches.size() > 1
			// TODO: throw an exception
			user = null;
		}

		return user;
	}

	private boolean checkToken(User user, String token) {
		boolean valid = false;
		if (user != null) {
			String hash = user.getToken();
			if (hash != null && !hash.isEmpty()) {
				valid = hash.equals(token);
			}
		}

		return valid;
	}
}
