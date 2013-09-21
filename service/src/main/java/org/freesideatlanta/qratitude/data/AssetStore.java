package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.util.*;

import org.freesideatlanta.qratitude.model.*;

public interface AssetStore {
	Collection<Asset> read();
	Collection<Asset> read(AssetQuery query);

	Asset create();
	Asset read(String id);
	void update(Asset asset) throws IOException;
	void delete(String id);
	void delete(AssetQuery query);
}
