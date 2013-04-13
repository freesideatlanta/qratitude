package org.freesideatlanta.qratitude;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import org.freesideatlanta.qratitude.common.Logger;

public class MainActivity extends Activity {
	public static final String EXTRA_ASSET = "org.freesideatlanta.qratitude.ASSET";
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PHOTO_SIZE = 100;

	private Logger log;
	private BitmapManager bitmapManager; 
	private MediaManager mediaManager;

	private Uri fileUri;
	private Asset asset;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// called when the activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// initialize the logger
		String n = getString(R.string.app_name);
		log = new Logger(this, n);

		// initialize class properties
		this.bitmapManager = new BitmapManager();
		
		String d = getString(R.string.destination_directory);
		this.mediaManager = new MediaManager(d);
		this.mediaManager.setLog(log);

		this.asset = new Asset();

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
				asset.setCategory(c);
				log.d("category = " + c);
			}
			@Override
			public void onNothingSelected(AdapterView<?> a) {
			}
		});
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

	public void uploadAsset(View view) {
		Intent i = new Intent(this, UploadAssetActivity.class);
		i.putExtra(EXTRA_ASSET, this.asset); 
		startActivity(i);
	}

	public void editSettings(View view) {
		Intent i = new Intent(this, EditSettingsActivity.class);
		startActivity(i);	
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
