package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.bson.types.*;

public class AssetQuery {

	private String searchText;
	private Collection<String> tags;

	public AssetQuery(String s, Collection<String> t) throws IllegalArgumentException {
		if (s == null && t == null) {
			throw new IllegalArgumentException("AssetQuery arguments cannot all be null");
		}

		this.searchText = s;
		this.tags = t;
	}

	public DBObject build() {
		QueryBuilder qb = null;
		boolean done = false;

		if (searchText == null) {
			qb = QueryBuilder.start("tags").all(tags);
			done = true;
		} else {
			Pattern p = Pattern.compile(searchText);
			qb = QueryBuilder.start("name").regex(p);
		}

		if (tags != null && !done) {
			qb.and("tags").all(tags);
		}

		DBObject dbo = qb.get();
		return dbo;
	} 
}
