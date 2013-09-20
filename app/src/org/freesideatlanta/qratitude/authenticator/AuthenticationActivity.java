package org.freesideatlanta.qratitude.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.freesideatlanta.qratitude.R;
import org.freesideatlanta.qratitude.common.Logger;

public class AuthenticationActivity extends AccountAuthenticatorActivity implements View.OnClickListener, AuthenticationListener {
	public static final String EXTRA_USERNAME = "username";
	public static final String EXTRA_AUTHORIZATION_TOKEN_TYPE = "authorization_token_type";

	private Logger log;
	private AccountManager accountManager;
	private UserLoginTask loginTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		// initialize class attributes
		String n = getString(R.string.app_name);
		log = new Logger(this, n);

		final Intent i = this.getIntent();
		String username = i.getStringExtra(Credentials.EXTRA_USERNAME);

		this.accountManager = AccountManager.get(this);

		// initialize controls
		EditText ut = (EditText) findViewById(R.id.edit_username);
		if (!TextUtils.isEmpty(username)) {
			ut.setText(username);
		}

		final Button authenticate = (Button) findViewById(R.id.button_authenticate);
		authenticate.setOnClickListener(this);
	}

	// TODO: @Override protected Dialog onCreateDialog(int id, Bundle arguments)

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

				this.loginTask = new UserLoginTask(this, c);
				this.loginTask.setAuthenticationListener(this);
				this.loginTask.execute();
			}
		}
	}

	@Override
	public void onAuthenticationResult(String token) {
		boolean success = ((token != null) && (token.length() > 0));

		if (success) {
			Credentials c = this.getCredentials();
			String username = c.getUsername();
			Account account = readAccount(username);

			if (account == null) {
				account = this.createAccount(username);
			}

			updateAccount(account, token);
			advance();
		} else {
			log.e(R.string.failed_authentication);
			// TODO: display a toast thing to the user
		}

		// TODO: hide progress dialog
	}

	@Override
	public void onAuthenticationCancel() {
		this.loginTask = null;

		// TODO: hide progress dialog
	}

	private Account readAccount(String username) {
		Account[] accounts = this.accountManager.getAccountsByType(getString(R.string.account_type));
		Account match = null;

		for (int index = 0; index < accounts.length; index++) {
			Account account = accounts[index];
			if (username.equals(account.name)) {
				match = account;
			}
		}

		return match;
	}

	private Account createAccount(String username) {
		String type = getString(R.string.account_type);
		log.d("about to create account");
		log.d("username = " + username);
		log.d("type = " + type);
		final Account account = new Account(username, type);
		this.accountManager.addAccountExplicitly(account, type, null);

		return account;
	}

	private void updateAccount(Account account, String token) {
		String type = getString(R.string.authorization_token_type);
		this.accountManager.setAuthToken(account, type, token);
	}

	private void advance() {
		final Intent i = new Intent();
		Credentials c = this.getCredentials();
		String username = c.getUsername();
		i.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
		i.putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.account_type));
		this.setAccountAuthenticatorResult(i.getExtras());
		setResult(RESULT_OK, i);
		finish();
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
