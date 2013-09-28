package org.freesideatlanta.qratitude.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceUtil {

	public static JSONObject parseObject(HttpResponse response) throws IOException, JSONException {
		JSONObject o = null;
		String json = null;
		StringBuilder builder = new StringBuilder();
		
		InputStream s = (response.getEntity() != null) ? response.getEntity().getContent() : null;
		
		if (s != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(s));
			String line = null;
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}

			json = builder.toString();
		}

		if (json != null) {
			o = new JSONObject(json);
		}

		return o;
	}
}
