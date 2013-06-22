var mongodb     = require('mongodb');
var MongoClient = mongodb.MongoClient;
var Server      = mongodb.Server;
var ObjectID    = mongodb.ObjectID;

function UserProvider(database, host, port) {
    var server  = new Server(host, port, { auto_reconnect: true });
    this.client = new MongoClient(server);
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
	console.log("UserProvider.create: enter");
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
				console.log("UserProvider.create: insert");
				callback(null, users);
			});
		}
	});
	console.log("UserProvider.create: exit");
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

module.exports = UserProvider;
