package org.freesideatlanta.qratitude.service;

import java.io.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.log4j.*;
import org.codehaus.jackson.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

@Path("/asset")
public class AssetResource {
	private static Logger log = Logger.getLogger(AssetResource.class);

	@POST
	public Response createAsset(String json) {
		log.debug(json);
		Response response = null;

		try {
			AssetStore store = StoreFactory.getAssetStore();
			
			Asset asset = store.create();
			asset.fromJson(json);
			store.update(asset);

			response = Response.status(Response.Status.CREATED).entity(json).build();

		} catch (IOException e) {
			// TODO: handle exception better
			log.debug(e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		log.debug(response);
		return response;
	}

	@GET
	public Response readAssets(
			@QueryParam("s") String searchText, 
			@QueryParam("t") List<String> tags) {

		Response response = null;
		try {
			AssetStore store = StoreFactory.getAssetStore();

			AssetQuery query = new AssetQuery(searchText, tags);
			Collection<Asset> assets = store.read(query);
			
			String json = Asset.toJson(assets);
			response = Response.status(Response.Status.OK).entity(json).build();
		} catch (IOException e) {
			// TODO: handle exception better
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return response;
	}

	@PUT 
	public Response updateAssets(String json) {
		// TODO: try to figure out how to automagically update all the assets
		return Response.status(Response.Status.OK).entity(json).build();
	}

	@GET
	@Path("/{id}")
	public Response readAsset(@PathParam("id") String id) {
		// TODO: find the asset with the id in the database
		Response response = null;
		try {
			Asset asset = new Asset();
			asset.setId("4048675309");
			asset.setName("TJI Joists");
			asset.getAttributes().put("description", "Light use for sound stage during 4 weeks of filming");
			asset.getAttributes().put("dimensions", "12ft and 16ft tall wood joists, ranging from 2 - 2.5in wide");
			asset.getAttributes().put("quantity", "380");
			asset.getAttributes().put("condition", "Excellent");
			asset.getAttributes().put("color", "Woody");
			asset.addPhoto("http://inventory.lifecyclebuildingcenter.org/img/tji_joists_md.jpg");
			
			String json = asset.toJson();
			response = Response.status(Response.Status.OK).entity(json).build();

		} catch (Exception e) {
			// TODO: handle exceptions better
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

				response = Response.status(Response.Status.OK).entity(json).build();
			} else {
				// TODO: handle error better
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		} catch (IOException e) {
			// TODO: handle exception better
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
