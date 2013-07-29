package org.freesideatlanta.qratitude.data;

public class StoreFactory {

	public static AssetStore getAssetStore() {
		// TODO: pull configuration from defined location
		AssetStoreMongo as = new AssetStoreMongo();
		return as;
	}

	public static PhotoStore getPhotoStore() {
		PhotoStoreDisk ps = new PhotoStoreDisk();
		return ps;
	}
}
