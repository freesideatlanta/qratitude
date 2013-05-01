var MongoClient = require('mongodb').MongoClient;
var Connection = require('mongodb').Connection;
var Server = require('mongodb').Server;
var BSON = require('mongodb').BSON;
var ObjectID = require('mongodb').ObjectID;

UserProvider = function (database, host, port) {
	this.client = new MongoClient(new Server(host, port, { auto_reconnect: true }, {}));
	this.client.open(function (error, client) {
		this.db = client.db(database);
	});
};

UserProvider.prototype.getCollection = function (callback) {
	this.db.collection('users', function (error, collection) {
		if (error) callback(error);
		else callback(null, collection);
	});
};

UserProvider.prototype.findByUsername = function (username, callback) {
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			collection.findOne({ username: username }, function (error, result) {
				if (error) callback(error);
				else callback(null, result)
			});
		}
	});
};

UserProvider.prototype.create = function (users, callback) {
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			if (typeof(users.length) == "undefined")
				users = [users];

			for (var index = 0; index < users.length; index++) {
				user = users[index];
				// TODO: update audit table with create
			}

			collection.insert(users, function () {
				callback(null, users);
			});
		}
	});
};

UserProvider.prototype.update = function (users, callback) {
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			if (typeof(users.length) == "undefined")
				users = [users];
			
			for (var index = 0; index < users.length; index++) {
				user = users[index];
				// TODO: update audit table with update
				collection.update({ _id: user._id }, user, function () {
					callback(null, users);
				});
			}
		}
	});
};

UserProvider.prototype.remove = function (users, callback) {
	this.getCollection(function (error, collection) {
		if (error) callback(error);
		else {
			if (typeof(users.length) == "undefined")
				users = [users];
			
			for (var index = 0; index < users.length; index++) {
				user = users[index];
				// TODO: update audit table with delete
				collection.remove({ _id: user._id }, function () {
					callback(null, users);
				});
			}
		}
	});
};

exports.UserProvider = UserProvider;
