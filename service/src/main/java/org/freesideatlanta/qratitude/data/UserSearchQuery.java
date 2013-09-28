package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.bson.types.*;

public class UserSearchQuery implements UserQuery {

	private String username;

	public UserSearchQuery(String username) throws IllegalArgumentException {
		if (username == null) {
			throw new IllegalArgumentException("UserQuery arguments cannot all be null");
		}

		this.username = username;
	}

	public DBObject build() {
		Pattern p = Pattern.compile(this.username);
		QueryBuilder qb = QueryBuilder.start("username").regex(p);
		DBObject dbo = qb.get();

		return dbo;
	} 
}
