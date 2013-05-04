
function Asset() {
};

Asset.prototype.search = function (request, response) {
	var tag = request.params.tag;
	request.assetProvider.findByTag(tag, function (error, asset) {
		if (error) console.log(error);
		else {
			response.statusCode = 200;
			response.setHeader('Content-Type', 'application/json');
			response.end(JSON.stringify(asset));
		}
	});
};

Asset.prototype.create = function (request, response) {
	console.log("* Asset.create");
	// TODO: check for authorization
	var asset = request.body;
	console.log("asset = " + asset);
	// TODO: validate the JSON against the schema
	if (asset) {
		console.log("request.assetProvider = " + request.assetProvider);
		request.assetProvider.create(asset, function (error, result) {
			var id = result._id;
			response.redirect('/asset/' + id + '/edit');
		});
	} else {
		console.log("asset was undefined");
	}
};

Asset.prototype.load = function (request, response, next) {
	var id = request.params.id;
	request.assetProvider.findById(function (error, asset) {
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

	request.assetProvider.update(asset, function (error) { 
		if (error) console.log(error);
	});
	
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.redirect('back');
};

Asset.prototype.remove = function (request, response) {
	var asset = request.asset;
	request.assetProvider.remove(asset, function (error) {
		if (error) console.log(error);
	});

	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
};

module.exports = Asset
