package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;
import java.util.*;

import com.mongodb.*;
import org.bson.types.*;

import org.freesideatlanta.qratitude.model.*;

public class AssetStoreMongo implements AssetStore {

	private String host;
	private int port;
	private String database;
	private String collection;

	private MongoClient client;
	private DB db;
	private DBCollection assets;

	public AssetStoreMongo(String host, int port, String database, String collection) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.collection = collection;
	}

	public void initialize() throws UnknownHostException {
		this.client = new MongoClient(this.host, this.port);
		this.db = this.client.getDB(this.database);
		this.assets = this.db.getCollection(this.collection);
	}

	@Override 
	public Collection<Asset> read(AssetQuery query) {
		Collection<Asset> assets = new ArrayList<Asset>();
		BasicDBObject dbo = query.build();

		// TODO: perform the query
		// TODO: convert the result to a collection of Asset

		return assets;
	}

	@Override
	public Asset create() {
		Asset asset = null;
		BasicDBObject dbo = new BasicDBObject();
		dbo.put("id", new ObjectId());

		this.assets.insert(dbo);

		asset = new Asset();
		asset.fromDbo(dbo);

		return asset;
	}

	@Override
	public Asset read(String id) {
		Asset asset = null;
		BasicDBObject query = new BasicDBObject();
		query.put("id", new ObjectId(id));
		DBObject dbo = this.assets.findOne(query);
		
		asset = new Asset();
		asset.fromDbo(dbo);

		return asset;
	}

	@Override
	public void update(Asset asset) throws IOException {
		String id = asset.getId();
		Asset original = this.read(id);

		// take the set difference on the photo URIs and then mark those for removal
		Set<URI> photosToRemove = Asset.getPhotosToRemove(original, asset);
		for (URI uri : photosToRemove) {
			// TODO: extract the filename from each uri
			// TODO: pass the filename to a queue, marking the photos for deletion
			// TODO: setup a worker thread to delete each photo as it enters the queue
		}

		// just override the mongo object with the updated (after photos are deleted)
		BasicDBObject query = new BasicDBObject();
		query.put("id", new ObjectId(id));
		this.assets.remove(query);

		DBObject dbo = asset.toDbo();
		this.assets.insert(dbo);
	}

	@Override
	public void delete(String id) {
		BasicDBObject query = new BasicDBObject();
		query.put("id", new ObjectId(id));
		this.assets.remove(query);
	}
}
