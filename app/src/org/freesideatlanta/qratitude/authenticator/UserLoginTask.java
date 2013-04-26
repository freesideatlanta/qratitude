package org.freesideatlanta.qratitude.authenticator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;

import org.freesideatlanta.qratitude.R;
import org.freesideatlanta.qratitude.common.NetworkUtil;
import org.freesideatlanta.qratitude.common.Logger;

public class UserLoginTask extends AsyncTask<Void, Void, String> {
	private Logger log;
	
	private AuthenticationListener listener;
	public void setAuthenticationListener(AuthenticationListener l) {
		this.listener = l;
	}

	private Credentials credentials;
	private URI authenticationUri;

	public UserLoginTask(Activity a, Credentials c) {
		String n = a.getString(R.string.app_name);
		this.log = new Logger(a, n);

		this.credentials = c;
		String url = a.getString(R.string.base_url) + a.getString(R.string.auth_path);
		this.authenticationUri = URI.create(url);
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			String u = this.credentials.getUsername();
			String p = this.credentials.getPassword();
			String token = this.authenticate(u, p);

			return token;
		} catch (Exception ex) {
			log.e(R.string.failed_authentication);
			return null;
		}
	}

	@Override
	protected void onPostExecute(final String token) throws IllegalStateException {
		if (this.listener == null) {
			log.d(R.string.uninitialized_authentication_listener);
			throw new IllegalStateException();
		}

		this.listener.onAuthenticationResult(token);
	}

	@Override
	protected void onCancelled() throws IllegalStateException {
		if (this.listener == null) {
			log.d(R.string.uninitialized_authentication_listener);
			throw new IllegalStateException();
		}

		this.listener.onAuthenticationCancel();
	}

	private String authenticate(String username, String password) {
		final HttpResponse response;
		final ArrayList<NameValuePair> parameters = this.getParameters();
		String token = null;

		try {
			final HttpEntity entity = new UrlEncodedFormEntity(parameters);
			final HttpPost post = new HttpPost(this.authenticationUri);
			post.addHeader(entity.getContentType());
			post.setEntity(entity);
			HttpClient client = NetworkUtil.getHttpClient();
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();

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
			log.e(R.string.token_read_error);
			token = null;
		}

		return token;
	}

	private ArrayList<NameValuePair> getParameters() {
		final ArrayList<NameValuePair> p = new ArrayList<NameValuePair>();
		p.add(new BasicNameValuePair(Credentials.EXTRA_USERNAME, this.credentials.getUsername()));
		p.add(new BasicNameValuePair(Credentials.EXTRA_PASSWORD, this.credentials.getPassword()));
		return p;
	}
}
