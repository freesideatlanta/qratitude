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

			UserAuthenticator ua = new UserAuthenticator();
			Token token = ua.login(username, password);

			if (token != null) {
				String tokenJson = token.toJson();

				response = Response
					.status(Response.Status.OK)
					.entity(tokenJson)
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
	public Response checkToken(@HeaderParam("token") String token) {
		log.debug(token);

		Response response = null;

		try {
			UserAuthenticator ua = new UserAuthenticator();
			boolean valid = ua.authenticate(token);

			if (valid) {
				UserStore us = StoreFactory.getUserStore();
				UserQuery query = new UserTokenQuery(token);
				Collection<User> matches = us.read(query);

				User user = null;
				if (matches.size() == 0) {
					user = null;
				} else if (matches.size() == 1) {
					Iterator<User> it = matches.iterator();
					user = it.next();
				} else {
					user = null;
				}

				if (user != null) {
					String userId = user.getId();
					Token t = new Token(userId, token);
					String json = t.toJson();

					response = Response
						.status(Response.Status.OK)
						.entity(json)
						.type(MediaType.APPLICATION_JSON)
						.build();
				} else {
					// TODO: make this more clear
					response = Response
						.status(Response.Status.INTERNAL_SERVER_ERROR)
						.build();
				}
			} else {
				response = Response
					.status(Response.Status.FORBIDDEN)
					.build();
			}
		} catch (Exception e) {
			log.debug(e);
		}

		return response;
	}

	@DELETE
	public Response deleteToken(@HeaderParam("token") String token) {
		Response response = null;

		try {
			UserAuthenticator ua = new UserAuthenticator();
			boolean valid = ua.authenticate(token);

			if (valid) {
				ua.logout(token);	

				response = Response
					.status(Response.Status.OK)
					.build();
			} else {
				response = Response
					.status(Response.Status.FORBIDDEN)
					.build();
			}
		} catch (Exception e) {
			log.debug(e);
		}

		return response;
	}
}
