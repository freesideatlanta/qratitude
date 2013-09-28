package org.freesideatlanta.qratitude.service;

//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import org.freesideatlanta.qratitude.Asset;
import org.freesideatlanta.qratitude.common.Logger;
import org.freesideatlanta.qratitude.common.NetworkUtil;
import org.freesideatlanta.qratitude.R;

public class AssetsProxy {
	private final Context context;
	private final Logger log;
	private URI assetsUri;

	public AssetsProxy(Context c) {
		this.context = c;
		String n = this.context.getString(R.string.app_name);
		this.log = new Logger(this.context, n);

		String url = this.context.getString(R.string.base_url) + this.context.getString(R.string.assets_path);
		this.assetsUri = URI.create(url);
	}

	public void uploadAsset(String username, String token, Asset asset) {
		final HttpResponse response;
		try {
			JSONObject o = asset.toJSON();
			log.d("asset.toJSON = " + o.toString());

			final HttpPost post = this.postAssets(o);
			post.addHeader("token", token);

			HttpClient client = NetworkUtil.getHttpClient();
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();

			if (code == HttpStatus.SC_CREATED) {
				log.d("successfully uploaded an asset");
			} else {
				// TODO: handle the error
			}

		} catch (final UnsupportedEncodingException ex) {
			log.d("this should never happen, so they say...");
			throw new IllegalStateException(ex);
		} catch (final JSONException ex) {
			log.d(ex.toString());
			log.e(ex.getMessage());
			// TODO replace with useful information
			//log.e(R.string.token_read_error);
		} catch (final IOException ex) {
			log.d(ex.toString());
			log.e(ex.getMessage());
			// TODO replace with useful information
			//log.e(R.string.token_read_error);
		}
	}

	private HttpPost postAssets(JSONObject asset) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(this.assetsUri);
		StringEntity e = new StringEntity(asset.toString());
		e.setContentEncoding("UTF-8");
		e.setContentType("application/json");
		post.addHeader(e.getContentType());
		post.setEntity(e);

		return post;
	}
}
