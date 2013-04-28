#!/bin/sh

BASE=http://localhost:3000

## Assets
# POST /asset
curl -i -H "Accept: application/json" -X POST -d @test-asset.json $BASE/asset
# GET /asset/:id
curl -i -H "Accept: application/json" -X GET $BASE/asset/0
# GET /asset/:id/view
curl -i -H "Accept: application/json" -X GET $BASE/asset/1/view
# GET /asset/:id/edit
curl -i -H "Accept: application/json" -X GET $BASE/asset/2/edit
# PUT /asset/:id/edit
curl -i -H "Accept: application/json" -X PUT -d @test-asset.json $BASE/asset/1/edit
# DELETE /asset/:id
curl -i -H "Accept: application/json" -X DELETE $BASE/asset

## Photos
#curl -i -H "Accept: image/jpeg" -X GET $BASE/photo 
#curl -i -H "Accept: application/json" -X POST -d @gentleman.jpg $BASE/photo
#curl -i -H "Accept: application/json" -X DELETE $BASE/photo
