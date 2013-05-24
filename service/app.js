// Modules
var config    = require('./config')
  , Providers = require('./providers')
  , Routes    = require('./routes')
  , Server    = require('./server');

var providers = new Providers(config.db)
  , routes    = new Routes(providers, config)
  , server    = new Server(config.server, routes, providers);

server.listen(function() {
    console.log("QRatitude listening on port %d", server.port);
});
