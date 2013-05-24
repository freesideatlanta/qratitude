var MongoClient = require('mongodb').MongoClient;
var Server = require('mongodb').Server;
var ObjectID = require('mongodb').ObjectID;

function AssetProvider(database, host, port) {
	client = new MongoClient(new Server(host, port, { auto_reconnect: true }, {}));
	client.open(function (error, client) {
		db = client.db(database);
	});
};

AssetProvider.prototype.getCollection = function (callback) {
	db.collection('assets', function (error, collection) {
		if (error) callback(error);
		else callback(null, collection);
	});
};

AssetProvider.prototype.findById = function (id, callback) {
	var asset_id = ObjectID.createFromHexString(id);
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			collection.findOne({ _id: asset_id }, function(error, result) {
				if (error) callback(error);
				else callback(null, result);
			});
		}
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

module.exports = AssetProvider;
