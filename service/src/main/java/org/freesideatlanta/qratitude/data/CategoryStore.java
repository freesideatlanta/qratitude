package org.freesideatlanta.qratitude.data;

import java.util.*;

import org.freesideatlanta.qratitude.model.*;

public interface CategoryStore {

	Collection<String> create(Collection<String> categories);
	Collection<String> read();
	void update(Collection<String> categories);
	void delete();
}
