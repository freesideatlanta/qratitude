
// TODO: replace fake data with call to data provider
var assets = [
	{ name: 'Orange Traffic Cones', description: 'Perfect for a night out on the town', quantity: 20 },
	{ name: 'Blue Velvet Curtains', description: 'For the discerning small town businessman', quantity: 5 }
];

exports.create = function (request, response) {
	var asset = request.body.asset;
	// TODO: validate attributes
	// TODO: store in database via the data provider
	assets.push(asset);
	var id = assets.length - 1;
	response.redirect('/asset/' + id + '/edit');
};

exports.load = function (request, response, next) {
	var id = request.params.id;
	// TODO: fetch asset from the mongo database
	request.asset = assets[id];
	if (request.asset) { 
		next();
	} else { 
		next(new Error('cannot find asset ' + id));
	}
};

exports.view = function (request, response) {
	var asset = request.asset;
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify(asset));
};

exports.edit = function (request, response) {
	var asset = request.asset;
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify(asset));
};

exports.update = function (request, response) {
	var asset = request.body.asset;
	// TODO: perform validation on body parameter
	request.asset.name = asset.name;
	request.asset.description = asset.description;
	request.asset.quantity = asset.quantity;

	// TODO: save out to mongo db via data provider
	
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.redirect('back');
};

exports.remove = function (request, response) {
	var asset = request.asset;
	// TODO: delete from the mongo db via data provider
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
};

