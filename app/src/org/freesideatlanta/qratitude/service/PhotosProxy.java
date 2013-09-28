package org.freesideatlanta.qratitude.service;

//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;

import org.freesideatlanta.qratitude.common.Logger;
import org.freesideatlanta.qratitude.common.NetworkUtil;
import org.freesideatlanta.qratitude.R;

public class PhotosProxy {
	private final Context context;
	private final Logger log;
	private URI photosUri;

	public PhotosProxy(Context c) {
		this.context = c;
		String n = this.context.getString(R.string.app_name);
		this.log = new Logger(this.context, n);

		String url = this.context.getString(R.string.base_url) + this.context.getString(R.string.photos_path);
		this.photosUri = URI.create(url);
	}

	public Uri uploadPhoto(String username, String token, Uri file) {
		final HttpResponse response;
		Uri remote = null;
		try {
			final HttpPost post = this.postPhotos(file);
			// TODO: specify the header keys in the configuration
			post.addHeader("token", token);

			HttpClient client = NetworkUtil.getHttpClient();
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();

			if (code == HttpStatus.SC_CREATED) {
				log.d("successfully uploaded a photo");
				JSONObject o = ServiceUtil.parseObject(response);
				// TODO: specify response keys in the configuration
				String url = o.getString("url");
				remote = Uri.parse(url);
			} else {
				// TODO: handle the error
			}

		} catch (final UnsupportedEncodingException ex) {
			log.d("this should never happen, so they say...");
			throw new IllegalStateException(ex);
		} catch (final JSONException ex) {
			log.d(ex.toString());
			log.e(ex.getMessage());
			// TODO: replace with useful information
			//log.e(R.string.token_read_error);
		} catch (final IOException ex) {
			log.d(ex.toString());
			log.e(ex.getMessage());
			// TODO: replace with useful information
			//log.e(R.string.token_read_error);
		}

		return remote;
	}

	private HttpPost postPhotos(Uri file) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(this.photosUri);
		String path = file.getPath();
		File toUpload = new File(path);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("file", toUpload);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		HttpEntity entity = builder.build();
		post.setEntity(entity);

		return post;
	}
}
