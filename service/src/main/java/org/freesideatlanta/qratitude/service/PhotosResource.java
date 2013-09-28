package org.freesideatlanta.qratitude.service;

import java.io.*;
import java.net.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.sun.jersey.core.header.*;
import com.sun.jersey.multipart.*;
import org.apache.commons.io.*;
import org.apache.log4j.*;
import org.codehaus.jackson.*;

import org.freesideatlanta.qratitude.data.*;
import org.freesideatlanta.qratitude.model.*;

@Path("/photos")
public class PhotosResource {
	private static Logger log = Logger.getLogger(PhotosResource.class);

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createPhoto(
			@HeaderParam("token") String token,
			@FormDataParam("file") InputStream is, 
			@FormDataParam("file") FormDataContentDisposition detail) {
		Response response = null;
		try {
			log.debug("token = " + token);
			boolean valid = token != null && !token.isEmpty();

			UserAuthenticator ua = new UserAuthenticator();
			valid &= ua.authenticate(token);
			log.debug("valid = " + valid);

			if (valid) {
				String filename = detail.getFileName();
				String extension = FilenameUtils.getExtension(filename);

				log.debug("filename = " + filename);
				log.debug("extension = " + extension);

				PhotoStore store = StoreFactory.getPhotoStore();
				URI uri = store.create(is, extension);
				String url = uri.toString();
				String json = this.toJson(url);

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

		} catch (Exception e) {
			// TODO: better exception handling
			log.debug(e);	
			log.debug(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return response;
	}

	private String toJson(String url) throws IOException {
		StringWriter sw = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(sw);

		g.writeStartObject();
		g.writeStringField("url", url);
		g.writeEndObject();

		g.close();

		String json = sw.toString();
		return json;
	}
}
