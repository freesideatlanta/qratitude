package org.freesideatlanta.qratitude;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.IntentService;
import android.content.Intent;

import org.freesideatlanta.qratitude.common.Logger;
import org.freesideatlanta.qratitude.service.AssetsProxy;

public class UploadAssetService extends IntentService {
	private final Logger log;
	private final AccountManager accountManager;
	private final AssetsProxy proxy;

	public UploadAssetService(String name) {
		super(name);

		String n = this.getString(R.string.app_name);
		this.log = new Logger(this, n);

		this.accountManager = AccountManager.get(this);
		this.proxy = new AssetsProxy(this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Asset asset = intent.getParcelableExtra(Asset.EXTRA_ASSET);

		try {
			// authenticate
			Account account = getAccount();
			String type = this.getString(R.string.account_type);
			boolean notifyAuthFailure = true;
			String token = this.accountManager.blockingGetAuthToken(account, type, notifyAuthFailure); 

			// upload
			this.proxy.uploadAsset(account, token, asset);	
		} catch (OperationCanceledException ex) {
			log.e(ex.toString());
			// TODO: handle
		} catch (IOException ex) {
			log.e(ex.toString());
			// TODO: handle
		} catch (AuthenticatorException ex) {
			log.e(ex.toString());
			// TODO: handle
		}
	}

	private Account getAccount() {
		String type = this.getString(R.string.account_type);
		Account[] matches = this.accountManager.getAccountsByType(type);
		Account account = null;

		if (matches.length > 0) {
			if (matches.length == 1) {
				account = matches[0];
			} else {
				// TODO: prompt user with dialog to select the account to use
			}
		} else { 
			// TODO: handle this as an error?  or is handled by service?
		}

		return account;
	}
}
