package org.freesideatlanta.qratitude.data;

import org.freesideatlanta.qratitude.model.*;

public class AssetStoreMongo implements AssetStore {

	@Override
	public Asset create() {
		// TODO: create mongo object with id set
		return null;
	}

	@Override
	public Asset read(String id) {
		// TODO: query for the mongo object with the matching id
		return null;
	}

	@Override
	public void update(Asset asset) {
		// TODO: query for the mongo object, update attributes
		String id = asset.getId();
	}

	@Override
	public void delete(String id) {
		// TODO: remove the mongo object with the matching id
	}
}
