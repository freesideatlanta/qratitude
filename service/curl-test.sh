#!/bin/sh

BASE=http://localhost:3000

curl -i -H "Accept: application/json" -X GET $BASE/asset
curl -i -H "Accept: application/json" -X POST -d @test-asset.json $BASE/asset
curl -i -H "Accept: application/json" -X PUT $BASE/asset
curl -i -H "Accept: application/json" -X DELETE $BASE/asset
curl -i -H "Accept: image/jpeg" -X GET $BASE/photo 
curl -i -H "Accept: application/json" -X POST -d @gentleman.jpg $BASE/photo
curl -i -H "Accept: application/json" -X DELETE $BASE/photo
