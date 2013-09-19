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
	private Logger log;
	private AccountManager accountManager;
	private AssetsProxy proxy;

	public UploadAssetService() {
		super("UploadAssetService");
	}

	@Override 
	public int onStartCommand(Intent intent, int flags, int startId) {
		String n = this.getString(R.string.app_name);
		this.log = new Logger(this, n);

		this.accountManager = AccountManager.get(this);
		this.proxy = new AssetsProxy(this);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Asset asset = intent.getParcelableExtra(Asset.EXTRA_ASSET);
		log.d("asset = " + asset.toString());

		try {
			// authenticate
			Account account = getAccount();

			if (account != null) {
				String username = account.name;
				String type = this.getString(R.string.account_type);
				boolean notifyAuthFailure = true;
				String token = this.accountManager.blockingGetAuthToken(account, type, notifyAuthFailure); 

				// upload
				this.proxy.uploadAsset(username, token, asset);
			} else {
				// TODO: handle
			}

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

		log.d("type = " + type);
		log.d("matches.length = " + matches.length);

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
