package org.freesideatlanta.qratitude;

import android.accounts.AccountManager;
import android.accounts.AccountAuthenticatorActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.freesideatlanta.qratitude.common.Logger;

public class AuthenticationActivity extends AccountAuthenticatorActivity implements View.OnClickListener {
	private Logger log;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		// initialize class attributes
		String n = getString(R.string.app_name);
		log = new Logger(this, n);

		final Intent i = this.getIntent();
		String username = i.getStringExtra(Credentials.EXTRA_USERNAME);

		// initialize controls
		EditText ut = (EditText) findViewById(R.id.edit_username);
		if (!TextUtils.isEmpty(username)) {
			ut.setText(username);
		}

		final Button authenticate = (Button) findViewById(R.id.button_authenticate);
		authenticate.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		final Button authenticate = (Button) findViewById(R.id.button_authenticate);
		if (view.equals(authenticate)) {
			Credentials c = this.getCredentials();
			if (c.isIncomplete()) {
				log.i(R.string.incomplete_credentials);
				// TODO: display a toast thing to the user
			} else {
				// TODO: show progress dialog
				UserLoginTask task = new UserLoginTask(this, c);
				task.execute();
			}
		}
	}

	private Credentials getCredentials() {
		EditText ut = (EditText) findViewById(R.id.edit_username);
		EditText pt = (EditText) findViewById(R.id.edit_password);
		String u = ut.getText().toString();
		String p = pt.getText().toString();
		Credentials c = new Credentials(u, p);	
		return c;
	}
}
