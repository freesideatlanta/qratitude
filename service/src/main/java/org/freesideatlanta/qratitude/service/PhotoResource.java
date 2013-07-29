package org.freesideatlanta.qratitude.service;

import java.io.*;
import java.net.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.sun.jersey.core.header.*;
import com.sun.jersey.multipart.*;
import org.apache.commons.io.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

@Path("/photo")
public class PhotoResource {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createPhoto(@FormDataParam("file") InputStream is, @FormDataParam("file") FormDataContentDisposition detail) {
		Response response = null;
		try {
			String filename = detail.getFileName();
			String extension = FilenameUtils.getExtension(filename);

			PhotoStore store = StoreFactory.getPhotoStore();
			URI uri = store.create(is, extension);
			String url = uri.toString();

			response = Response.status(Response.Status.CREATED).entity(url).build();

		} catch (Exception e) {
			// TODO: better exception handling
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return response;
	}
}
