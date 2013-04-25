package org.freesideatlanta.qratitude;

import android.accounts.AccountManager;
import android.accounts.AccountAuthenticatorActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import org.freesideatlanta.qratitude.common.Logger;

public class AuthenticationActivity extends AccountAuthenticatorActivity {
	private Logger log;
	private Credentials credentials;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		// initialize class attributes
		String n = getString(R.string.app_name);
		log = new Logger(this, n);

		final Intent i = this.getIntent();
		String username = i.getStringExtra(Credentials.EXTRA_USERNAME);
		String p = i.getStringExtra(Credentials.EXTRA_PASSWORD);
		this.credentials = new Credentials(username, p);

		// initialize controls
		EditText ut = (EditText) findViewById(R.id.edit_username);
		if (!TextUtils.isEmpty(username)) {
			ut.setText(username);
		}
	}
}
