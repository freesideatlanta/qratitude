package org.freesideatlanta.qratitude.service;

import java.io.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.log4j.*;
import org.codehaus.jackson.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

@Path("/assets")
public class AssetsResource {
	private static Logger log = Logger.getLogger(AssetsResource.class);

	@POST
	public Response createAsset(
			@HeaderParam("username") String username,
			@HeaderParam("token") String token,
			String json) {
		log.debug(json);
		Response response = null;

		try {
			UserAuthenticator ua = new UserAuthenticator();
			boolean valid = ua.authenticate(username, token);

			if (valid) {
				AssetStore store = StoreFactory.getAssetStore();
			
				Asset asset = store.create();
				asset.fromJson(json);
				store.update(asset);

				response = Response
					.status(Response.Status.CREATED)
					.entity(json)
					.type(MediaType.APPLICATION_JSON)	
					.build();
			} else {
				response = Response
					.status(Response.Status.FORBIDDEN)
					.build();
			}

		} catch (IOException e) {
			// TODO: handle exception better
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			// TODO: handle exception better
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		log.debug(response);
		return response;
	}

	@GET
	public Response readAssets(
			@QueryParam("c") String code,
			@QueryParam("s") String searchText, 
			@QueryParam("t") List<String> tags) {

		Response response = null;
		try {
			AssetStore store = StoreFactory.getAssetStore();
			AssetQuery query = null;

			if (code != null) {
				query = new AssetCodeQuery(code);
			} else {
				query = new AssetSearchQuery(searchText, tags);
			}
				
			Collection<Asset> assets = store.read(query);
			
			String json = Asset.toJson(assets);
			response = Response
				.status(Response.Status.OK)
				.entity(json)
				.type(MediaType.APPLICATION_JSON)	
				.build();
		} catch (IOException e) {
			// TODO: handle exception better
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return response;
	}

	@PUT 
	public Response updateAssets(String json) {
		// TODO: try to figure out how to automagically update all the assets
		return Response.status(501).build();
	}

	@GET
	@Path("/{id}")
	public Response readAsset(@PathParam("id") String id) {
		Response response = null;
		try {
			AssetStore store = StoreFactory.getAssetStore();
			Asset asset = store.read(id);

			String json = asset.toJson();
			response = Response
				.status(Response.Status.OK)
				.entity(json)
				.type(MediaType.APPLICATION_JSON)	
				.build();

		} catch (Exception e) {
			// TODO: handle exceptions better
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return response;
	}

	@PUT
	@Path("/{id}")
	public Response updateAsset(@PathParam("id") String id, String json) {
		Response response = null;
		try {
			AssetStore store = StoreFactory.getAssetStore();
			Asset original = store.read(id);

			if (original != null) {			

				Asset asset = new Asset();
				asset.fromJson(json);
				store.update(asset);

				response = Response
					.status(Response.Status.OK)
					.entity(json)
					.type(MediaType.APPLICATION_JSON)	
					.build();
			} else {
				// TODO: handle error better
				log.debug("asset id: " + id + " not found");
				response = Response.status(Response.Status.NOT_FOUND).build();
			}
		} catch (IOException e) {
			// TODO: handle exception better
			log.debug(e);
		}

		return response;
	}

	@DELETE
	@Path("/{id}")
	public Response deleteAsset(@PathParam("id") String id) {
		AssetStore store = StoreFactory.getAssetStore();
		store.delete(id);

		return Response.status(Response.Status.OK).build();
	}
}
