#!/bin/sh

ADMIN=qratitude-admin
DB=qratitude

sudo -s postgres createuser -D -P $ADMIN
# no superuser, no role creator
sudo -s postgres createdb -O $ADMIN $DB
# edit /etc/postgresql/9.1/main/pg_hba.conf
# set peer to md5 in the line: 
# local		all		all			peer
