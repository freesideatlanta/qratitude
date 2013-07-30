package org.freesideatlanta.qratitude.data;

import java.io.*;

import org.freesideatlanta.qratitude.model.*;

public interface AssetStore {

	Asset create();
	Asset read(String id);
	void update(Asset asset) throws IOException;
	void delete(String id);
}
