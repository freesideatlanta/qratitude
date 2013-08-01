package org.freesideatlanta.qratitude.data;

import java.net.*;

import com.mongodb.*;
import org.apache.log4j.*;
import org.bson.types.*;

public abstract class StoreMongo {
	private static Logger log = Logger.getLogger(StoreMongo.class);

	protected String host;
	protected int port;
	protected String database;
	protected String name;

	protected MongoClient client;
	protected DB db;
	protected DBCollection collection;

	public String getHost() {
		return this.host;
	}
	public int getPort() {
		return this.port;
	}
	public String getDatabase() {
		return this.database;
	}
	public String getName() {
		return this.name;
	}

	public StoreMongo(String host, int port, String database, String name) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.name = name;
	}

	public void initialize() throws UnknownHostException {
		this.client = new MongoClient(this.host, this.port);
		this.db = this.client.getDB(this.database);
		this.collection = this.db.getCollection(this.name);
	}
}
