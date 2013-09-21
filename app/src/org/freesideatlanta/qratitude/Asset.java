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

	private Map<String, String> attributes;
	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	private String category;
	public void setCategory(String c) {
		this.category = c;
	}

	private String description;
	public void setDescription(String d) {
		this.description = d;
	}

	private String dimensions;
	public void setDimensions(String d) {
		this.dimensions = d;
	}

	private String quantity;
	public void setQuantity(String q) {
		this.quantity = q;
	}

	private String condition;
	public void setCondition(String c) {
		this.condition = c;
	}

	private String color;
	public void setColor(String c) {
		this.color = c;
	}

	private List<String> tags;
	public List<String> getTags() {
		return this.tags;
	}

	private List<Uri> photos;
	public List<Uri> getPhotos() {
		return this.photos;
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
		this.attributes = new LinkedHashMap<String, String>();
		this.tags = new ArrayList<String>();
		this.photos = new ArrayList<Uri>();
	}

	private Asset(Parcel in) {
		this();

		this.id = in.readString();
		this.code = in.readString();
		this.name = in.readString();
		this.category = in.readString();
		this.description = in.readString();
		this.dimensions = in.readString();
		this.quantity = in.readString();
		this.condition = in.readString();
		this.color = in.readString();
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
		out.writeString(this.category);
		out.writeString(this.description);
		out.writeString(this.dimensions);
		out.writeString(this.quantity);
		out.writeString(this.condition);
		out.writeString(this.color);
		out.writeTypedList(this.photos);
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("code", this.code);
		o.put("name", this.name);

		JSONArray tags = new JSONArray(this.tags);
		o.put("tags", tags);

		JSONObject attributes = new JSONObject();
		attributes.put("category", this.category);
		attributes.put("description", this.description);
		attributes.put("dimensions", this.dimensions);
		attributes.put("quantity", this.quantity);
		attributes.put("condition", this.condition);
		attributes.put("color", this.color);
		o.put("attributes", attributes);

		JSONArray photos = new JSONArray(this.photos);
		o.put("photos", photos);

		return o;
	}
}
