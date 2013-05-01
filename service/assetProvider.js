var MongoClient = require('mongodb').MongoClient;
var Connection = require('mongodb').Connection;
var Server = require('mongodb').Server;
var BSON = require('mongodb').BSON;
var ObjectID = require('mongodb').ObjectID;

AssetProvider = function (database, host, port) {
	this.client = new MongoClient(new Server(host, port, { auto_reconnect: true }, {}));
	this.client.open(function (error, client) {
		this.db = client.db(database);
	});
};

AssetProvider.prototype.getCollection = function (callback) {
	this.db.collection('assets', function (error, collection) {
		if (error) callback(error);
		else callback(null, collection);
	});
};

AssetProvider.prototype.findByTag = function (tag, callback) {
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			collection.findOne({ tag: tag }, function (error, result) {
				if (error) callback(error);
				else callback(null, result)
			});
		}
	});
};

AssetProvider.prototype.create = function (assets, callback) {
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			if (typeof(assets.length) == "undefined")
				assets = [assets];

			for (var index = 0; index < assets.length; index++) {
				asset = assets[index];
				// TODO: update audit table with create
			}

			collection.insert(assets, function () {
				callback(null, assets);
			});
		}
	});
};

AssetProvider.prototype.update = function (assets, callback) {
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			if (typeof(assets.length) == "undefined")
				assets = [assets];
			
			for (var index = 0; index < assets.length; index++) {
				asset = assets[index];
				// TODO: update audit table with update
				collection.update({ _id: asset._id }, asset, function () {
					callback(null, assets);
				});
			}
		}
	});
};

AssetProvider.prototype.remove = function (assets, callback) {
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			if (typeof(assets.length) == "undefined")
				assets = [assets];
			
			for (var index = 0; index < assets.length; index++) {
				asset = assets[index];
				// TODO: update audit table with delete
				collection.remove({ _id: asset._id }, function () {
					callback(null, assets);
				});
			}
		}
	});
};

exports.AssetProvider = AssetProvider;
