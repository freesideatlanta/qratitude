db.createCollection('users');
db.users.ensureIndex({ 'username': 1 });

db.createCollection('assets');
db.assets.ensureIndex({ 'category': 1 });
db.assets.ensureIndex({ 'tag': 1 });

db.createCollection('photos');
db.assets.ensureIndex({ 'asset_id': 1 });
