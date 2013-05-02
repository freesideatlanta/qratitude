#!/bin/sh

BASE=http://localhost:3000

## Assets
curl -i -X POST -H 'Content-Type: application/json' -d @test-asset.json $BASE/asset
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
