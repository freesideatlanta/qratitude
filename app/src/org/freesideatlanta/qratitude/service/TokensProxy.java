package org.freesideatlanta.qratitude.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.freesideatlanta.qratitude.authenticator.Credentials;
import org.freesideatlanta.qratitude.common.Logger;
import org.freesideatlanta.qratitude.common.NetworkUtil;
import org.freesideatlanta.qratitude.R;

public class TokensProxy {
	private final Context context;
	private final Logger log;
	private URI authenticationUri;

	public TokensProxy(Context c) {
		this.context = c;
		String n = this.context.getString(R.string.app_name);
		this.log = new Logger(this.context, n);

		String url = this.context.getString(R.string.base_url) + this.context.getString(R.string.tokens_path);
		this.authenticationUri = URI.create(url);
	}	

	public String authenticate(Credentials c) {
		String u = c.getUsername();
		String p = c.getPassword();
		String token = this.authenticate(u, p);
		return token;
	}

	public String authenticate(String username, String password) {
		final HttpResponse response;
		String token = null;

		try {
			JSONObject credentials = this.writeCredentials(username, password);
			final HttpPost post = this.postTokens(credentials);
			HttpClient client = NetworkUtil.getHttpClient();
			log.d("about to execute the post web method");
			response = client.execute(post);
			log.d("executed the post web method");
			int code = response.getStatusLine().getStatusCode();
			log.d("code = " + code);

			if (code == HttpStatus.SC_OK) {
				JSONObject o = this.parseObject(response);
				if (o != null) {
					token = o.getString("token");
				}
			} else {
				// TODO: handle this case better
			}
			
			if ((token != null) && (token.length() > 0)) {
				log.i(R.string.authentication_success);
			}

		} catch (final UnsupportedEncodingException ex) {
			log.d("this should never happen, so they say...");
			throw new IllegalStateException(ex);
		} catch (final JSONException ex) {
			log.d(ex.toString());
			log.e(ex.getMessage());
			// TODO: replace with useful information
			//log.e(R.string.token_read_error);
			token = null;
		} catch (final IOException ex) {
			log.d(ex.toString());
			log.e(ex.getMessage());
			log.e(R.string.token_read_error);
			token = null;
		}

		return token;
	}

	private JSONObject writeCredentials(String username, String password) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("username", username);
		json.put("password", password);
		return json;
	}

	private HttpPost postTokens(JSONObject credentials) throws UnsupportedEncodingException {
		log.d("this.authenticationUri = " + this.authenticationUri.toString());
		final HttpPost post = new HttpPost(this.authenticationUri);
		StringEntity e = new StringEntity(credentials.toString());
		e.setContentEncoding("UTF-8");
		e.setContentType("application/json");
		post.addHeader(e.getContentType());
		post.setEntity(e);

		return post;
	}	

	private JSONObject parseObject(HttpResponse response) throws IOException, JSONException {
		JSONObject o = null;
		String json = null;
		StringBuilder builder = new StringBuilder();
		
		InputStream s = (response.getEntity() != null) ? response.getEntity().getContent() : null;
		
		if (s != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(s));
			String line = null;
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}

			json = builder.toString();
		}

		if (json != null) {
			o = new JSONObject(json);
		}

		return o;
	}
}
