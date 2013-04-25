package org.freesideatlanta.qratitude;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

public class AuthenticationActivity extends Activity {
	private static final String EXTRA_USERNAME = "org.freesideatlanta.qratitude.USERNAME";
	private static final String EXTRA_PASSWORD = "org.freesideatlanta.qratitude.PASSWORD";

	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		AccountManager am = AccountManager.get(this);
		final Intent i = this.getIntent();
		this.username = i.getStringExtra(EXTRA_USERNAME);

		EditText u = (EditText) findViewById(R.id.edit_username);

		if (!TextUtils.isEmpty(this.username)) {
			u.setText(this.username);
		}
	}
}
