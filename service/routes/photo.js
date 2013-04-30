exports.create = function (request, response) {
	// TODO: authenticate for user
	var filename = request.files.photo.name;
	fs.readFile(request.files.photo.path, function (err, data) {
		var newPath = "/srv/uploads/" + filename;
		fs.writeFile(newPath, data, function (err) { 
			// TODO: report/log the error message
			response.redirect("back");
		});
	});
};

exports.load = function (request, response, next) {
	// TODO: authenticate for user
	var id = request.params.id;
	// TODO: fetch photo from the database
};

exports.view = function (request, response) {
	var photo = request.photo;
};

exports.remove = function (request, response) {
	var photo = request.photo;
	// TODO: delete photo from the database 
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
};

