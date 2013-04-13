package org.freesideatlanta.qratitude;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Asset implements Parcelable {
	
	private String category;
	public void setCategory(String c) {
		this.category = c;
	}

	private String description;
	private String dimensions;
	private String quantity;
	private String condition;
	private String color;
	
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
		this.photos = new ArrayList<Uri>();
	}

	private Asset(Parcel in) {
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
		out.writeString(this.category);
		out.writeString(this.description);
		out.writeString(this.dimensions);
		out.writeString(this.quantity);
		out.writeString(this.condition);
		out.writeString(this.color);
		out.writeTypedList(this.photos);
	}
}
