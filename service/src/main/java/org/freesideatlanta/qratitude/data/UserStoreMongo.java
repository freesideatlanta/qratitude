package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;
import java.util.*;

import com.mongodb.*;
import com.mongodb.util.*;
import org.apache.log4j.*;
import org.bson.types.*;

import org.freesideatlanta.qratitude.model.*;

public class UserStoreMongo extends StoreMongo implements UserStore {
	private static Logger log = Logger.getLogger(UserStoreMongo.class);
	
	public static User fromDbo(DBObject dbo) throws IllegalArgumentException {
		if (dbo == null) {
			throw new IllegalArgumentException("fromDbo argument dbo cannot be null");
		}

		// TODO: validation
		ObjectId oid = (ObjectId)dbo.get("_id");
		String id  = oid.toString();
		String username = (String)dbo.get("username");

		User user = new User();
		user.setId(id);
		user.setUsername(username);

		return user;
	}

	public static DBObject toDbo(User user) throws IOException {
		String json = user.toJson();
		DBObject dbo = (DBObject)JSON.parse(json);
		dbo.removeField("id");
		String id = user.getId();
		ObjectId oid = new ObjectId(id);
		dbo.put("_id", oid);

		return dbo;
	}

	public UserStoreMongo(String host, int port, String database, String name) {
		super(host, port, database, name);
	}

	@Override
	public Collection<User> read() {
		Collection<User> users = new ArrayList<User>();
		DBCursor cursor = this.collection.find();
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			User user = fromDbo(dbo);
			users.add(user);
		}

		log.debug("users.size(): " + users.size());
		return users;
	}

	@Override 
	public Collection<User> read(UserQuery query) {
		Collection<User> assets = new ArrayList<User>();
		DBObject q = query.build();
		DBCursor cursor = this.collection.find(q);
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			User asset = fromDbo(dbo);
			assets.add(asset);
		}

		return assets;
	}

	@Override
	public User create() {
		BasicDBObject dbo = new BasicDBObject();
		this.collection.insert(dbo);
		User user = fromDbo(dbo); 

		return user;
	}

	@Override
	public User read(String id) {
		BasicDBObject query = new BasicDBObject();
		ObjectId oid = new ObjectId(id);
		query.put("_id", oid);
		DBObject dbo = this.collection.findOne(query);
	
		User user = null;
		if (dbo != null) {	
			user = fromDbo(dbo);
		}

		return user;
	}

	@Override
	public void update(User user) throws IOException {
		log.debug("user: " + user.toJson());
		String id = user.getId();
		
		// TODO: any reason to reference the original?
		User original = this.read(id);
		log.debug("original: " + original.toJson());


		DBObject dbo = toDbo(user);
		this.collection.save(dbo);
	}

	@Override
	public void delete(String id) {
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
		this.collection.remove(query);
	}
}
