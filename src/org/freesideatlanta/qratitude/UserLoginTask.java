package org.freesideatlanta.qratitude;

import android.app.Activity;
import android.os.AsyncTask;

import org.freesideatlanta.qratitude.common.NetworkUtil;
import org.freesideatlanta.qratitude.common.Logger;

public class UserLoginTask extends AsyncTask<Void, Void, String> {
	private Logger log;
	private Credentials credentials;

	public UserLoginTask(Activity a, Credentials c) {
		String n = a.getString(R.string.app_name);
		this.log = new Logger(a, n);

		this.credentials = c;
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

	private String authenticate(String username, String password) {
		return null;
	}
}
