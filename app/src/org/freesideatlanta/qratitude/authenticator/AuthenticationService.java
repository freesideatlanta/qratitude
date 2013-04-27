package org.freesideatlanta.qratitude.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticationService extends Service {
	private Authenticator authenticator;

	@Override 
	public void onCreate() {
		this.authenticator = new Authenticator(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return this.authenticator.getIBinder();
	}

	@Override 
	public void onDestroy() {
	}
}
