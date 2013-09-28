package org.freesideatlanta.qratitude;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Asset implements Parcelable {
	public static final String EXTRA_ASSET = "org.freesideatlanta.qratitude.ASSET";
	
	private String id;
	public void setId(String i) {
		this.id = i;
	}
	public String getId() {
		return this.id;
	}

	private String code;
	public void setCode(String c) {
		this.code = c;
	}
	public String getCode() {
		return this.code;
	}

	private String name;
	public void setName(String n) {
		this.name = n;
	}

	private List<String> keys;

	private Map<String, String> attributes;
	public void putAttribute(String attribute, String value) {
		this.attributes.put(attribute, value);
	}
	public void removeAttribute(String attribute) {
		this.attributes.remove(attribute);
	}

	private List<String> tags;
	public List<String> getTags() {
		return this.tags;
	}

	private List<Uri> photos;
	public List<Uri> getPhotos() {
		return this.photos;
	}
	public void setPhotos(List<Uri> photos) {
		this.photos = photos;
	}

	public static final Parcelable.Creator<Asset> CREATOR = new Parcelable.Creator<Asset>() {
		public Asset createFromParcel(Parcel in) {
			return new Asset(in);
		}
		public Asset[] newArray(int size) {
			return new Asset[size];
		}
	};

	public Asset() {
		// TODO: make this dynamic to the app/client/customer
		this.keys = new ArrayList<String>();
		this.keys.add("category");
		this.keys.add("description");
		this.keys.add("dimensions");
		this.keys.add("quantity");
		this.keys.add("condition");
		this.keys.add("color");

		this.attributes = new LinkedHashMap<String, String>();
		this.tags = new ArrayList<String>();
		this.photos = new ArrayList<Uri>();
	}

	private Asset(Parcel in) {
		this();

		this.id = in.readString();
		// TODO: push both code and name to attributes?
		this.code = in.readString();
		this.name = in.readString();

		for (String key : this.keys) {
			this.attributes.put(key, in.readString());
		}
		
		in.readTypedList(this.photos, Uri.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(this.id);
		out.writeString(this.code);
		out.writeString(this.name);

		for (String key : keys) {
			out.writeString(this.attributes.get(key));
		}

		out.writeTypedList(this.photos);
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("code", this.code);
		o.put("name", this.name);

		JSONArray tags = new JSONArray(this.tags);
		o.put("tags", tags);

		JSONObject attrs = new JSONObject();
		for (String key : this.keys) {
			attrs.put(key, this.attributes.get(key));
		}
		o.put("attributes", attrs);

		JSONArray photos = new JSONArray(this.photos);
		o.put("photos", photos);

		return o;
	}
}
