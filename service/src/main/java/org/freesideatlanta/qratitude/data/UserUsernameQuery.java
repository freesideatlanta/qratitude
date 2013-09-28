package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.bson.types.*;

public class UserUsernameQuery implements UserQuery {

	private String username;

	public UserUsernameQuery(String username) throws IllegalArgumentException {
		if (username == null) {
			throw new IllegalArgumentException("UserQuery arguments cannot all be null");
		}

		this.username = username;
	}

	public DBObject build() {
		QueryBuilder qb = QueryBuilder.start("username").is(this.username);
		DBObject dbo = qb.get();

		return dbo;
	} 
}
