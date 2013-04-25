package org.freesideatlanta.qratitude;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapManager {

	public BitmapManager() {
	}	

	public Bitmap decodeSampledBitmapFromUri(String path, int width, int height) {
		Bitmap bm = null;
		final BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, o);

		o.inSampleSize = calculateInSampleSize(o, width, height);
		o.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, o);

		return bm;
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
		final int h = options.outHeight;
		final int w = options.outWidth;
		int s = 1;

		if (h > height || w > width) {
			if (w > h) {
				s = Math.round((float)h / (float)height);
			} else {
				s = Math.round((float)w / (float)width);
			}
		}

		return s;
	}
}
