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

			UserStore us = StoreFactory.getUserStore();
			User user = us.read(username);

			boolean valid = false;
			if (user != null) {
				String hash = user.getPassword();
				valid = CryptUtil.check(password, hash);
			}

			if (valid) {
				String token = UUID.randomUUID().toString();
				String hash = CryptUtil.getSaltedHash(token);
				user.setToken(hash);
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
	@Path("/{hash}")
	public Response readToken(@PathParam("hash") String hash) {
		Response response = null;
		
		// TODO: return OK if the hash of the token is both present and valid

		return response;
	}

	@DELETE
	@Path("/{hash}")
	public Response deleteToken(@PathParam("hash") String hash) {
		Response response = null;

		// TODO: destroy the token (user-initiated logout admin-initiated expiration)
		// TODO: do this by clearing the entry in the 

		return response;
	}
}
