package org.freeside.qratitude.common;

import android.app.Activity;
import android.util.Log;

public class Logger  {

	private String appName;
	public void setAppName(String n) {
		this.appName = n;
	}

	private Activity activity;
	public void setActivity(Activity a) {
		this.activity = a;
	}

	public Logger(Activity a, String n) {
		this.setAppName(n);
		this.setActivity(a);
	}

	public void d(int id) {
		this.d(this.getMessage(id));
	}

	public void d(String m) {
		Log.d(this.appName, m);
	}

	public void e(int id) {
		this.e(this.getMessage(id));
	}

	public void e(String m) {
		Log.e(this.appName, m);
	}

	private String getMessage(int id) {
		return this.activity.getString(id);
	}
}
