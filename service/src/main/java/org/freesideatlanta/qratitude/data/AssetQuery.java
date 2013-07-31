package org.freesideatlanta.qratitude.data;

import java.util.*;

import com.mongodb.*;
import org.bson.types.*;

public class AssetQuery {
	private String searchText;
	private String category;
	private Collection<String> tags;

	public AssetQuery(String s, String c, Collection<String> t) {
		this.searchText = s;
		this.category = c;
		this.tags = t;
	}

	public BasicDBObject build() {
		BasicDBObject dbo = new BasicDBObject();

		return dbo;
	} 
}
