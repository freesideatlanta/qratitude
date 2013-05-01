
Asset = function (assetProvider) {
	this.assetProvider = assetProvider;
};

Asset.prototype.search = function (request, response) {
	var tag = request.params.tag;
	this.assetProvider.findByTag(tag, function (error, asset) {
		if (error) console.log(error);
		else {
			response.statusCode = 200;
			response.setHeader('Content-Type', 'application/json');
			response.end(JSON.stringify(asset));
		}
	});
};

Asset.prototype.create = function (request, response) {
	// TODO: check for authorization
	var asset = request.body.asset;
	// TODO: validate the JSON against the schema
	this.assetProvider.create(asset, function (error, result) {
		var id = result._id;
		response.redirect('/asset/' + id + '/edit');
	});
};

Asset.prototype.load = function (request, response, next) {
	var id = request.params.id;
	this.assetProvider.findById(function (error, asset) {
		if (error) console.log(error);
		else {
			request.asset = asset;
			next();
		}
	});
};

Asset.prototype.view = function (request, response) {
	var asset = request.asset;
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify(asset));
};

Asset.prototype.edit = function (request, response) {
	var asset = request.asset;
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify(asset));
};

Asset.prototype.update = function (request, response) {
	var asset = request.body.asset;
	// TODO: validate the JSON against the schema
	request.asset = asset;

	this.assetProvider.update(asset, function (error) { 
		if (error) console.log(error);
	});
	
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.redirect('back');
};

Asset.prototype.remove = function (request, response) {
	var asset = request.asset;
	this.assetProvider.remove(asset, function (error) {
		if (error) console.log(error);
	});

	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
};

exports.Asset = Asset
