#!/bin/sh

# NOTE: freesideatlanta.keystore is a symlink to the actual keystore; which is likely physically stored on encrypted media
jarsigner -verbose -sigalg MD5withRSA -digestalg SHA1 -keystore freesideatlanta.keystore bin/qratitude-release-unsigned.apk freeside
jarsigner -verify bin/qratitude-release-unsigned.apk
RESULT=$?

if [ $RESULT -ne 0 ]; then
	exit 1;
else
	zipalign -vf 4 bin/qratitude-release-unsigned.apk bin/qratitude.apk
	exit 0;
fi
