package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.util.*;

import org.freesideatlanta.qratitude.model.*;

public interface UserStore {

	Collection<User> read();
	Collection<User> read(UserQuery query);

	User create();
	User create(User user) throws IOException;
	User read(String id);
	void update(User user) throws IOException;
	void delete(String id);
}
