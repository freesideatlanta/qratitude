package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.apache.log4j.*;
import org.bson.types.*;

public class AssetSearchQuery implements AssetQuery {
	private static Logger log = Logger.getLogger(AssetSearchQuery.class);

	private String searchText;
	private Collection<String> tags;

	public AssetSearchQuery(String s, Collection<String> t) throws IllegalArgumentException {
		if (s == null && t == null) {
			throw new IllegalArgumentException("AssetQuery arguments cannot all be null");
		}

		this.searchText = s;
		this.tags = t;
	}

	@Override
	public DBObject build() {
		QueryBuilder qb = null;
		boolean done = false;

		if (searchText == null) {
			qb = QueryBuilder.start("tags").all(tags);
			done = true;
		} else {
			Pattern p = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
			qb = QueryBuilder.start("name").regex(p);
		}

		if (tags != null && tags.size() > 0 && !done) {
			qb.and("tags").all(tags);
		}

		DBObject dbo = qb.get();
		log.debug("dbo = " + dbo.toString());

		return dbo;
	} 
}
