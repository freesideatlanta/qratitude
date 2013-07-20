package org.freesideatlanta.qratitude.service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/asset")
public class AssetResource {

	@POST
	public Response createAsset(String json) {
		// TODO: create an asset in the database
		return Response.status(201).entity(json).build();
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
		String json = "{ _id: '" + id + "', name: 'my asset' }";
		return Response.status(200).entity(json).build();
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
