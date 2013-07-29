package org.freesideatlanta.qratitude.service;

import java.io.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.codehaus.jackson.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

@Path("/asset")
public class AssetResource {

	@POST
	public Response createAsset(String json) {
		AssetStore store = StoreFactory.getAssetStore();
		
		Asset asset = store.create();
		asset.fromJson(json);
		store.update(asset);

		return Response.status(Response.Status.CREATED).entity(json).build();
	}

	@GET
	public Response readAssets() {
		String json = "[ { _id: '123456', name: 'my asset' }, { _id: '789101', name: 'my other asset' } ]";
		// TODO: use RESTEasy and Jackson to automagically produce the application/json
		// TODO: return the list of all assets
		return Response.status(200).entity(json).build();
	}

	@PUT 
	public Response updateAssets(String json) {
		// TODO: try to figure out how to automagically update all the assets
		return Response.status(200).entity(json).build();
	}

	@GET
	@Path("/{id}")
	public Response readAsset(@PathParam("id") String id) {
		// TODO: find the asset with the id in the database
		Response response = null;
		try {
			Asset asset = new Asset();
			asset.setId("4048675309");
			asset.getAttributes().put("name", "TJI Joists");
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
		// TODO: find the asset with the id in the database
		// TODO: parse the incoming json via the asset, set values
		return Response.status(200).entity(json).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteAsset(@PathParam("id") String id) {
		// TODO: delete the asset with the id in the database
		return Response.status(200).build();
	}
}
