package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.bson.types.*;

public class UserQuery {

	private String searchText;

	public UserQuery(String s) throws IllegalArgumentException {
		if (s == null) {
			throw new IllegalArgumentException("AssetQuery arguments cannot all be null");
		}

		this.searchText = s;
	}

	public DBObject build() {
		Pattern p = Pattern.compile(this.searchText);
		QueryBuilder qb = QueryBuilder.start("username").regex(p);

		DBObject dbo = qb.get();
		return dbo;
	} 
}
