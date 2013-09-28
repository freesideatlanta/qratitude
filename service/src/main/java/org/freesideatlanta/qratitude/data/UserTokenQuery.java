package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.bson.types.*;

public class UserTokenQuery implements UserQuery {

	private String token;

	public UserTokenQuery(String token) throws IllegalArgumentException {
		if (token == null) {
			throw new IllegalArgumentException("UserQuery arguments cannot all be null");
		}

		this.token = token;
	}

	public DBObject build() {
		QueryBuilder qb = QueryBuilder.start("token").is(this.token);
		DBObject dbo = qb.get();

		return dbo;
	} 
}
