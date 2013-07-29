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
		String id = asset.getId();
		Asset original = this.read(id);

		// TODO: take the set difference on the photo URIs and then mark those for removal
		// TODO: just override the mongo object with the updated (after photos are deleted)
	}

	@Override
	public void delete(String id) {
		// TODO: remove the mongo object with the matching id
	}
}
