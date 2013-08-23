package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.bson.types.*;

public class UserQuery {

	private String username;

	public UserQuery(String username) throws IllegalArgumentException {
		if (username == null) {
			throw new IllegalArgumentException("AssetQuery arguments cannot all be null");
		}

		this.username = username;
	}

	public DBObject build() {
		QueryBuilder qb = QueryBuilder.start("username").is(this.username);

		DBObject dbo = qb.get();
		return dbo;
	} 
}
