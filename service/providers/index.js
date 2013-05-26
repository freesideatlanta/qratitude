module.exports = function Provider(dbConfig)
{
    var UserProvider  = require('./userProvider');
    var AssetProvider = require('./assetProvider');
    
    this.users  = new UserProvider (dbConfig.name, dbConfig.host, dbConfig.port);
    this.assets = new AssetProvider(dbConfig.name, dbConfig.host, dbConfig.port);
}
