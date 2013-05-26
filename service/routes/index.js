module.exports = function Routes(providers, config) {

    var Photo = require('./photo.js')
      , Asset = require('./asset.js')
      , User  = require('./user.js');

    this.user  = new User(providers.users);
    this.asset = new Asset();
    this.photo = new Photo();
};
