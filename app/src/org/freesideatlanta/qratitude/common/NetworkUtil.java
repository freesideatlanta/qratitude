package org.freesideatlanta.qratitude.common;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.freesideatlanta.qratitude.R;

public class NetworkUtil {
	public static HttpClient getHttpClient() {
		HttpClient client = new DefaultHttpClient();
		final HttpParams p = client.getParams();
		int timeout = R.integer.http_request_timeout_ms;
		HttpConnectionParams.setConnectionTimeout(p, timeout);
		HttpConnectionParams.setSoTimeout(p, timeout);
		ConnManagerParams.setTimeout(p, timeout);

		return client;
	}
}
