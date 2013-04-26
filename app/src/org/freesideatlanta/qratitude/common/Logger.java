package org.freesideatlanta.qratitude.common;

import android.content.Context;
import android.util.Log;

public class Logger  {

	private String appName;
	public void setAppName(String n) {
		this.appName = n;
	}

	private Context context;
	public void setContext(Context a) {
		this.context = a;
	}

	public Logger(Context c, String n) {
		this.setAppName(n);
		this.setContext(c);
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

	public void i(int id) {
		this.i(this.getMessage(id));
	}

	public void i(String m) {
		Log.i(this.appName, m);
	}

	private String getMessage(int id) {
		return this.context.getString(id);
	}
}
