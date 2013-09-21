package org.freesideatlanta.qratitude;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.freesideatlanta.qratitude.authenticator.AuthenticationActivity;
import org.freesideatlanta.qratitude.common.Logger;

public class ScanActivity extends Activity implements View.OnClickListener {
	public static boolean InDebugMode;
	private Logger log;
	private Asset asset;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// called when the activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

		// initialize static "constants"
		ScanActivity.InDebugMode = ((this.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0);

		// initialize the logger
		String n = getString(R.string.app_name);
		log = new Logger(this, n);

		// initialize class properties
		this.asset = new Asset();

		// initialize the event handlers
		final Button scan = (Button) findViewById(R.id.button_scan);
		scan.setOnClickListener(this);

		final Button settings = (Button) findViewById(R.id.button_settings);
		settings.setOnClickListener(this);
    }

	@Override 
	public void onClick(View view) {
		final Button scan = (Button) findViewById(R.id.button_scan);
		final Button settings = (Button) findViewById(R.id.button_settings);
		if (view.equals(scan)) {
			IntentIntegrator i = new IntentIntegrator(this);
			i.initiateScan();
		} else if (view.equals(settings)) {
			Intent i = new Intent(this, AuthenticationActivity.class);
			startActivity(i);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult r = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (r != null) {
			if (InDebugMode) {
				this.asset.setCode("4048675309");
			} else {
				// obtain scanned string (the psuedo-GUID)
				String code = r.getContents();
				this.asset.setCode(code);
			}

			log.d("asset.code = " + this.asset.getCode());

			// pass the asset to the product data entry activity
			Intent i = new Intent(this, ProductDataEntryActivity.class);
			i.putExtra(Asset.EXTRA_ASSET, this.asset);
			startActivity(i);

		} else {
			log.d("IntentResult returned null");
		}
	}

}
