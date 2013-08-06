package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;
import java.util.*;

import com.mongodb.*;
import com.mongodb.util.*;
import org.apache.log4j.*;
import org.bson.types.*;

import org.freesideatlanta.qratitude.model.*;

public class AssetStoreMongo extends StoreMongo implements AssetStore {
	private static Logger log = Logger.getLogger(AssetStoreMongo.class);
	
	public static Asset fromDbo(DBObject dbo) throws IllegalArgumentException {
		if (dbo == null) {
			throw new IllegalArgumentException("fromDbo argument dbo cannot be null");
		}

		// TODO: validation
		ObjectId oid = (ObjectId)dbo.get("_id");
		String id  = oid.toString();
		String name = (String)dbo.get("name");

		Asset asset = new Asset();
		asset.setId(id);
		asset.setName(name);

		BasicDBList dboTags = (BasicDBList)dbo.get("tags");
		if (dboTags != null) {
			for (int index = 0; index < dboTags.size(); index++) {
				String tag = (String)dboTags.get(index);
				asset.getTags().add(tag);
			}
		}
		
		DBObject dboAttributes = (DBObject)dbo.get("attributes");
		if (dboAttributes != null) {
			Map attributes = dboAttributes.toMap();
			Set<Map.Entry> entries = attributes.entrySet();
			for (Map.Entry entry : entries) {
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();
				asset.getAttributes().put(key, value);
			}
		}

		BasicDBList dboPhotos = (BasicDBList)dbo.get("photos");
		if (dboPhotos != null) {
			for (int index = 0; index < dboPhotos.size(); index++) {
				String url = (String) dboPhotos.get(index);
				asset.addPhoto(url);
			}
		}

		return asset;
	}

	public static DBObject toDbo(Asset asset) throws IOException {
		String json = asset.toJson();
		DBObject dbo = (DBObject)JSON.parse(json);
		dbo.removeField("id");
		String id = asset.getId();
		ObjectId oid = new ObjectId(id);
		dbo.put("_id", oid);

		return dbo;
	}

	public AssetStoreMongo(String host, int port, String database, String name) {
		super(host, port, database, name);
	}

	@Override
	public Collection<Asset> read() {
		Collection<Asset> assets = new ArrayList<Asset>();
		DBCursor cursor = this.collection.find();
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			Asset asset = fromDbo(dbo);
			assets.add(asset);
		}

		log.debug("assets.size(): " + assets.size());
		return assets;
	}

	@Override 
	public Collection<Asset> read(AssetQuery query) {
		Collection<Asset> assets = new ArrayList<Asset>();
		DBObject q = query.build();
		DBCursor cursor = this.collection.find(q);
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			Asset asset = fromDbo(dbo);
			assets.add(asset);
		}

		return assets;
	}

	@Override
	public Asset create() {
		BasicDBObject dbo = new BasicDBObject();
		this.collection.insert(dbo);
		Asset asset = fromDbo(dbo); 

		return asset;
	}

	@Override
	public Asset read(String id) {
		BasicDBObject query = new BasicDBObject();
		ObjectId oid = new ObjectId(id);
		query.put("_id", oid);
		DBObject dbo = this.collection.findOne(query);
	
		Asset asset = null;
		if (dbo != null) {	
			asset = fromDbo(dbo);
		}

		return asset;
	}

	@Override
	public void update(Asset asset) throws IOException {
		log.debug("asset: " + asset.toJson());
		String id = asset.getId();
		Asset original = this.read(id);
		log.debug("original: " + original.toJson());

		// take the set difference on the photo URIs and then mark those for removal
		Set<URI> photosToRemove = Asset.getPhotosToRemove(original, asset);
		for (URI uri : photosToRemove) {
			// TODO: extract the filename from each uri
			// TODO: pass the filename to a queue, marking the photos for deletion
			// TODO: setup a worker thread to delete each photo as it enters the queue
		}

		DBObject dbo = toDbo(asset);
		this.collection.save(dbo);
	}

	@Override
	public void delete(String id) {
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
		this.collection.remove(query);
	}
}
