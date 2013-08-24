package org.freesideatlanta.qratitude.service;

import java.io.*;
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

			Authenticator a = new Authenticator();
			User user = a.login(username, password);

			if (user != null) {
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
	public Response checkToken(
			@HeaderParam("username") String username,
			@HeaderParam("token") String token) {
		Response response = null;

		Authenticator a = new Authenticator();
		boolean valid = a.authenticate(username, token);

		if (valid) {
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

	@DELETE
	public Response deleteToken(
			@HeaderParam("username") String username,
			@HeaderParam("token") String token) {
		Response response = null;
		Authenticator a = new Authenticator();
		boolean valid = a.authenticate(username, token);

		if (valid) {
			a.logout(username);	

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
}
