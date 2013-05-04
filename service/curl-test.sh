#!/bin/sh

## Settings

DB=qratitude_local
DB_HOST=localhost
DB_PORT=27017
SETUP=database/mongodb/create_collections.js

WEB_HOST=localhost
WEB_PORT=3000
BASE=http://$WEB_HOST:$WEB_PORT

## Setup
mongo --quiet $DB_HOST:$DB_PORT/$DB --eval "db.dropDatabase()" 
mongo --quiet $DB_HOST:$DB_PORT/$DB $SETUP

## Assets
# POST /asset
curl -X POST -H 'Content-Type: application/json' -d @test-asset.json $BASE/asset
mongo $DB_HOST:$DB_PORT/$DB --eval "db.assets.findOne()"
# GET /asset/search/:tag
grep -Po '"tag":.*?[^\\]",' test-asset.json
TAG=$?
curl -X GET -H 'Content-Type: application/json' $BASE/asset/search/$TAG
#curl -i -H "Accept: application/json" -X POST -d @test-asset.json $BASE/asset
#curl -i -H "Accept: application/json" -X GET $BASE/asset/0
#curl -i -H "Accept: application/json" -X GET $BASE/asset/1/view
#curl -i -H "Accept: application/json" -X GET $BASE/asset/2/edit
#curl -i -H "Accept: application/json" -X PUT -d @test-asset.json $BASE/asset/1/edit
#curl -i -H "Accept: application/json" -X DELETE $BASE/asset

## Photos
#curl -i -H "Accept: image/jpeg" -X GET $BASE/photo 
#curl -i -H "Accept: application/json" -X POST -d @gentleman.jpg $BASE/photo
#curl -i -H "Accept: application/json" -X DELETE $BASE/photo

