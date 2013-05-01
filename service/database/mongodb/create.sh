#!/bin/sh

DB=qratitude
CREATE=create_collections.js

mongo $DB $CREATE
