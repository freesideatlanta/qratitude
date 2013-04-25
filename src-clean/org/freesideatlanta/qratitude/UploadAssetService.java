package org.freesideatlanta.qratitude;

import android.app.IntentService;
import android.content.Intent;

public class UploadAssetService extends IntentService {

	public UploadAssetService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Asset asset = intent.getParcelableExtra(Asset.EXTRA_ASSET);
	}
}
