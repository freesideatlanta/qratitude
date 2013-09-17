package org.freesideatlanta.qratitude.authenticator;

import android.app.Activity;
import android.os.AsyncTask;

import org.freesideatlanta.qratitude.common.Logger;
import org.freesideatlanta.qratitude.R;
import org.freesideatlanta.qratitude.service.TokensProxy;

public class UserLoginTask extends AsyncTask<Void, Void, String> {
	private Logger log;
	private Activity activity;
	
	private AuthenticationListener listener;
	public void setAuthenticationListener(AuthenticationListener l) {
		this.listener = l;
	}

	private Credentials credentials;

	public UserLoginTask(Activity a, Credentials c) {
		String n = a.getString(R.string.app_name);
		this.log = new Logger(a, n);

		this.activity = a;
		this.credentials = c;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			TokensProxy proxy = new TokensProxy(this.activity);
			log.d("this.credentials = " + this.credentials.toString());
			String token = proxy.authenticate(this.credentials);
			log.d("token = " + token);

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
