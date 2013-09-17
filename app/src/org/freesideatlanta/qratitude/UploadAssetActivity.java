package org.freesideatlanta.qratitude;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.freesideatlanta.qratitude.common.Logger;

public class UploadAssetActivity extends Activity {
	private Logger log;

	private Asset asset;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		// initialize the logger
		String n = getString(R.string.app_name);
		log = new Logger(this, n);
		
		// extract asset from the intent bundle 
		Intent i = this.getIntent();
		this.asset = (Asset) i.getParcelableExtra(Asset.EXTRA_ASSET);
	}

	@Override
	protected void onStart() {
		
	}
}
