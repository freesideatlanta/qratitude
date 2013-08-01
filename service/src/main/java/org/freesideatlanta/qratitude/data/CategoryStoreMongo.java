package org.freesideatlanta.qratitude.data;

import java.util.*;

import com.mongodb.*;
import org.bson.types.*;

import org.freesideatlanta.qratitude.model.*;

public class CategoryStoreMongo extends StoreMongo implements CategoryStore {

	public CategoryStoreMongo(String host, int port, String database, String collection) {
		super(host, port, database, collection);
	}

	@Override
	public Collection<String> create(Collection<String> categories) {
		for (String category : categories) {
			BasicDBObject dbo = new BasicDBObject();
			dbo.put("name", category);
			this.collection.insert(dbo);
		}

		return categories;
	}

	@Override
	public Collection<String> read() {
		Collection<String> categories = new ArrayList<String>();

		DBCursor cursor = this.collection.find();
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			String category = (String)dbo.get("name");
			categories.add(category);
		}

		return categories;
	}

	@Override
	public void update(Collection<String> categories) {
		// override the categories collection
		this.delete();
		Collection<String> results = this.create(categories);
	}

	@Override
	public void delete() {
		this.collection.drop();
	}
}
