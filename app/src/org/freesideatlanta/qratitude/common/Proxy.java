package org.freesideatlanta.qratitude.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;

import org.freesideatlanta.qratitude.R;
import org.freesideatlanta.qratitude.authenticator.Credentials;

public class Proxy {
	private final Context context;
	private final Logger log;
	private URI authenticationUri;

	public Proxy(Context c) {
		this.context = c;
		String n = this.context.getString(R.string.app_name);
		this.log = new Logger(this.context, n);

		String url = this.context.getString(R.string.base_url) + this.context.getString(R.string.auth_path);
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
		final ArrayList<NameValuePair> parameters = this.getParameters(username, password);
		String token = null;

		try {
			log.d("setting up the post web method to execute");
			final HttpEntity entity = new UrlEncodedFormEntity(parameters);
			log.d("this.authenticationUri = " + this.authenticationUri);
			final HttpPost post = new HttpPost(this.authenticationUri);
			log.d("created post web method");
			post.addHeader(entity.getContentType());
			post.setEntity(entity);
			HttpClient client = NetworkUtil.getHttpClient();
			log.d("about to execute the post web method");
			response = client.execute(post);
			log.d("executed the post web method");
			int code = response.getStatusLine().getStatusCode();
			log.d("code = " + code);

			if (code == HttpStatus.SC_OK) {
				InputStream s = (response.getEntity() != null) ? response.getEntity().getContent() : null;
				if (s != null) {
					BufferedReader br = new BufferedReader(new InputStreamReader(s));
					token = br.readLine().trim();
				}
			}
			
			if ((token != null) && (token.length() > 0)) {
				log.i(R.string.authentication_success);
			}

		} catch (final UnsupportedEncodingException ex) {
			log.d("This should never happen, so they say...");
			throw new IllegalStateException(ex);
		} catch (final IOException ex) {
			log.d(ex.toString());
			log.e(ex.getMessage());
			log.e(R.string.token_read_error);
			token = null;
		}

		return token;
	}

	private ArrayList<NameValuePair> getParameters(String username, String password) {
		final ArrayList<NameValuePair> p = new ArrayList<NameValuePair>();
		p.add(new BasicNameValuePair(Credentials.EXTRA_USERNAME, username));
		p.add(new BasicNameValuePair(Credentials.EXTRA_PASSWORD, password));
		return p;
	}
}
