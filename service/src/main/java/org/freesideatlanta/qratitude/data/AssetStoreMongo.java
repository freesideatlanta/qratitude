package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;
import java.util.*;

import com.mongodb.*;
import org.apache.log4j.*;
import org.bson.types.*;

import org.freesideatlanta.qratitude.model.*;

public class AssetStoreMongo extends StoreMongo implements AssetStore {
	private static Logger log = Logger.getLogger(AssetStoreMongo.class);

	public AssetStoreMongo(String host, int port, String database, String name) {
		super(host, port, database, name);
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
		ObjectId id = new ObjectId();

		log.debug(id);
		dbo.put("id", id);

		log.debug(this.collection);
		this.collection.insert(dbo);

		asset = new Asset();
		asset.fromDbo(dbo);

		return asset;
	}

	@Override
	public Asset read(String id) {
		Asset asset = null;
		BasicDBObject query = new BasicDBObject();
		query.put("id", new ObjectId(id));
		DBObject dbo = this.collection.findOne(query);
		
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
		this.collection.remove(query);

		DBObject dbo = asset.toDbo();
		this.collection.insert(dbo);
	}

	@Override
	public void delete(String id) {
		BasicDBObject query = new BasicDBObject();
		query.put("id", new ObjectId(id));
		this.collection.remove(query);
	}
}
