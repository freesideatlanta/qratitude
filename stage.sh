#!/bin/sh

TAG=QRatitude

ant debug
RESULT=$?

if [ $RESULT -ne 0 ]; then
	exit 1;
else
	adb install bin/qratitude-debug.apk
	adb logcat -c
	adb logcat ActivityManager:E $TAG:V *:E
	exit 0;
fi
