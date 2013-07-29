package org.freesideatlanta.qratitude.data;

import org.freesideatlanta.qratitude.model.*;

public interface AssetStore {

	Asset create();
	Asset read(String id);
	void update(Asset asset);
	void delete(String id);
}
