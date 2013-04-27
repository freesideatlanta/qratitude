
var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , http = require('http')
  , path = require('path');
var ProductProvider = require('./productprovider-memory').ProductProvider;

var app = express();

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(express.cookieParser('taxidermy nutria'));
app.use(express.session());
app.use(app.router);
app.use(require('stylus').middleware(__dirname + '/public'));
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

var productProvider = new ProductProvider();

app.get('/', function(req, res) {
	productProvider.findAll(function (error, docs) {
		res.send(docs);
	});
})

app.get('/auth', function (request, response) {
	response.send("login page");
})

app.post('/auth', function (request, response) {
	console.log("sending back the token on the response...");
	response.send("8675309");
})


app.get('/', routes.index);
app.get('/users', user.list);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
