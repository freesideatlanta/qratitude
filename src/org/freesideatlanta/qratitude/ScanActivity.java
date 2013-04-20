package org.freesideatlanta.qratitude;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
		final Button b = (Button) findViewById(R.id.button_scan);
		b.setOnClickListener(this);
    }

	@Override 
	public void onClick(View view) {
		final Button b = (Button) findViewById(R.id.button_scan);
		if (view.equals(b)) {
			IntentIntegrator i = new IntentIntegrator(this);
			i.initiateScan();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult r = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (r != null) {
			if (InDebugMode) {
				this.asset.setId("4048675309");
			} else {
				// obtain scanned string (the psuedo-GUID)
				String id = r.getContents();
				this.asset.setId(id);
			}

			log.d("asset.id = " + this.asset.getId());

			// pass the asset to the product data entry activity
			Intent i = new Intent(this, ProductDataEntryActivity.class);
			i.putExtra(Asset.EXTRA_ASSET, this.asset);
			startActivity(i);

		} else {
			log.d("IntentResult returned null");
		}
	}
}
