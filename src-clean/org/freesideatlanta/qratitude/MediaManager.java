package org.freesideatlanta.qratitude;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;

import org.freesideatlanta.qratitude.common.Logger;

public class MediaManager {
  
	private Logger log;
	public void setLog(Logger l) {
		this.log = l;
	}

	private String destinationDirectoryPath;

	public MediaManager(String destinationDirectoryPath) {
	   this.destinationDirectoryPath = destinationDirectoryPath;	
	}

	public Uri getOutputMediaFileUri(int type) {
		File f = this.getOutputMediaFile(type);
		Uri u = null;

		if (f != null) {
			u = Uri.fromFile(f);
		}

		return u;
	}

	private File getOutputMediaFile(int type) {
		String state = Environment.getExternalStorageState();
		File f = null;
		log.d(state);

		boolean isMounted = state.equals(Environment.MEDIA_MOUNTED);

		if (isMounted) {
			File dest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			File dir = new File(dest, this.destinationDirectoryPath);
			boolean success = true;

			if (!dir.exists()) {
				success = dir.mkdirs();
			}

			if (success) {
				Date now = new Date();
				String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(now);
				if (type == FileColumns.MEDIA_TYPE_IMAGE) {
					String path = dir.getPath() + File.separator + ts + ".jpg";
					log.d(path);
					f = new File(path);
				} else {
					log.d(R.string.unknown_media_type);
				}
			} else {
				log.d(R.string.failed_to_create_directory);
			}
		} else {
			log.d(R.string.external_media_unmounted);
		}

		return f;
	}
}
