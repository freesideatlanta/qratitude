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
import org.freesideatlanta.qratitude.common.Logger;
import org.freesideatlanta.qratitude.common.NetworkUtil;
import org.freesideatlanta.qratitude.common.Proxy;

public class UserLoginTask extends AsyncTask<Void, Void, String> {
	private Logger log;
	private Activity activity;
	
	private AuthenticationListener listener;
	public void setAuthenticationListener(AuthenticationListener l) {
		this.listener = l;
	}

	private Credentials credentials;
	private URI authenticationUri;

	public UserLoginTask(Activity a, Credentials c) {
		String n = a.getString(R.string.app_name);
		this.log = new Logger(a, n);

		this.activity = a;
		this.credentials = c;
		String url = a.getString(R.string.base_url) + a.getString(R.string.auth_path);
		this.authenticationUri = URI.create(url);
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			Proxy proxy = new Proxy(this.activity);
			log.d("this.credentials = " + this.credentials.toString());
			String token = proxy.authenticate(this.credentials);

			return token;
		} catch (Exception ex) {
			log.e(ex.getMessage());
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

}
