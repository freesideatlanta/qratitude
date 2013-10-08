package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.bson.types.*;

import org.freesideatlanta.qratitude.model.*;

public class UserTokenHashQuery implements UserQuery {

	private String hash;

	public UserTokenHashQuery(String hash) throws IllegalArgumentException {
		if (hash == null) {
			throw new IllegalArgumentException("UserQuery arguments cannot all be null");
		}

		this.hash = hash;
	}

	public DBObject build() {
		QueryBuilder qb = QueryBuilder.start("token").is(this.hash);
		DBObject dbo = qb.get();

		return dbo;
	} 
}
