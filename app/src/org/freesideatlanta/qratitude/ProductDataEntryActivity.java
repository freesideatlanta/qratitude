package org.freesideatlanta.qratitude;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.freesideatlanta.qratitude.common.Logger;

public class ProductDataEntryActivity extends Activity implements View.OnClickListener {
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PHOTO_SIZE = 100;

	private Logger log;
	private BitmapManager bitmapManager; 
	private MediaManager mediaManager;

	private Map<Integer,String> assetViewAttributeMap;

	private Uri fileUri;
	private Asset asset;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// called when the activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_data_entry);

		// initialize the logger
		String n = getString(R.string.app_name);
		log = new Logger(this, n);

		// initialize class properties
		this.bitmapManager = new BitmapManager();
		
		String d = getString(R.string.destination_directory);
		this.mediaManager = new MediaManager(d);
		this.mediaManager.setLog(log);

		this.assetViewAttributeMap = new LinkedHashMap<Integer,String>();
		this.assetViewAttributeMap.put(R.id.edit_description, "description");
		this.assetViewAttributeMap.put(R.id.edit_size, "dimensions");
		this.assetViewAttributeMap.put(R.id.edit_quantity, "quantity");
		this.assetViewAttributeMap.put(R.id.edit_condition, "condition");
		this.assetViewAttributeMap.put(R.id.edit_color, "color");

		// extract asset from the intent bundle 
		Intent i = this.getIntent();
		this.asset = (Asset) i.getParcelableExtra(Asset.EXTRA_ASSET);

		log.d("asset.code = " + this.asset.getCode());

		// sets up the categories spinner control
		final Spinner s = (Spinner) findViewById(R.id.spinner_category);
		ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
		a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(a);

		// set up data binding to Asset
		s.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String c = s.getSelectedItem().toString();
				asset.putAttribute("category", c);
				log.d("category = " + c);
			}
			@Override
			public void onNothingSelected(AdapterView<?> a) {
			}
		});

		// setup event handler(s)
		final Button b = (Button) findViewById(R.id.button_upload);
		b.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		final Button b = (Button) findViewById(R.id.button_upload);
		if (view.equals(b)) {
			this.updateModel();
			this.uploadAsset();
		}
	}

	public void snapPicture(View view) {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = this.mediaManager.getOutputMediaFileUri(FileColumns.MEDIA_TYPE_IMAGE);

		if (fileUri == null) {
			Context c = this.getApplicationContext();
			Toast t = Toast.makeText(c, getString(R.string.unable_to_create_files), Toast.LENGTH_SHORT);
			t.show();
		} else {
			i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		}
	}

	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// add the Uri to the Asset
				this.asset.getPhotos().add(this.fileUri);

				// display image in the 'gallery' view
				LinearLayout gallery = (LinearLayout) findViewById(R.id.layout_gallery);
				String p = this.fileUri.getPath();

				log.d(this.fileUri.toString());
				log.d(p);

				File photo = new File(p);
				View v = viewFromPhoto(photo);
				gallery.addView(v);
			} else if (resultCode == RESULT_CANCELED) {
				log.d(R.string.user_canceled_photo);
			} else {
				log.d(R.string.photo_result_failed);
			}
		}
	}

	private void updateModel() {
		EditText et = (EditText)findViewById(R.id.edit_title);
		this.asset.setName(et.getText().toString());

		for (Map.Entry<Integer,String> entry : this.assetViewAttributeMap.entrySet()) {
			int id = entry.getKey();
			et = (EditText)findViewById(id);
			String value = et.getText().toString();
			String attribute = entry.getValue();
			
			this.asset.putAttribute(attribute, value);
		}
	}

	private void uploadAsset() {
		Intent i = new Intent(this, UploadAssetService.class);
		i.putExtra(Asset.EXTRA_ASSET, this.asset);
		startService(i);
	}

	private View viewFromPhoto(File photo) {
		String path = photo.getAbsolutePath();
		log.d(path);

		Bitmap bm = this.bitmapManager.decodeSampledBitmapFromUri(path, PHOTO_SIZE, PHOTO_SIZE);
		Context c = this.getApplicationContext();
		LinearLayout l = new LinearLayout(c);
		l.setLayoutParams(new LayoutParams(PHOTO_SIZE + 30, PHOTO_SIZE + 30));
		l.setGravity(Gravity.CENTER);

		ImageView view = new ImageView(c);
		view.setLayoutParams(new LayoutParams(PHOTO_SIZE + 30, PHOTO_SIZE + 30));
		view.setScaleType(ImageView.ScaleType.CENTER_CROP);
		view.setImageBitmap(bm);

		l.addView(view);
		return l;
	}
}
