package org.freesideatlanta.qratitude.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.freesideatlanta.qratitude.common.Logger;
import org.freesideatlanta.qratitude.common.TokensProxy;
import org.freesideatlanta.qratitude.R;

public class Authenticator extends AbstractAccountAuthenticator {
	private final Logger log;
	private final Context context;

	public Authenticator(Context c) {
		super(c);
		this.context = c;

		String n = c.getString(R.string.app_name);
		this.log = new Logger(c, n);
	}

	@Override
	public Bundle addAccount(
			AccountAuthenticatorResponse response, 
			String accountType,
			String authTokenType,
			String[] requiredFeatures,
			Bundle options) {
		final Intent i = new Intent(this.context, AuthenticationActivity.class);
		i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		final Bundle b = new Bundle();
		b.putParcelable(AccountManager.KEY_INTENT, i);
		return b;
	}

	@Override
	public Bundle getAuthToken(
			AccountAuthenticatorResponse response, 
			Account account,
			String authTokenType,
			Bundle loginOptions) throws NetworkErrorException {
		log.d("authTokenType = " + authTokenType);
		Bundle result;

		String att = this.context.getString(R.string.authorization_token_type);
		log.d("att = " + att);

		if (authTokenType.equals(att)) {
			result = this.getServerAuthorization(account);
			if (result == null) {
				result = this.getAuthorizationPrompt(account, response, authTokenType);
			}
		} else {
			result = new Bundle();
			String m = this.context.getString(R.string.invalid_authorization_token_type);
			result.putString(AccountManager.KEY_ERROR_MESSAGE, m);
		}

		return result;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) {
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		// returning null implies that there is no support for multiple authorization token types
		return null;
	}

	@Override 
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) {
		final Bundle b = new Bundle();
		b.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		return b;
	}

	@Override 
	public Bundle updateCredentials(
			AccountAuthenticatorResponse response,
			Account account,
			String authTokenType, 
			Bundle loginOptions) {
		return null;
	}

	private Bundle getServerAuthorization(Account account) {
		Bundle result = null;
		final AccountManager am = AccountManager.get(this.context);
		final String p = am.getPassword(account);
		if (p != null) {
			final TokensProxy proxy = new TokensProxy(this.context);
			final String token = proxy.authenticate(account.name, p);
			if (!TextUtils.isEmpty(token)) {
				String type = this.context.getString(R.string.account_type);
				result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, type);
				result.putString(AccountManager.KEY_AUTHTOKEN, token);
			}
		}

		return result;
	}

	private Bundle getAuthorizationPrompt(Account account, AccountAuthenticatorResponse response, String authTokenType) {
		final Intent i = new Intent(this.context, AuthenticationActivity.class);
		i.putExtra(AuthenticationActivity.EXTRA_USERNAME, account.name);
		i.putExtra(AuthenticationActivity.EXTRA_AUTHORIZATION_TOKEN_TYPE, authTokenType);
		i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

		Bundle result = new Bundle();
		result.putParcelable(AccountManager.KEY_INTENT, i);
		return result;
	}
}
