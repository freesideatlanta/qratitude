package org.freesideatlanta.qratitude.common;

//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
import java.net.URI;

//import org.apache.http.client.HttpClient;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;

import android.accounts.Account;
import android.content.Context;

import org.freesideatlanta.qratitude.R;
import org.freesideatlanta.qratitude.Asset;

public class AssetsProxy {
	private final Context context;
	private final Logger log;
	private URI assetsUri;

	public AssetsProxy(Context c) {
		this.context = c;
		String n = this.context.getString(R.string.app_name);
		this.log = new Logger(this.context, n);

		String url = this.context.getString(R.string.base_url) + this.context.getString(R.string.assets_path);
		this.assetsUri = URI.create(url);
	}

	public void uploadAsset(Account account, String token, Asset asset) {
		// TODO: implement this web method call
	}
}
