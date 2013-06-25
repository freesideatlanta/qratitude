var mongodb     = require('mongodb');
var MongoClient = mongodb.MongoClient;
var Server      = mongodb.Server;
var ObjectID    = mongodb.ObjectID;

UserProvider = function(database, host, port) {
    var server  = new Server(host, port, { auto_reconnect: true });
    this.client = new MongoClient(server);
	this.database = database;
};

UserProvider.prototype.findByUsername = function (username, callback) {
	this.client.open(function (error, client) {
		var db = client.db(this.database);
		db.collection('users').findOne({username: username }, function (error, result) {
			if (error) callback(error);
			else callback(null, result);
		});
		client.close();
	});
};

UserProvider.prototype.create = function (users, callback) {
	this.client.open(function (error, client) {
		var db = client.db(this.database);
		if (typeof(users.length) == "undefined") users = [users];
		db.collection('users').insert(users, function() {
			callback(null, users);
		});
		client.close();
	});
};

module.exports.UserProvider = UserProvider;
